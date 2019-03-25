package com.yz.aac.exchange.repository.domian;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserOrder {

	/** 买卖单ID */
	private Long id;
	
	/** 买卖单人ID  */
	private Long userId;
	
	/** 原始额度（等于最开始出售额度定额不变） */
	private BigDecimal originalLimit;
	
	/** 出售额度 */
	private BigDecimal availableTradeAmount;
	
	/** 货币符号  */
	private String currencySymbol;
	
	/** AAB单价  */
	private BigDecimal aabPrice;
	
	/** 备注  */
	private String remark;
	
	/** 发布类型(1-买单 2-卖单) */
	private Integer type;
	
	/** 状态(1-上架 2-下架) */
	private Integer status;
	
	/** 创建时间 */
	private Long createTime;
	
	/** 修改时间 */
	private Long updateTime;

	public UserOrder(Long userId, BigDecimal originalLimit, BigDecimal availableTradeAmount,
			String currencySymbol, BigDecimal aabPrice, String remark,
			Integer type, Integer status, Long createTime) {
		super();
		this.userId = userId;
		this.originalLimit = originalLimit;
		this.availableTradeAmount = availableTradeAmount;
		this.currencySymbol = currencySymbol;
		this.aabPrice = aabPrice;
		this.remark = remark;
		this.type = type;
		this.status = status;
		this.createTime = createTime;
	}
	
}
