package com.yz.aac.exchange.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MerchantCurrencyStatisticsResponse {

    @ApiModelProperty(value = "总量（总活跃存量、总冻结存量、总挖矿存量）-- 当前的量", position = 1)
    private BigDecimal sum;

    @ApiModelProperty(value = "是否上涨（判断颜色_加减）true涨、false跌", position = 2)
    private Boolean isRise;

    @ApiModelProperty(value = "涨幅(XX.XX %)", position = 3)
    private BigDecimal gain;

    private BigDecimal lastSum;

    public Boolean getIsRise() {
        if (sum==null || lastSum==null) return true;
        return lastSum.compareTo(sum) < 1 ;
    }

    public BigDecimal getGain() {
        if (sum==null || lastSum==null || sum.compareTo(lastSum)==0|| sum.compareTo(BigDecimal.ZERO)==0|| lastSum.compareTo(BigDecimal.ZERO)==0) return BigDecimal.ZERO;
        return getIsRise() ? sum.subtract(lastSum).multiply(BigDecimal.valueOf(100)).divide(lastSum,2,BigDecimal.ROUND_HALF_UP) : lastSum.subtract(sum).multiply(BigDecimal.valueOf(100)).divide(lastSum,2,BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getSum() {
        if (sum==null || sum.compareTo(BigDecimal.ZERO)==0) return BigDecimal.ZERO;
        return sum;
    }
}
