package com.yz.aac.exchange.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ApiModel("商户币交易记录")
public class MerchantAssertTradeRecordResponse {

    @ApiModelProperty(value = "ID", position = 1)
    private Long id;

    @ApiModelProperty(value = "交易发起人ID", position = 2)
    private Long initiatorId;

    @ApiModelProperty(value = "交易发起人名称", position = 3)
    private String initiatorName;

    @ApiModelProperty(value = "交易类型 1: 买入 2: 售出 3: 转账", position = 4)
    private Integer tradeType;

    @ApiModelProperty(value = "交易时间", position = 5)
    private Long tradeTime;

    @ApiModelProperty(value = "货币符号", position = 6)
    private String currencySymbol;

    @ApiModelProperty(value = "平台币单价（每一个单位的商户币兑换多少平台币）", position = 7)
    private BigDecimal platformPrice;

    @ApiModelProperty(value = "交易额度", position = 8)
    private BigDecimal tradeAmount;

    @ApiModelProperty(value = "交易完成后的平台币余额", position = 9)
    private BigDecimal balance;
    
    @ApiModelProperty(value = "交易完成后的平台币有效余额", position = 10)
    private BigDecimal validBalance;
    
    @ApiModelProperty(value = "交易完成后商家币余额", position = 11)
    private BigDecimal merchantBalance;
    
    @ApiModelProperty(value = "交易完成后商家币有效余额", position = 12)
    private BigDecimal merchantValidBalance;
    
    @ApiModelProperty(value = "交易伙伴ID", position = 13)
    private Long partnerId;

    @ApiModelProperty(value = "交易伙伴名称", position = 14)
    private String partnerName;

    @ApiModelProperty(value = "消费流向", position = 15)
    private String tradeResult;

	public MerchantAssertTradeRecordResponse(Long initiatorId, String initiatorName,
			Integer tradeType, Long tradeTime, String currencySymbol,
			BigDecimal platformPrice, BigDecimal tradeAmount,
			BigDecimal balance, Long partnerId, String partnerName) {
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
	}
    
    public MerchantAssertTradeRecordResponse() {
    }

	public MerchantAssertTradeRecordResponse(Long tradeTime, String currencySymbol,
			BigDecimal platformPrice, BigDecimal tradeAmount) {
		super();
		this.tradeTime = tradeTime;
		this.currencySymbol = currencySymbol;
		this.platformPrice = platformPrice;
		this.tradeAmount = tradeAmount;
	}
    
}
