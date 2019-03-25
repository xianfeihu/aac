package com.yz.aac.wallet.service;

import com.yz.aac.wallet.model.response.SmsResponse;


/**
 * 短信服务
 * @author Xian.FeiHu
 *
 */
public interface SmsService {

	/**
	 * 发送短信验证码
	 * @param mobile 手机号
	 * @param authCode 自定义短信验证码 （4～10个数字）
	 * @return
	 * @throws Exception
	 */
	SmsResponse sendCode(String mobile, Integer authCode) throws Exception;
	
	/**
	 * 校验验证码
	 * @param mobile 手机号
	 * @param code 验证码
	 * @param type 验证码类型<1-登录 2-修改密码>
	 * @return
	 * @throws Exception
	 */
	SmsResponse verifycode(String mobile, String code, Integer type) throws Exception;
}
