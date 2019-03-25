package com.yz.aac.exchange.model.response;

import static com.yz.aac.common.Constants.Misc.PLATFORM_CURRENCY_EXCHANGE_RATE;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CurrencyMallIndexInfoResponse {

    private String currencySymbol;

    private Integer tradVolume;

    private BigDecimal yesterdayPlatformPrice;

    private BigDecimal lastPlatformPrice;

    private BigDecimal aboutRmb;

    private Boolean isRise;

    private BigDecimal gain;

    @ApiModelProperty(value = "货币符号", position = 1)
    public String getCurrencySymbol() {
        return currencySymbol;
    }

    @ApiModelProperty(value = "截至昨日该时刻交易量", position = 2)
    public Integer getTradVolume() {
        return tradVolume;
    }

    @ApiModelProperty(value = "最后一单平台币价格", position = 3)
    public BigDecimal getLastPlatformPrice() {
        return lastPlatformPrice;
    }

    @ApiModelProperty(value = "换算成人名币", position = 4)
    public BigDecimal getAboutRmb() {
        if (lastPlatformPrice==null) return null;
        return lastPlatformPrice.multiply(new BigDecimal(PLATFORM_CURRENCY_EXCHANGE_RATE.value())).setScale(2,BigDecimal.ROUND_HALF_UP);
    }

    @ApiModelProperty(value = "是否上涨（判断颜色_加减）true涨、false跌", position = 5)
    public Boolean getIsRise() {
        if (lastPlatformPrice==null || yesterdayPlatformPrice==null) return true;
        return yesterdayPlatformPrice.compareTo(lastPlatformPrice) < 1 ;
    }

    @ApiModelProperty(value = "涨幅(XX.XX %)", position = 6)
    public BigDecimal getGain() {
        if (yesterdayPlatformPrice==null || lastPlatformPrice==null) return BigDecimal.ZERO;
        return (lastPlatformPrice.subtract(yesterdayPlatformPrice)).multiply(BigDecimal.valueOf(100)).divide(yesterdayPlatformPrice,2,BigDecimal.ROUND_HALF_UP);
//        if (lastPlatformPrice==null || yesterdayPlatformPrice==null || lastPlatformPrice.compareTo(yesterdayPlatformPrice)==0) return BigDecimal.ZERO;
//        return getIsRise() ? lastPlatformPrice.subtract(yesterdayPlatformPrice).multiply(BigDecimal.valueOf(100)).divide(yesterdayPlatformPrice,2,BigDecimal.ROUND_HALF_UP) : yesterdayPlatformPrice.subtract(lastPlatformPrice).multiply(BigDecimal.valueOf(100)).divide(yesterdayPlatformPrice,2,BigDecimal.ROUND_HALF_UP);
    }

    public CurrencyMallIndexInfoResponse(String currencySymbol, Integer tradVolume, BigDecimal yesterdayPlatformPrice, BigDecimal lastPlatformPrice) {
        this.currencySymbol = currencySymbol;
        this.tradVolume = tradVolume;
        this.yesterdayPlatformPrice = yesterdayPlatformPrice;
        this.lastPlatformPrice = lastPlatformPrice;
    }
}
