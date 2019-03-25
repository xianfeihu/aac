package com.yz.aac.wallet.service.impl;

import com.github.pagehelper.util.StringUtil;
import com.yz.aac.common.Constants;
import com.yz.aac.common.Constants.ExternalServiceNumber;
import com.yz.aac.common.Constants.ResponseMessageInfo;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.common.util.RandomUtil;
import com.yz.aac.wallet.Constants.*;
import com.yz.aac.wallet.aspect.targetCustom.IdentityAspect;
import com.yz.aac.wallet.aspect.targetCustom.RegisterAspect;
import com.yz.aac.wallet.aspect.targetCustom.VCodeAspect;
import com.yz.aac.wallet.model.request.ExternalRegisterUserRequest;
import com.yz.aac.wallet.model.request.IdCardMesRequest;
import com.yz.aac.wallet.model.request.LoginMesRequest;
import com.yz.aac.wallet.model.response.IdCardLoginResponse;
import com.yz.aac.wallet.model.response.IdCardResponse;
import com.yz.aac.wallet.model.response.SmsResponse;
import com.yz.aac.wallet.model.response.UserInfoResponse;
import com.yz.aac.wallet.repository.*;
import com.yz.aac.wallet.repository.domain.*;
import com.yz.aac.wallet.service.AccountService;
import com.yz.aac.wallet.service.IdentityService;
import com.yz.aac.wallet.service.SmsService;
import com.yz.aac.wallet.util.RegularUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

import static com.yz.aac.common.Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION;
import static com.yz.aac.common.Constants.ResponseMessageInfo.MSG_INTERACTIVE_EXCEPTION;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService{
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private SmsService smsService;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserAssertRepository userAssertRepository;

	@Autowired
	private UserRoleRepository userRoleRepository;
	
	@Autowired
	private UserPropertyRepository userPropertyRepository;
	
	@Autowired
	private UserSmsCodeRepository userSmsCodeRepository;
	
	@Autowired
	private IdentityService identityService;
	
	@Autowired
	private MerchantRepository merchantRepository;
	
	@Autowired
	private MerchantAssertIssuanceRepository merchantAssertIssuanceRepository;
	
	@Autowired
	private MerchantAssertIssuanceAuditRepository merchantAssertIssuanceAuditRepository;
	
	@Autowired
	private UserBirthdayLoginFailRecordRepository userBirthdayLoginFailRecordRepository;

	@VCodeAspect
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean getMobileCode(String mobile, Integer type) throws Exception {
		type = null == type ? SmsCodeType.LOGIN.code() : type;
		UserSmsCode smsCode = userSmsCodeRepository.getByMobile(Long.valueOf(mobile), type);
		SmsResponse smsRsp = null;
		if (null == smsCode || ((System.currentTimeMillis() - smsCode.getSendTime()) / (1000 * 60)) > 10) {//短信有效时间10分钟
			smsRsp = smsService.sendCode(mobile, Integer.valueOf(RandomUtil.createNumber(4)));
			
			if (YunXinErrorCodeEnum.CODE_OK.errorCode() == smsRsp.getCode()) {
				//先删除再保存验证码
				userSmsCodeRepository.deleteByMobile(Long.valueOf(mobile), type);
				userSmsCodeRepository.saveUserSmsCode(new UserSmsCode(Long.valueOf(mobile), smsRsp.getObj(), type, System.currentTimeMillis()));
				return true;
			}
		} else {
			smsRsp = smsService.sendCode(mobile, Integer.valueOf(smsCode.getCode()));
		}
		
		if (YunXinErrorCodeEnum.CODE_OK.errorCode() != smsRsp.getCode()) {
			log.error("验证码短信发送失败：" + YunXinErrorCodeEnum.findEnumByCode(smsRsp.getCode()).toString());
	        throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), (YunXinErrorCodeEnum.CODE_416.errorCode() == smsRsp.getCode()) ? "今日发送验证码次数已上限！" : "验证码短信发送失败！");
		}
		return true;
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public UserInfoResponse SMSLogin(LoginMesRequest LoginMesRequest) throws Exception {
		//校验验证码
		SmsResponse smsResponse = smsService.verifycode(LoginMesRequest.getMobile(), String.valueOf(LoginMesRequest.getCode()), SmsCodeType.LOGIN.code());
		
		if (YunXinErrorCodeEnum.CODE_OK.errorCode() == smsResponse.getCode()) {
			//判断用户是否存在
			User user = userRepository.getUserByMobile(Long.valueOf(LoginMesRequest.getMobile()));
			if (null == user) {
				//注册
				user = accountService.register(Long.valueOf(LoginMesRequest.getMobile()), (StringUtils.isEmpty(LoginMesRequest.getInviterCode())) ? "" : LoginMesRequest.getInviterCode(), ExternalServiceNumber.LOCAL_APP.code());
			} else {
				//登录
				accountAuthCheck(user.getId());
			}
			
			//返回用户基本信息
			UserRole role = userRoleRepository.getByUserId(user.getId());
			
			UserInfoResponse userInfoResponse = new UserInfoResponse(
					user.getId(), user.getMobileNumber(),
					user.getSource(), user.getRegTime(),
					role.getIsMerchant(), role.getIsAdvertiser(), user.getName(),
					user.getGender(), user.getIdNumber(), StringUtil.isEmpty(user.getPaymentPassword()) ? StateType.NO_STATE.code() : StateType.OK_STATE.code(),
							user.getInviterCode());
			
			if (role.getIsMerchant() == StateType.OK_STATE.code()) {
				//获取押金状态
				Merchant merchant = merchantRepository.getMerchantByMobile(user.getMobileNumber());
				Long issuanceId  = merchantAssertIssuanceRepository.getByMerchantId(merchant.getId());
				if (null != issuanceId) {
					List<MerchantAssertIssuanceAudit> auditList = merchantAssertIssuanceAuditRepository.getByIssuanceIdAndStatus(issuanceId, null);
					if (null != auditList && auditList.size() > 0) {
						userInfoResponse.setDepositStatus(auditList.get(0).getStatus());
					} 
				}
			}
			return userInfoResponse;
		 } else if (YunXinErrorCodeEnum.CODE_404.errorCode() == smsResponse.getCode()){
	        throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "验证码错误！");
		} else {
			log.error("验证码校验失败：" + YunXinErrorCodeEnum.findEnumByCode(smsResponse.getCode()).toString());
	        throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "验证码失效!");
		}
		
	}
	
	@Override
	public IdCardLoginResponse idCardLogin(String idCard, Long userId) throws Exception {
		accountAuthCheck(userId);
		
		User user = userRepository.getUserById(userId);
		if (!StringUtils.isEmpty(user.getIdNumber())) {
			if (idCard.equals(RegularUtil.extractYTD(user.getIdNumber()))) {
				return new IdCardLoginResponse(true);
			}
			userBirthdayLoginFailRecordRepository.saveUserBirthdayLoginFailRecord(new UserBirthdayLoginFailRecord(userId, System.currentTimeMillis()));
			List<UserBirthdayLoginFailRecord> ublfrlList = userBirthdayLoginFailRecordRepository.getUserTwentyFourDate(userId);
			int num = DefaultBirthdayEoorLoginNumEnum.NUM.code() - ((null == ublfrlList || ublfrlList.size() == 0) ? 0 : ublfrlList.size());
			if (num <= 0) {//冻结
				String des = "生日登录错误次数过多！";
				userPropertyRepository.updateStatus(userId, UserAccountStatus.DISCONTINUE_USE.code(), des);
				
				//自动清除登录错误记录
				userBirthdayLoginFailRecordRepository.deleteRecordByUserId(userId);
				throw new BusinessException(MSG_INTERACTIVE_EXCEPTION.code(), "您的账号被停用，请联系平台\n冻结原因:" + des);
			}
			throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "你的出生日期不正确，还有" + num +"次机会尝试。如果再次输错" + num + "次，您的账号和资产会暂时被冻结！");
		} 
		throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "当前用户没有实名认证");
	}
	
	/**
	 * 用户注册
	 * @param mobile 手机号码
	 * @return
	 */
	@RegisterAspect
	@Transactional(rollbackFor = Exception.class)
	public User register(Long mobile, String inviterCode, Integer registerFrom) throws Exception {
		//1.添加用户信息
		User user = new User();
		user.setMobileNumber(mobile);
		user.setSource(UserSourceType.RUNTINE_REGISTER.code());
		user.setRegTime(System.currentTimeMillis());
		user.setRegistrationType(registerFrom);
		
		if (!StringUtils.isEmpty(inviterCode)) {
			//根据唯一邀请码查找用户
			User inviterUser = userRepository.getUserByInviterCode(inviterCode);
			if (null != inviterUser) {
				user.setInviterId(inviterUser.getId());
			} else {
				throw new BusinessException(Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "邀请码不存在！");
			}
		}
		
		//防止重复邀请码
		boolean condition = true;
		while(condition){
			try{
				user.setInviterCode(RandomUtil.createNumber(8));
				userRepository.saveUser(user);
				condition = false;
			} catch(DuplicateKeyException du){
				condition = true;
				log.info("用户邀请码重复，重新生成！");
			}
		}
		
		return user;
	}
	
	@IdentityAspect
	@Override
	public IdCardResponse idCardAuth(IdCardMesRequest idCardMesRequest, Long userId) throws Exception {
		
		return identityService.Authentication(idCardMesRequest.getIdcard(), idCardMesRequest.getRealname());
		
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long externalRegisterUser(ExternalRegisterUserRequest registerUserRequest, Long serviceId) throws Exception {
		String currencySymbol = this.merchantAssertIssuanceRepository.queryCurrencyByMerchantId(registerUserRequest.getMerchantId());
		if (currencySymbol==null) {
			throw new BusinessException(Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "该商户不存在！");
		}
		// 判断用户是否存在
		User user = userRepository.getUserByMobileOrIdCard(registerUserRequest.getMobileNumber(), registerUserRequest.getIdNumber());
		if (user != null) {
			if (user.getMobileNumber().equals(registerUserRequest.getMobileNumber())) {
				if (!user.getIdNumber().equals(registerUserRequest.getIdNumber()) || !user.getName().equals(registerUserRequest.getName())) {
					throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), "该手机号已被实名注册");
				}
			}
			// 身份证校验
			if (user.getIdNumber().equals(registerUserRequest.getIdNumber())) {
				if (!user.getName().equals(registerUserRequest.getName()) || !user.getMobileNumber().equals(registerUserRequest.getMobileNumber())) {
					throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), "该身份信息已被实名注册");
				}
			}
			return user.getId();
		}

		//注册
		user = accountService.register(registerUserRequest.getMobileNumber(), "", serviceId.intValue());

		// 身份证校验
		IdCardResponse authentication = this.accountService.idCardAuth(new IdCardMesRequest(registerUserRequest.getIdNumber(), registerUserRequest.getName(), registerUserRequest.getGender(), null), user.getId());
		if (authentication.getRes()==2) {
			throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code() , "身份认证失败");
		}

		// 新建该商家下用户资产信息
		this.userAssertRepository.saveUserAssert(new UserAssert(null,
				user.getId(),
				currencySymbol,
				BigDecimal.ZERO,
				BigDecimal.ZERO,
				RandomUtil.genUUID(),
				System.currentTimeMillis()));

		return user.getId();
	}

	/**
	 * 账户禁用权限判定
	 * @param userId
	 * @throws BusinessException
	 */
	public void accountAuthCheck(Long userId) throws BusinessException{
		UserProperty uProperty = userPropertyRepository.getByUserId(userId);
		if (null == uProperty || uProperty.getStatus() == StateType.NO_STATE.code()) {
			 throw new BusinessException(MSG_INTERACTIVE_EXCEPTION.code(), "您的账号被停用，请联系平台\n冻结原因:" + (StringUtils.isEmpty(uProperty.getStatusDescription()) ? "无" : uProperty.getStatusDescription()));
		}
	}
	
}
