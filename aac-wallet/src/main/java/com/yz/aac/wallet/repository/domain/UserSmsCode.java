package com.yz.aac.wallet.repository.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserSmsCode {

	/** ID */
	private Long id;
	
	/** 电话号码  */
	private Long mobileNumber;
	
	/** 验证码 */
	private String code;
	
	/** 验证码类型<1-登录 2-修改密码>*/
	private Integer type;
	
	/** 发送时间  */
	private Long sendTime;

	public UserSmsCode(Long mobileNumber, String code, Integer type, Long sendTime) {
		super();
		this.mobileNumber = mobileNumber;
		this.code = code;
		this.type = type;
		this.sendTime = sendTime;
	}
	
}
