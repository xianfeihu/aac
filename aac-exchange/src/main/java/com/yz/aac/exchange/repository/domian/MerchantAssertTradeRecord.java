package com.yz.aac.exchange.repository.domian;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * 商户币交易记录
 *
 */
public class MerchantAssertTradeRecord {

    /**
     * ID
     */
    private Long id;

    /**
     * 交易发起人ID
     */
    private Long initiatorId;

    /**
     * 交易发起人名称
     */
    private String initiatorName;

    /**
     * 交易类型 1: 买入 2: 售出 3: 转账
     */
    private Integer tradeType;

    /**
     * 交易时间
     */
    private Long tradeTime;

    /**
     * 货币符号
     */
    private String currencySymbol;

    /**
     * 平台币单价（每一个单位的商户币兑换多少平台币）
     */
    private BigDecimal platformPrice;

    /**
     * 交易额度
     */
    private BigDecimal tradeAmount;

    /**
     * 交易完成后的平台币余额
     */
    private BigDecimal balance;
    
    /**
     * 交易完成后的平台币有效余额
     */
    private BigDecimal validBalance;
    
    /**
     * 交易完成后商家币余额
     */
    private BigDecimal merchantBalance;
    
    /**
     * 交易完成后商家币有效余额
     */
    private BigDecimal merchantValidBalance;
    
    /**
     * 交易伙伴ID
     */
    private Long partnerId;

    /**
     * 交易伙伴名称
     */
    private String partnerName;

    /**
     * 消费流向
     */
    private String tradeResult;

    /**
     * 挂单ID
     */
    private Long orderId;

	public MerchantAssertTradeRecord(Long initiatorId, String initiatorName,
			Integer tradeType, Long tradeTime, String currencySymbol,
			BigDecimal platformPrice, BigDecimal tradeAmount,
			BigDecimal balance, Long partnerId, String partnerName,
			BigDecimal validBalance, BigDecimal merchantBalance,BigDecimal merchantValidBalance,
			Long orderId) {
		super();
		this.initiatorId = initiatorId;
		this.initiatorName = initiatorName;
		this.tradeType = tradeType;
		this.tradeTime = tradeTime;
		this.currencySymbol = currencySymbol;
		this.platformPrice = platformPrice;
		this.tradeAmount = tradeAmount;
		this.balance = balance;
		this.partnerId = partnerId;
		this.partnerName = partnerName;
		this.validBalance = validBalance;
		this.merchantBalance = merchantBalance;
		this.merchantValidBalance = merchantValidBalance;
		this.orderId = orderId;
	}
    
	public MerchantAssertTradeRecord(Long tradeTime, String currencySymbol,Integer tradeType,
			BigDecimal platformPrice, BigDecimal tradeAmount, Long orderId) {
		super();
		this.tradeTime = tradeTime;
		this.currencySymbol = currencySymbol;
		this.tradeType = tradeType;
		this.platformPrice = platformPrice;
		this.tradeAmount = tradeAmount;
		this.orderId = orderId;
	}
    
}
