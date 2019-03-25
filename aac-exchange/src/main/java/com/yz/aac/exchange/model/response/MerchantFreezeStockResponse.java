package com.yz.aac.exchange.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@ApiModel("冻结卖单明细")
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MerchantFreezeStockResponse {

    @ApiModelProperty(value = "卖单号", position = 1)
    private Long id;

    @ApiModelProperty(value = "现存冻结币量", position = 2)
    private BigDecimal availableTradeAmount;

    @ApiModelProperty(value = "初始冻结币量", position = 3)
    private BigDecimal originalLimit;

    @ApiModelProperty(value = "限价(单位:AAB)", position = 4)
    private BigDecimal aabPrice;

    @ApiModelProperty(value = "获得平台币", position = 5)
    private BigDecimal aboutPlatformCurrency;

    public Long getId() {
        return id;
    }

    public BigDecimal getAvailableTradeAmount() {
        return availableTradeAmount;
    }

    public BigDecimal getOriginalLimit() {
        return originalLimit;
    }

    public BigDecimal getAabPrice() {
        return aabPrice;
    }

    public BigDecimal getAboutPlatformCurrency() {
        if (aabPrice==null || originalLimit==null || availableTradeAmount==null) {
            return BigDecimal.ZERO;
        }
        return aabPrice.multiply(originalLimit.subtract(availableTradeAmount).setScale(2,BigDecimal.ROUND_HALF_UP));
    }

    public MerchantFreezeStockResponse(Long id, BigDecimal availableTradeAmount, BigDecimal originalLimit, BigDecimal aabPrice) {
        this.id = id;
        this.availableTradeAmount = availableTradeAmount;
        this.originalLimit = originalLimit;
        this.aabPrice = aabPrice;
    }
}
