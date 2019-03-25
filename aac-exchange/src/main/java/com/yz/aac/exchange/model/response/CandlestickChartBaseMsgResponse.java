package com.yz.aac.exchange.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Setter;

import java.math.BigDecimal;

import static com.yz.aac.common.Constants.Misc.PLATFORM_CURRENCY_EXCHANGE_RATE;

@Setter
@ApiModel("该商户币最新的相关数据")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CandlestickChartBaseMsgResponse {

    @ApiModelProperty(value = "货币符号", position = 1)
    private String currencySymbol;

    @ApiModelProperty(value = "最高价", position = 2)
    private BigDecimal maxPrice;

    @ApiModelProperty(value = "最低价", position = 3)
    private BigDecimal minPrice;

    @ApiModelProperty(value = "近期价格（收盘价、币单价）", position = 4)
    private BigDecimal recentPrice;

    @ApiModelProperty(value = "开盘价", position = 5)
    private BigDecimal openPrice;

    @ApiModelProperty(value = "昨日收盘价", position = 6)
    private BigDecimal yesterdayClosePrice;

    @ApiModelProperty(value = "昨日APP端发行的币量", position = 7)
    private BigDecimal platformTradNum;

    @ApiModelProperty(value = "昨日小程序端发行的币量", position = 8)
    private BigDecimal appletTradNum;

    @ApiModelProperty(value = "该商户币总发行量", position = 9)
    private BigDecimal currencySum;

    @ApiModelProperty(value = "昨日成交总笔数", position = 10)
    private Integer currencyNum;

    @ApiModelProperty(value = "当前统计类型（1、日---2、周---3、月）", position = 11,hidden = true)
    private Integer countType;

    @ApiModelProperty(value = "等值RMB", position = 12)
    private BigDecimal aboutRmb;

    @ApiModelProperty(value = "创建时间（统计日期）", position = 13, hidden = true)
    private Long createTime;

    @ApiModelProperty(value = "是否上涨（判断颜色_加减）", position = 14)
    private Boolean isRise;

    @ApiModelProperty(value = "涨幅(XX.XX %)", position = 15)
    private BigDecimal gain;

    @ApiModelProperty(value = "总市值", position = 16)
    public BigDecimal getMarketValue(){
        if ( currencySum==null || recentPrice== null ) return BigDecimal.ZERO;
        return currencySum.multiply(recentPrice).setScale(2,BigDecimal.ROUND_HALF_UP);
    }

    @ApiModelProperty(value = "市场流通量", position = 18)
    private BigDecimal liquidity;

    @ApiModelProperty(value = "昨日总发行币量", position = 19)
    public BigDecimal getAllTradNum() {
        return platformTradNum.add(appletTradNum);
    }

    public BigDecimal getLiquidity() {
        return liquidity;
    }

    public CandlestickChartBaseMsgResponse() {
    }

    public CandlestickChartBaseMsgResponse(String currencySymbol, BigDecimal maxPrice, BigDecimal minPrice, BigDecimal recentPrice, BigDecimal openPrice, BigDecimal platformTradNum, BigDecimal appletTradNum, Integer currencyNum, Integer countType, Long createTime, BigDecimal yesterdayClosePrice, BigDecimal currencySum) {
        this.currencySymbol = currencySymbol;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.recentPrice = recentPrice;
        this.openPrice = openPrice;
        this.platformTradNum = platformTradNum;
        this.appletTradNum = appletTradNum;
        this.currencyNum = currencyNum;
        this.countType = countType;
        this.createTime = createTime;
        this.yesterdayClosePrice = yesterdayClosePrice;
        this.currencySum = currencySum;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice == null ? BigDecimal.valueOf(-1) : maxPrice;
    }

    public BigDecimal getMinPrice() {
        return minPrice == null ? BigDecimal.valueOf(-1) : minPrice;
    }

    public BigDecimal getRecentPrice() {
        return recentPrice==null ? BigDecimal.valueOf(-1) : recentPrice.setScale(2,BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getOpenPrice() {
        return openPrice == null ? BigDecimal.valueOf(-1) : openPrice;
    }

    public BigDecimal getPlatformTradNum() {
        return platformTradNum;
    }

    public BigDecimal getAppletTradNum() {
        return appletTradNum;
    }

    public Integer getCurrencyNum() {
        return currencyNum;
    }

    public BigDecimal getAboutRmb() {
        if(recentPrice==null) return BigDecimal.ZERO;
        return recentPrice.multiply(new BigDecimal(PLATFORM_CURRENCY_EXCHANGE_RATE.value())).setScale(2,BigDecimal.ROUND_HALF_UP);
    }

    public Long getCreateTime() {
        return createTime;
    }

    public Boolean getIsRise() {
        if (openPrice==null || recentPrice==null) return true;
        return openPrice.compareTo(recentPrice) < 1;
    }

    public BigDecimal getGain() {
        if (openPrice==null || recentPrice==null) return BigDecimal.ZERO;
        return (recentPrice.subtract(openPrice)).multiply(BigDecimal.valueOf(100)).divide(openPrice,2,BigDecimal.ROUND_HALF_UP);
//        if (recentPrice==null || yesterdayClosePrice==null || yesterdayClosePrice.compareTo(recentPrice)==0) return BigDecimal.ZERO;
//        return getIsRise() ? recentPrice.subtract(yesterdayClosePrice).multiply(BigDecimal.valueOf(100)).divide(yesterdayClosePrice,2,BigDecimal.ROUND_HALF_UP) : yesterdayClosePrice.subtract(recentPrice).multiply(BigDecimal.valueOf(100)).divide(yesterdayClosePrice,2,BigDecimal.ROUND_HALF_UP);
    }

}
