package com.yz.aac.wallet.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


@ApiModel("商户货币发行请求数据")
@Data
public class MerchantAssertIssuanceMessageRequest {

    @ApiModelProperty(value = "商户ID", position = 1, required = true)
    private Long merchantId;

    @ApiModelProperty(value = "货币符号", position = 2, required = true)
    private String currencySymbol;

    @ApiModelProperty(value = "总发行量", position = 3, required = true)
    private BigDecimal total;

    @ApiModelProperty(value = "挖矿占比", position = 4, required = true)
    private BigDecimal miningRate;

    @ApiModelProperty(value = "固定收益占比", position = 5, required = true)
    private BigDecimal fixedIncomeRate;

    @ApiModelProperty(value = "STO分红占比", position = 6, required = true)
    private BigDecimal stoDividendRate;

    @ApiModelProperty(value = "其他模式 (1、是 2、否)", position = 6, required = true)
    private Integer otherMode;

    @ApiModelProperty(value = "收益周期(天)", position = 7, required = true)
    private Integer incomePeriod;

    @ApiModelProperty(value = "投资限售期(天)", position = 8, required = true)
    private Integer restrictionPeriod;

    @ApiModelProperty(value = "简介", position = 9, required = true)
    private String introduction;

    @ApiModelProperty(value = "白皮书URL", position = 10, required = true)
    private String whitePaperUrl;
}
