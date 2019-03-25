package com.yz.aac.wallet.aspect;

import com.yz.aac.common.Constants.Misc;
import com.yz.aac.common.Constants.ResponseMessageInfo;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.common.util.RandomUtil;
import com.yz.aac.wallet.Constants.*;
import com.yz.aac.wallet.model.request.IdCardMesRequest;
import com.yz.aac.wallet.model.response.IdCardResponse;
import com.yz.aac.wallet.repository.*;
import com.yz.aac.wallet.repository.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static com.yz.aac.common.Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION;

/**
 * 用户aop
 */
@Slf4j
@Order(1)
@Component
@Aspect
public class AccountAspect {
	
	/** 验证码repository */
	@Autowired
	private UserSmsCodeRepository userSmsCodeRepository;
	
	/** 用户repository */
	@Autowired
	private UserRepository userRepository;
	
	/** 商户账号repository */
	@Autowired
	private MerchantRepository merchantRepository;
	
	/** 用户角色repository */
	@Autowired
	private UserRoleRepository userRoleRepository;
	
	/** 用户资产repository */
	@Autowired
	private UserAssetsRepository userAssertRepository;
	
	/** 用户属性值repository */
	@Autowired
	private UserPropertyRepository userPropertyRepository;
	
	/** 用策略属性repository*/
	@Autowired
	private IncreaseStrategyRepository increaseStrategyRepository;
	
	@Autowired
	private UserIncomeStatisticsRepository userIncomeStatisticsRepository;
	
	@Autowired
	private UserBehaviourStatisticsRepository userBehaviourStatisticsRepository;
	
	@Autowired
	private ParamConfigRepository paramConfigRepository;
	
	@Autowired
	private UserMiningRecordRepository userMiningRecordRepository;

	/**
	 * 验证码时效校验
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Before("@annotation(com.yz.aac.wallet.aspect.targetCustom.VCodeAspect)")
	public void verificationCode(JoinPoint pjp) throws Throwable{
		
		Object[] args = pjp.getArgs();
    	String mobile = String.valueOf(args[0]);
    	Integer type = null == args[1] ? SmsCodeType.LOGIN.code() : Integer.valueOf(args[1].toString());
    	
    	//验证码短信时间两分钟
		Predicate<String> predicate = str -> {
			UserSmsCode smsCode = userSmsCodeRepository.getByMobile(Long.valueOf(str), type);
			if (null == smsCode) {
				return true;
			}
			
			if (((System.currentTimeMillis() - smsCode.getSendTime()) / (1000 * 60)) > 2) {
				return true;
			} 
			return false;
		};
		
		if (!predicate.test(mobile)) {
			throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "120秒内不能重复发送，请耐心等待！");
		}
	}
	
	/**
	 * 用户注册成功后完成事件：
	 * 1.为用户添加角色
	 * 2.初始化用户（AAB）资产<此时钱包地址为null>
	 * 3.初始化用户元力值等基本属性
	 * 4.同时存在相同手机号商户并且已经实名认证则执行 AccountAspect.buildUserOtherAttr(..)
	 * 5.初始化用户平台币资产统计
	 * 6.初始化用户行为统计
	 * 
	 * @param pjp
	 * @param user
	 * @throws Throwable
	 */
	@Order(1)
	@AfterReturning(returning = "user",pointcut = "@annotation(com.yz.aac.wallet.aspect.targetCustom.RegisterAspect)")
	@Transactional(rollbackFor = Exception.class)
	public void register(JoinPoint pjp, User user) throws Throwable{
		
//		Object[] args = pjp.getArgs();
//    	Long mobile = Long.valueOf(args[1].toString());
    	
		//（1）
		UserRole role = new UserRole(user.getId(), StateType.NO_STATE.code() , StateType.NO_STATE.code());
		Merchant merchant = merchantRepository.getMerchantByMobile(user.getMobileNumber());
		if (null != merchant) {
			role.setIsMerchant(StateType.OK_STATE.code());
		}
		userRoleRepository.saveUserRole(role);
		
		//（2）
		userAssertRepository.saveUserAssets(new UserAssets(user.getId(), Misc.PLATFORM_CURRENCY_SYMBOL.value(),
				new BigDecimal(0), new BigDecimal(0), null));
		
		//（3）
		UserProperty uProperty = new UserProperty(user.getId(), 0, increaseStrategyRepository.getByEnabled(StateType.OK_STATE.code()), StateType.OK_STATE.code());
		userPropertyRepository.saveUserProperty(uProperty);
		
		//（4）
		if (null != merchant && !StringUtils.isEmpty(merchant.getIdNumber())) {
			IdCardMesRequest idCardMesRequest = new IdCardMesRequest(merchant.getIdNumber(), merchant.getName(), merchant.getGender(), merchant.getCreateTime());
			buildUserOtherAttr(idCardMesRequest, user.getId());

			user.setIdNumber(merchant.getIdNumber());
			user.setName(merchant.getName());
			user.setGender(merchant.getGender());

		}
		
		//（5）
		List<String> keyList = new ArrayList<>();
		for (UserIncomeType uit : UserIncomeType.values()) {
			keyList.add(uit.code());
		}
		if (null != keyList && keyList.size() > 0) {
			userIncomeStatisticsRepository.saveUserIncomeStatistics(user.getId(), Misc.PLATFORM_CURRENCY_SYMBOL.value(), keyList, BigDecimal.valueOf(0.00D));
		}
		
		//（6）
		keyList.clear();
		for (UserBehaviourStatisticsKey ubsk : UserBehaviourStatisticsKey.values()) {
			keyList.add(ubsk.name());
		}
		if (null != keyList && keyList.size() > 0) {
			userBehaviourStatisticsRepository.batchAdd(user.getId(), keyList, 0);
		}
		
	}
	
	/**
	 * 实名认证完成事件
	 * @param pjp
	 * @param idCardResponse
	 * @throws Throwable
	 */
	@Order(1)
	@AfterReturning(returning = "idCardResponse",pointcut = "execution(* com.yz.aac.wallet.service.impl.AccountServiceImpl.idCardAuth(..))")
	@Transactional(rollbackFor = Exception.class)
	public void idCardAuth(JoinPoint pjp, IdCardResponse idCardResponse) throws Throwable{
		try{
			Object[] args = pjp.getArgs();
			IdCardMesRequest idCardMesRequest = (IdCardMesRequest) args[0];
			idCardMesRequest.setRealNameCrtTime(System.currentTimeMillis());
			
			Long userId = Long.valueOf(args[1].toString());
			
			if (idCardResponse.getRes() == StateType.OK_STATE.code()) {//匹配
				buildUserOtherAttr(idCardMesRequest, userId);
			}
		} catch(NullPointerException e){
			log.info("没有对应APP用户账号，无需添加用户属性！");
		}
	}
	
	/**
	 * 构建用户属性
	 * 1.添加用户身份信息
	 * 2.生成AAB地址
	 * 3.赠送默认AAB，并且记录挖矿记录
	 * 4.添加实名认证时间
	 * 5.好友邀请
	 * @param idCardMesRequest
	 * @param userId
	 */
	private void buildUserOtherAttr(IdCardMesRequest idCardMesRequest, Long userId){
		User user = userRepository.getUserById(userId);

		//（1）
		userRepository.updateUserIDCard(idCardMesRequest.getIdcard(), idCardMesRequest.getRealname(), idCardMesRequest.getGender(), userId);
		
		//（2）
		boolean condition = true;
		while(condition){
			try{
				userAssertRepository.updateUserAssetsAdds(RandomUtil.genUUID(), userId, Misc.PLATFORM_CURRENCY_SYMBOL.value());
				condition = false;
			} catch(DuplicateKeyException du){
				condition = true;
				log.info("钱包地址重复，重新生成！");
			}
		}
		
		//（3）
		ParamConfig paramConfig = paramConfigRepository.getParamConfig(ParamConfigSubclassEnum.REAL_NAME_VERIFICATION_CURRENCY.maxCode(), ParamConfigSubclassEnum.REAL_NAME_VERIFICATION_CURRENCY.minCode(), ParamConfigSubclassEnum.REAL_NAME_VERIFICATION_CURRENCY.name());
		int upset = userAssertRepository.addUserAssertBalance(userId, Misc.PLATFORM_CURRENCY_SYMBOL.value(), BigDecimal.valueOf(Double.valueOf(paramConfig.getValue())), BigDecimal.valueOf(Double.valueOf(paramConfig.getValue())));
		if (upset > 0) {
			//挖矿记录
			userMiningRecordRepository.saveMiningRecord(new UserMiningRecord(userId, user.getInviterId(), MiningActionEnum.CERTIFICATION.code(), System.currentTimeMillis(), BigDecimal.valueOf(Double.valueOf(paramConfig.getValue())), MiningBonusTypeEnum.PLATFORM_CURRENCY.code()));
			
			//用户收入统计
			userIncomeStatisticsRepository.updateUserIncomeStatistices(userId, Misc.PLATFORM_CURRENCY_SYMBOL.value(), UserIncomeType.MINING.name(), BigDecimal.valueOf(Double.valueOf(paramConfig.getValue())));
			
			//挖矿邀请人分成
			miningSeparation(BigDecimal.valueOf(Double.valueOf(paramConfig.getValue())), user.getInviterId());
		}
		//（4）
		userPropertyRepository.updateRealNameCrtTime(userId, idCardMesRequest.getRealNameCrtTime());

		//（7）
		if (null != user.getInviterId()) {
			//判断是否超过邀请好友限制
			ParamConfig maxInvitationPerDay = paramConfigRepository.getParamConfig(ParamConfigSubclassEnum.MAX_INVITATION_PER_DAY.maxCode(), ParamConfigSubclassEnum.MAX_INVITATION_PER_DAY.minCode(), ParamConfigSubclassEnum.MAX_INVITATION_PER_DAY.name());
			List<UserMiningRecord> miningList = userMiningRecordRepository.getRecordToDay(user.getInviterId(), MiningActionEnum.INVITE_FRIENDS.code());

			ParamConfig invitationCurrency = paramConfigRepository.getParamConfig(ParamConfigSubclassEnum.INVITATION_CURRENCY.maxCode(), ParamConfigSubclassEnum.INVITATION_CURRENCY.minCode(), ParamConfigSubclassEnum.INVITATION_CURRENCY.name());
			if (null == miningList || miningList.size() < Integer.valueOf(maxInvitationPerDay.getValue())) {
				//好友奖励
				int index = userAssertRepository.addUserAssertBalance(user.getInviterId(), Misc.PLATFORM_CURRENCY_SYMBOL.value(), BigDecimal.valueOf(Double.valueOf(invitationCurrency.getValue())), BigDecimal.valueOf(Double.valueOf(invitationCurrency.getValue())));
				if (index > 0) {
					//挖矿记录
					User inviterUser = userRepository.getUserById(user.getInviterId());
					userMiningRecordRepository.saveMiningRecord(new UserMiningRecord(user.getInviterId(), inviterUser.getInviterId(), MiningActionEnum.INVITE_FRIENDS.code(), System.currentTimeMillis(), BigDecimal.valueOf(Double.valueOf(invitationCurrency.getValue())), MiningBonusTypeEnum.PLATFORM_CURRENCY.code()));
					//用户收入统计
					userIncomeStatisticsRepository.updateUserIncomeStatistices(user.getInviterId(), Misc.PLATFORM_CURRENCY_SYMBOL.value(), UserIncomeType.MINING.name(), BigDecimal.valueOf(Double.valueOf(invitationCurrency.getValue())));

					//挖矿邀请人分成
					miningSeparation(BigDecimal.valueOf(Double.valueOf(invitationCurrency.getValue())), inviterUser.getInviterId());

				}
			}

			//邀请码注册奖励
			int index = userAssertRepository.addUserAssertBalance(user.getId(), Misc.PLATFORM_CURRENCY_SYMBOL.value(), BigDecimal.valueOf(Double.valueOf(invitationCurrency.getValue())), BigDecimal.valueOf(Double.valueOf(invitationCurrency.getValue())));
			if (index > 0) {
				//挖矿记录
				userMiningRecordRepository.saveMiningRecord(new UserMiningRecord(user.getId(), user.getInviterId(), MiningActionEnum.INVITATION_CODE_REGISTER.code(), System.currentTimeMillis(), BigDecimal.valueOf(Double.valueOf(invitationCurrency.getValue())), MiningBonusTypeEnum.PLATFORM_CURRENCY.code()));
				//用户收入统计
				userIncomeStatisticsRepository.updateUserIncomeStatistices(user.getId(), Misc.PLATFORM_CURRENCY_SYMBOL.value(), UserIncomeType.MINING.name(), BigDecimal.valueOf(Double.valueOf(invitationCurrency.getValue())));

				//挖矿邀请人分成
				miningSeparation(BigDecimal.valueOf(Double.valueOf(invitationCurrency.getValue())), user.getInviterId());
			}

			//邀请人邀请好友行为+1
			userBehaviourStatisticsRepository.addBehaviourStatistics(user.getInviterId(), UserBehaviourStatisticsKey.INVITE.name(), 1);

		}
	}
	
	/**
	 * 实名身份本地校验
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Before("@annotation(com.yz.aac.wallet.aspect.targetCustom.IdentityAspect)")
	public void identityLocalCheck(JoinPoint pjp) throws Throwable{
		
		Object[] args = pjp.getArgs();
		IdCardMesRequest idCardMesRequest = (IdCardMesRequest) args[0];
		
		//检查该身份证是否已经绑定使用
		if (null != userRepository.queryUserByIdNumber(idCardMesRequest.getIdcard())
				|| null != merchantRepository.queryMerchantByIdNumber(idCardMesRequest.getIdcard())) {
			throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), "该身份证已经绑定使用！");
		}
				
	}
	
	/**
	 * 挖矿邀请人分成
	 * @param inviterId 用户
	 * @param balance 奖励金额
	 */
	public void miningSeparation(BigDecimal balance, Long inviterId){
		if (null != inviterId) {
			ParamConfig miningRoyaltyRate = paramConfigRepository.getParamConfig(ParamConfigSubclassEnum.MINING_ROYALTY_RATE.maxCode(), ParamConfigSubclassEnum.MINING_ROYALTY_RATE.minCode(), ParamConfigSubclassEnum.MINING_ROYALTY_RATE.name());
			balance =balance.multiply(BigDecimal.valueOf(Integer.valueOf(miningRoyaltyRate.getValue())/1000));
			if (balance.compareTo(BigDecimal.ZERO) == 1) {
				userAssertRepository.addUserAssertBalance(inviterId, Misc.PLATFORM_CURRENCY_SYMBOL.value(),
						balance, balance);
				
				//用户收入统计
				userIncomeStatisticsRepository.updateUserIncomeStatistices(inviterId, Misc.PLATFORM_CURRENCY_SYMBOL.value(), UserIncomeType.MINING.name(), balance);
			}
		}
	}
	
}
