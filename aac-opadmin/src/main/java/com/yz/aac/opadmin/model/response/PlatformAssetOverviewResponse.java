package com.yz.aac.opadmin.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@ApiModel("查询平台资产总览响应")
public class PlatformAssetOverviewResponse {

    @ApiModelProperty(value = "平台币符号", position = 1, required = true)
    private String platformCurrencySymbol;

    @ApiModelProperty(value = "更新时间", position = 2, required = true)
    private Long updateTime;

    @ApiModelProperty(value = "资产总量", position = 3, required = true)
    private BigDecimal total;

    @ApiModelProperty(value = "活跃总量", position = 4, required = true)
    private BigDecimal active;

    @ApiModelProperty(value = "活跃自然增长量", position = 5, required = true)
    private BigDecimal activeIncrease;

    @ApiModelProperty(value = "活跃自然增长被挖量", position = 6, required = true)
    private BigDecimal activeIncreaseMinded;

    @ApiModelProperty(value = "活跃自然增长剩余量", position = 7, required = true)
    private BigDecimal activeIncreaseRest;

    @ApiModelProperty(value = "活跃挖矿量", position = 8, required = true)
    private BigDecimal activeMining;

    @ApiModelProperty(value = "活跃挖矿被挖量", position = 9, required = true)
    private BigDecimal activeMiningMined;

    @ApiModelProperty(value = "活跃挖矿剩余量", position = 10, required = true)
    private BigDecimal activeMiningRest;

    @ApiModelProperty(value = "固定总量", position = 11, required = true)
    private BigDecimal fixed;

    @ApiModelProperty(value = "固定已售出量", position = 12, required = true)
    private BigDecimal fixedSold;

    @ApiModelProperty(value = "固定剩余量", position = 13, required = true)
    private BigDecimal fixedRest;

}
