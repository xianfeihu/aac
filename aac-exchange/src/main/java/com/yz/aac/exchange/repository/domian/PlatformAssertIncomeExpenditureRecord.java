package com.yz.aac.exchange.repository.domian;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlatformAssertIncomeExpenditureRecord {

	/** ID */
	private Long id;
	
	/** 进出账方向（1-进账 2-出账）  */
	private Integer direction;
	 
	/** 用户ID */
	private Long userId;
	
	/** 用户名称  */
	private String userName;
	
	/** 记录时间  */
	private Long actionTime;
	
	/** 平台币交易金额  */
	private BigDecimal amount;
	
	/** 法币交易金额（仅限进账） */
	private BigDecimal rmbAmount;
	
	/** 交易类型（1-充值平台<进账> 2-发币押金<进账> 3-交易手续费<进账> 4-广告费<进账> 5-购买平台币转账<出账>）  */
	private Integer action;

	public PlatformAssertIncomeExpenditureRecord(Integer direction,
			Long userId, String userName, Long actionTime, BigDecimal amount,
			BigDecimal rmbAmount, Integer action) {
		super();
		this.direction = direction;
		this.userId = userId;
		this.userName = userName;
		this.actionTime = actionTime;
		this.amount = amount;
		this.rmbAmount = rmbAmount;
		this.action = action;
	}
	
}
