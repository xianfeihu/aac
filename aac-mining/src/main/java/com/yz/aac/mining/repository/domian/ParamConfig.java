package com.yz.aac.mining.repository.domian;

import lombok.Data;

@Data
public class ParamConfig {

	/** ID */
	private Long id;
	
	/** 大类（1-挖矿 2-交易）  */
	private Integer category;
	
	/** 小类（1-1: 阅读 1-2: 实名认证1-3: 签到1-4: 关注公众号1-5: 邀请好友2-1: 挂单买卖2-2: 汇率）  */
	private Integer direction;
	
	/** 键  */
	private String key;
	
	/** 值  */
	private String value;
	
}
