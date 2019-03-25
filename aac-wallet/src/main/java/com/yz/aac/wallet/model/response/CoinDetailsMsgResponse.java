package com.yz.aac.wallet.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yz.aac.common.util.EnumUtil;
import com.yz.aac.wallet.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Setter;

import java.math.BigDecimal;

import static com.yz.aac.common.Constants.Misc.PLATFORM_CURRENCY_EXCHANGE_RATE;
import static com.yz.aac.common.Constants.Misc.PLATFORM_CURRENCY_SYMBOL;
import static com.yz.aac.wallet.Constants.Other.FREEZE;

@AllArgsConstructor
@Setter
@ApiModel("币资产详情")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoinDetailsMsgResponse {

    @ApiModelProperty(value = "币的类型", position = 1, required = true)
    private String currencySymbol;
    @ApiModelProperty(value = "币的数量", position = 2, required = true)
    private BigDecimal amount;
    @ApiModelProperty(value = "当前币种汇率", position = 3, required = true)
    private BigDecimal platformPrice;
    private Long reason;
    @ApiModelProperty(value = "冻结币原因 1: 发币押金 2: 挂单购买 3: 挂单出售", position = 4)
    private StringBuilder freezeReason;

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getPlatformPrice() {
        return platformPrice;
    }

    @JsonIgnore
    public StringBuilder getFreezeReasonBuilder() {
        return freezeReason;
    }

    public String getFreezeReason() {
        if (reason!=null) {
            freezeReason.deleteCharAt(freezeReason.length()-2);
            return freezeReason.toString();
        }
        return null;
    }

    public StringBuilder getReason(StringBuilder stb) {
        stb.append(EnumUtil.getByCode(Constants.FreezeReasonType.class,reason.intValue()).des() +": "+ FREEZE.value() + amount + currencySymbol + ", ");
        return stb;
    }

    @ApiModelProperty(value = "获取当前币约为多少平台币", position = 6)
    public BigDecimal getAboutAab(){
        if (currencySymbol.equals(PLATFORM_CURRENCY_SYMBOL.value()))
            return amount.multiply(BigDecimal.valueOf(Long.valueOf(PLATFORM_CURRENCY_EXCHANGE_RATE.value()))).setScale(2, BigDecimal.ROUND_HALF_UP);
        if (amount!=null && platformPrice!=null)
            return amount.multiply(platformPrice).setScale(2, BigDecimal.ROUND_HALF_UP);
        return BigDecimal.ZERO;
    }

    public CoinDetailsMsgResponse(String currencySymbol, BigDecimal amount, Long reason, BigDecimal platformPrice) {
        this.currencySymbol = currencySymbol;
        this.amount = amount;
        this.reason = reason;
        this.platformPrice = platformPrice;
    }

    public CoinDetailsMsgResponse(String currencySymbol, BigDecimal amount, BigDecimal platformPrice) {
        this.currencySymbol = currencySymbol;
        this.amount = amount;
        this.platformPrice = platformPrice;
    }

    public CoinDetailsMsgResponse(String currencySymbol, BigDecimal amount) {
        this.currencySymbol = currencySymbol;
        this.amount = amount;
    }

}
