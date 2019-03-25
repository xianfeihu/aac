package com.yz.aac.wallet.repository.domain;

import lombok.Data;

@Data
public class IncreaseStrategy {

	/** 用户ID */
	private Long id;
	
	/** 策略名称  */
	private String name;
	
	/** 昨日增长元力基数 */
	private Integer increasedPowerPoint;
	
	/** 昨日广告有效点击率或提交信息总数量（基数）  */
	private Integer consumedAd;
	
	/** 昨日AAC数量（基数）  */
	private Integer platformCurrency;
	
	/** 是否启用(1-是 2-否)  */
	private Integer isDefault;
	
	/** 创建时间 */
	private Long createTime;
	
	/** 修改时间 */
	private Long updateTime;

}
