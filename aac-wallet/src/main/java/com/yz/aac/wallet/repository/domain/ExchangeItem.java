package com.yz.aac.wallet.repository.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExchangeItem {

	/** ID */
	private Long id;
	
	/** 兑换服务ID */
	private Long exchangeId;
	
	/** 兑换法币金额  */
	private Integer rmbAmount;

	/** 平台币付款金额  */
	private BigDecimal platformAmount;
	
	/** 排序号  */
	private Integer orderNumber;
}
