package com.yz.aac.mining.aspect;

import com.yz.aac.common.Constants.Misc;
import com.yz.aac.common.Constants.ResponseMessageInfo;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.mining.Constants.MiningActionEnum;
import com.yz.aac.mining.Constants.ParamConfigSubclassEnum;
import com.yz.aac.mining.Constants.UserBehaviourStatisticsKey;
import com.yz.aac.mining.Constants.UserIncomeType;
import com.yz.aac.mining.model.request.GrowthRequest;
import com.yz.aac.mining.model.request.WechatCheckCodeRequest;
import com.yz.aac.mining.repository.*;
import com.yz.aac.mining.repository.domian.ParamConfig;
import com.yz.aac.mining.repository.domian.User;
import com.yz.aac.mining.repository.domian.UserMiningRecord;
import com.yz.aac.mining.repository.domian.UserNaturalGrowthRecord;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户相关aop
 */
@Order(1)
@Component
@Aspect
public class AccountAspect {
	
	@Autowired
	private UserChallengeQuestionsRecordRepository userChallengeQuestionsRecordRepository;
	
	@Autowired
	private UserBehaviourStatisticsRepository userBehaviourStatisticsRepository;
	
	@Autowired
	private UserNaturalGrowthRecordRepository userNaturalGrowthRecordRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ParamConfigRepository paramConfigRepository;
	
	@Autowired
	private UserAssertRepository userAssertRepository;
	
	@Autowired
	private UserIncomeStatisticsRepository userIncomeStatisticsRepository;
	
	@Autowired
	private UserMiningRecordRepository userMiningRecordRepository;
	
	@Autowired
	private WeChatVerificationCodeRepository weChatVerificationCodeRepository;
	
	@Before("execution(* com.yz.aac.mining.service.impl.AccountServiceImpl.wechatCheckCode(..))")
	public void wechatCheckCodeBefore(JoinPoint pjp) throws Throwable{
		Object[] args = pjp.getArgs();
		Long userId = Long.valueOf(args[1].toString());
		
		//是否关注过公众号
		List<UserMiningRecord> recordList = userMiningRecordRepository.getRecordByAction(userId, MiningActionEnum.FOCUS_ON_WECHAT.code());
		if (null != recordList && recordList.size() > 0) {
			throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "你已关注过公众号！");
		}
	}

	@Order(1)
	@AfterReturning(pointcut = "execution(* com.yz.aac.mining.service.impl.AccountServiceImpl.wechatCheckCode(..))")
	@Transactional(rollbackFor = Exception.class)
	public void wechatCheckCodeAfterReturn(JoinPoint pjp) throws Throwable{
		Object[] args = pjp.getArgs();
		WechatCheckCodeRequest wechatCheckCodeRequest = (WechatCheckCodeRequest) args[0];
    	Long userId = Long.valueOf(args[1].toString());
    	addAccountBehaviour(userId, UserBehaviourStatisticsKey.ATTENTION_PUBLIC_NUMBER.name());
    	
    	//删除验证码
    	weChatVerificationCodeRepository.deleteVerificationCode(wechatCheckCodeRequest.getCode());
	}

	@Order(1)
	@AfterReturning(pointcut = "execution(* com.yz.aac.mining.service.impl.AccountServiceImpl.signInPoint(..))")
	@Transactional(rollbackFor = Exception.class)
	public void getQuestionListAfterReturn(JoinPoint pjp) throws Throwable{
		
		Object[] args = pjp.getArgs();
    	Long userId = Long.valueOf(args[0].toString());
    	addAccountBehaviour(userId, UserBehaviourStatisticsKey.SIGN_IN.name());
	}
	
	/**
	 * 1.删除已收取过的能量记录
	 * 2.用户好友分成
	 * @param pjp
	 * @throws Throwable
	 */
	@Order(1)
	@AfterReturning(pointcut = "execution(* com.yz.aac.mining.service.impl.AccountServiceImpl.collectingEnergy(..))")
	@Transactional(rollbackFor = Exception.class)
	public void collectingEnergyAfterReturn(JoinPoint pjp) throws Throwable{
		
		Object[] args = pjp.getArgs();
		GrowthRequest growthRequest = (GrowthRequest) args[0];
		Long userId = Long.valueOf(args[1].toString());
		
		UserNaturalGrowthRecord growth = userNaturalGrowthRecordRepository.getGrowthRecordById(growthRequest.getGrowthId());
		userNaturalGrowthRecordRepository.deleteNaturalGrowthById(growthRequest.getGrowthId());
		
		User user = userRepository.getUserById(userId);
		User inviterUser = userRepository.getUserByInviterCode(String.valueOf(user.getInviterId()));
		if (null != inviterUser) {
			miningSeparation(growth.getGrowthAmount(), inviterUser.getId());
		}
		
	}
	
	
	/**
	 * 添加用户行为统计次数
	 * @param userId
	 * @param key
	 * @param addValue
	 */
	public void addAccountBehaviour(Long userId, String key){
		userBehaviourStatisticsRepository.updateBehaviourStatistics(userId, key);
	}
	
	/**
	 * 挖矿邀请人分成
	 * @param userId 用户
	 * @param balance 奖励金额
	 */
	public void miningSeparation(BigDecimal balance, Long inviterId){
		if (null != inviterId) {
			ParamConfig miningRoyaltyRate = paramConfigRepository.getParamConfig(ParamConfigSubclassEnum.MINING_ROYALTY_RATE.maxCode(), ParamConfigSubclassEnum.MINING_ROYALTY_RATE.minCode(), ParamConfigSubclassEnum.MINING_ROYALTY_RATE.name());
			balance = balance.multiply(BigDecimal.valueOf(Integer.valueOf(miningRoyaltyRate.getValue())/1000));
			if (balance.compareTo(BigDecimal.ZERO) == 1) {
				userAssertRepository.addUserAssertBalance(inviterId, Misc.PLATFORM_CURRENCY_SYMBOL.value(),
						balance, balance);
				
				//用户收入统计
				userIncomeStatisticsRepository.updateUserIncomeStatistices(inviterId, Misc.PLATFORM_CURRENCY_SYMBOL.value(), UserIncomeType.MINING.name(), balance);
			}
			
		}
	}
	
}
