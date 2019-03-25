package com.yz.aac.exchange.repository.domian;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PlatformServiceChargeStrategy {

	/** ID */
	private Long id;
	
	/** 策略名称  */
	private String name;
	
	/** 交易费率（不含%） */
	private BigDecimal tradeChargeRate;
	
	/** 发币押金  */
	private BigDecimal issuanceDeposit;
	
	/** 是否是默认策略（1-是 2-否）  */
	private Integer isDefault;
	
	/** 创建时间  */
	private Long createTime;
	
	/** 更新时间 */
	private Long updateTime;
	
}
