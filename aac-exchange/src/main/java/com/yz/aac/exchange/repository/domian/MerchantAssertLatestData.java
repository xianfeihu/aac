package com.yz.aac.exchange.repository.domian;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MerchantAssertLatestData {

    private Long id;

    @ApiModelProperty(value = "货币符号", position = 1)
    private String currencySymbol;

    @ApiModelProperty(value = "最大单价", position = 2)
    private BigDecimal maxPrice;

    @ApiModelProperty(value = "最小单价", position = 3)
    private BigDecimal minPrice;

    @ApiModelProperty(value = "近期价格（收盘价）", position = 4)
    private BigDecimal recentPrice;

    @ApiModelProperty(value = "开盘价", position = 5)
    private BigDecimal openPrice;

    @ApiModelProperty(value = "昨日收盘价", position = 6)
    private BigDecimal yesterdayClosePrice;

    @ApiModelProperty(value = "APP端成交的币数", position = 7)
    private BigDecimal platformTradNum;

    @ApiModelProperty(value = "小程序端成交的币数", position = 8)
    private BigDecimal appletTradNum;

    @ApiModelProperty(value = "成交总笔数", position = 9)
    private Integer currencyNum;

    @ApiModelProperty(value = "当前统计类型（1、日---2、周---3、月）", position = 10)
    private Integer countType;

    @ApiModelProperty(value = "创建时间（统计日期）", position = 11)
    private Long createTime;
}
