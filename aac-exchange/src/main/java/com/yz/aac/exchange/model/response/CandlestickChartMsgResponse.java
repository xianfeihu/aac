package com.yz.aac.exchange.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("该商户币交易(日、周、月)相关数据")
public class CandlestickChartMsgResponse {

    @ApiModelProperty(value = "创建时间（统计日期）", position = 1)
    private Long createTime;

    @ApiModelProperty(value = "开盘价", position = 2)
    private BigDecimal openPrice;

    @ApiModelProperty(value = "近期价格（收盘价）", position = 3)
    private BigDecimal recentPrice;

    @ApiModelProperty(value = "最小单价", position = 4)
    private BigDecimal minPrice;

    @ApiModelProperty(value = "最大单价", position = 5)
    private BigDecimal maxPrice;

    @JsonIgnore
    @ApiModelProperty(value = "APP端成交的币数", hidden = true)
    private BigDecimal platformTradNum;

    @JsonIgnore
    @ApiModelProperty(value = "小程序端成交的币数", hidden = true)
    private BigDecimal appletTradNum;

    @ApiModelProperty(value = "成交总笔数", position = 6)
    private Integer currencyNum;

    @ApiModelProperty(value = "成交总币数", position = 7)
    public BigDecimal getTotalNumber() {
        return platformTradNum.add(appletTradNum).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    @ApiModelProperty(value = "涨幅", position = 8)
    public BigDecimal getGain() {
        if (recentPrice==null || openPrice==null || recentPrice.compareTo(openPrice)==0) return BigDecimal.ZERO;
        return recentPrice.subtract(openPrice).multiply(BigDecimal.valueOf(100)).divide(openPrice, BigDecimal.ROUND_HALF_UP);
    }

}
