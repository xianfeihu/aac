package com.yz.aac.wallet.repository.domain;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class UserIncomeStatistics {

	/** ID */
	private Long id;
	
	/** 用户ID  */
	private Long userId;
	
	/** 货币符号 */
	private String currencySymbol;
	
	/** 键  */
	private String key;
	
	/** 值  */
	private BigDecimal value;

	public UserIncomeStatistics(Long userId, String currencySymbol, String key,
			BigDecimal value) {
		super();
		this.userId = userId;
		this.currencySymbol = currencySymbol;
		this.key = key;
		this.value = value;
	}
	

}
