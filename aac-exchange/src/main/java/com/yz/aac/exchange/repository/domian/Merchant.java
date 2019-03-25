package com.yz.aac.exchange.repository.domian;

import lombok.Data;

@Data
public class Merchant {

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
	
	/** 创建时间  */
	private Long createTime;

}
