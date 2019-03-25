package com.yz.aac.wallet.repository.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@ApiModel("商户币交易记录")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MerchantAssertTradeRecord {

    @ApiModelProperty(value = "ID", position = 1)
    private Long id;

    @ApiModelProperty(value = "交易发起人ID", position = 2, required = true)
    private Long initiatorId;

    @ApiModelProperty(value = "交易发起人名称", position = 3, required = true)
    private String initiatorName;

    @ApiModelProperty(value = "交易类型 1: 买入 2: 售出 3: 转账", position = 4, required = true)
    private Long tradeType;

    @ApiModelProperty(value = "交易时间", position = 5)
    private Long tradeTime;

    @ApiModelProperty(value = "货币符号", position = 6, required = true)
    private String currencySymbol;

    @ApiModelProperty(value = "平台币单价（每一个单位的商户币兑换多少平台币）", position = 7)
    private BigDecimal platformPrice;

    @ApiModelProperty(value = "交易额度", position = 8, required = true)
    private BigDecimal tradeAmount;

    @ApiModelProperty(value = "交易完成后的平台币余额", position = 9)
    private BigDecimal balance;

    @ApiModelProperty(value = "交易完成后的平台币有效余额", position = 10)
    private BigDecimal validBalance;

    @ApiModelProperty(value = "交易完成后商家币余额", position = 11)
    private BigDecimal merchantBalance;

    @ApiModelProperty(value = "交易完成后商家币有效余额", position = 12)
    private BigDecimal merchantValidBalance;

    @ApiModelProperty(value = "交易伙伴ID", position = 13, required = true)
    private Long partnerId;

    @ApiModelProperty(value = "交易伙伴名称", position = 14, required = true)
    private String partnerName;

    @ApiModelProperty(value = "消费流向", position = 15)
    private String tradeResult;

    @ApiModelProperty(value = "订单ID", position = 16)
    private String orderId;

    /**
     * 查询转账记录
     */
    public MerchantAssertTradeRecord(Integer initiatorId, String initiatorName,Integer partnerId, String partnerName, Long tradeType, Long tradeTime, BigDecimal tradeAmount, String currencySymbol) {
        this.initiatorId = initiatorId.longValue();
        this.initiatorName = initiatorName;
        this.partnerId = partnerId.longValue();
        this.tradeTime = tradeTime;
        this.tradeAmount = tradeAmount;
        this.partnerName = partnerName;
        this.currencySymbol = currencySymbol;
        this.tradeType = tradeType;
    }

    /**
     * 归纳转账记录
     */
    public MerchantAssertTradeRecord(Long initiatorId, String currencySymbol, BigDecimal tradeAmount) {
        this.initiatorId = initiatorId;
        this.currencySymbol = currencySymbol;
        this.tradeAmount = tradeAmount;
    }

    @ApiModelProperty(value = "花费多少平台币", position = 13)
    public BigDecimal getUsePlatCurrencyAmount() {
        if (platformPrice==null) return null;
        return platformPrice.multiply(tradeAmount);
    }
}
