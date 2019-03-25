package com.yz.aac.exchange.repository.domian;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PlatformAssertSellingOrder {

	/** ID */
	private Long id;
	
	/** 挂单人ID  */
	private Long sellerId;
	
	/** 可出售总额 */
	private BigDecimal availableTradeAmount;
	
	/** 单笔最小金额  */
	private BigDecimal minAmountLimit;
	
	/** 单笔最大限额  */
	private BigDecimal maxAmountLimit;
	
	/** 法币单价  */
	private BigDecimal rmbPrice;
	
	/** 备注 */
	private String remark;
	
	/** 创建时间 */
	private Long createTime;
	
	/** 更新时间 */
	private Long updateTime;
	
}
