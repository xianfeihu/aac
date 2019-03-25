package com.yz.aac.wallet.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Merchant {

	/** 用户ID */
	private Long id;
	
	/** 真实姓名  */
	private String name;

	/** 商户名称  */
	private String merchantName;

	/** 商家二维码连接 */
	private String merchantVisitUrl;
	
	/** 性别(1-男 2-女) */
	private Integer gender;
	
	/** 身份证号码  */
	private String idNumber;
	
	/** 电话号码  */
	private Long mobileNumber;
	
	/** 创建时间  */
	private Long createTime;

}
