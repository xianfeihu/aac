package com.yz.aac.exchange.repository.domian;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlatformAssertTradeRecord {

	/** ID */
	private Long id;
	
	/** 购买人ID  */
	private Long initiatorId;
	
	/** 购买人名称 */
	private String initiatorName;
	
	/** 交易类型 （1-商户发币押金 2-买入 3-转账 4-分红）  */
	private Integer tradeType;
	
	/** 交易时间  */
	private Long tradeTime;
	
	/** 法币单价（每一个单位的平台币兑换多少法币）  */
	private BigDecimal rmbPrice;
	
	/** 可交易额度 */
	private BigDecimal availableTradeAmount;
	
	/** 实际交易额度*/
	private BigDecimal tradeAmount;
	
	/** 单笔最小限额 */
	private BigDecimal minAmountLimit;
	
	/** 单笔最大限额 */
	private BigDecimal maxAmountLimit;
	
	/** 平台币钱包地址 */
	private String walletAddress;
	
	/** 交易完成后的平台币余额 */
	private BigDecimal balance;
	
	/** 交易完成后的可用平台币余额 */
	private BigDecimal validBalance;
	
	/** 交易伙伴ID */
	private Long partnerId;
	
	/** 交易伙伴名称 */
	private String partnerName;
	
	/** 付款参考号 */
	private String payNumber;
	
	/** 转账审核状态 （1-后台待转账 2-已完成 3-已取消） */
	private Integer status;
	
	/** 平台币挂单ID */
	private Long sellingOrderId;

	public PlatformAssertTradeRecord(Long initiatorId, String initiatorName,
			Integer tradeType, Long tradeTime, BigDecimal rmbPrice,
			BigDecimal tradeAmount, String walletAddress, BigDecimal balance, BigDecimal validBalance, 
			Long partnerId, String partnerName, String payNumber, Integer status, Long sellingOrderId) {
		super();
		this.initiatorId = initiatorId;
		this.initiatorName = initiatorName;
		this.tradeType = tradeType;
		this.tradeTime = tradeTime;
		this.rmbPrice = rmbPrice;
		this.tradeAmount = tradeAmount;
		this.walletAddress = walletAddress;
		this.balance = balance;
		this.validBalance = validBalance;
		this.partnerId = partnerId;
		this.partnerName = partnerName;
		this.payNumber = payNumber;
		this.status = status;
		this.sellingOrderId = sellingOrderId;
	}
	
	
	
}
