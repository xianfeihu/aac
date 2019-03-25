package com.yz.aac.mining.repository.domian;

import lombok.Data;

@Data
public class User {

	/** 用户ID */
	private Long id;
	
	/** 真实姓名  */
	private String name;
	
	/** 性别(1-男 2-女) */
	private Integer gender;
	
	/** 身份证号码  */
	private String idNumber;
	
	/** 电话号码  */
	private Long mobileNumber;
	
	/** 支付密码  */
	private String paymentPassword;
	
	/** 用户来源(1-常规注册用户 2-虚拟挂单用户) */
	private Integer source;
	
	/** 注册时间  */
	private Long regTime;
	
	/** 推荐人ID */
	private Long inviterId;
	
	/** 用户邀请码 */
	private String inviterCode;

}
