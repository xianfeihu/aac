package com.yz.aac.wallet.service;

import com.yz.aac.wallet.model.request.ExternalRegisterUserRequest;
import com.yz.aac.wallet.model.request.IdCardMesRequest;
import com.yz.aac.wallet.model.request.LoginMesRequest;
import com.yz.aac.wallet.model.response.IdCardLoginResponse;
import com.yz.aac.wallet.model.response.IdCardResponse;
import com.yz.aac.wallet.model.response.UserInfoResponse;
import com.yz.aac.wallet.repository.domain.User;


/**
 * APP用户登录流程服务
 *
 */
public interface AccountService {

	/**
	 * 获取手机验证码
	 * @param mobile 手机号
	 * @param type 验证码类型<1-登录 2-修改密码 3-商户实名认证 4-小程序同步>
	 * @return
	 * @throws Exception
	 */
	boolean getMobileCode(String mobile, Integer type) throws Exception;
	
	/**
	 * 登录
	 * @param LoginMesRequest
	 * @return
	 * @throws Exception
	 */
	UserInfoResponse SMSLogin(LoginMesRequest LoginMesRequest) throws Exception;
	
	/**
	 * 出生日期登录
	 * @param idCard 身份证（年月日八位字符串）
	 * @param userId 用户ID
	 * @return
	 * @throws Exception
	 */
	IdCardLoginResponse idCardLogin(String idCard, Long userId) throws Exception;

	/**
	 * 注册
	 * @param mobile
	 * @param inviterCode
	 * @param registerFrom 注册来源
	 * @return
	 */
	User register(Long mobile, String inviterCode, Integer registerFrom) throws Exception ;
	
	/**
	 * 身份证实名认证
	 * @param idCardMesRequest
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	IdCardResponse idCardAuth(IdCardMesRequest idCardMesRequest, Long userId) throws Exception;

	/**
	 * 外部服务器调用用户注册（注册-认证-绑定）
	 * @param registerUserRequest
	 * @param serviceId
	 * @return
	 */
	Long externalRegisterUser(ExternalRegisterUserRequest registerUserRequest, Long serviceId) throws Exception;
}
