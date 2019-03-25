package com.yz.aac.wallet.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("商户发行货币简介")
public class MerchantIssueCurrencyResponse {

    @ApiModelProperty(value = "STO分红占比", position = 1)
    private BigDecimal stoDividendRate;

    @ApiModelProperty(value = "固定收益占比", position = 2)
    private BigDecimal fixedIncomeRate;

    @ApiModelProperty(value = "收益周期(天)", position = 3)
    private Long incomePeriod;

    @ApiModelProperty(value = "投资限售期(天)", position = 4)
    private Integer restrictionPeriod;

    @ApiModelProperty(value = "商户名称", position = 5)
    private String merchantName;

    @ApiModelProperty(value = "总发行量", position = 6)
    private BigDecimal total;

    @ApiModelProperty(value = "白皮书URL", position = 8)
    private String whitePaperUrl;

    @ApiModelProperty(value = "简介", position = 9)
    private String introduction;

    @ApiModelProperty(value = "发行日期", position = 10)
    private Long auditTime;

    @ApiModelProperty(value = "App下载连接", position = 11)
    private String appUri;

    @ApiModelProperty(value = "市场流通量", position = 12)
    private BigDecimal liquidity;

    public MerchantIssueCurrencyResponse(BigDecimal stoDividendRate, BigDecimal fixedIncomeRate, Long incomePeriod, Integer restrictionPeriod, String merchantName, BigDecimal total, String whitePaperUrl, String introduction, Long auditTime) {
        this.stoDividendRate = stoDividendRate;
        this.fixedIncomeRate = fixedIncomeRate;
        this.incomePeriod = incomePeriod;
        this.restrictionPeriod = restrictionPeriod;
        this.merchantName = merchantName;
        this.total = total;
        this.whitePaperUrl = whitePaperUrl;
        this.introduction = introduction;
        this.auditTime = auditTime;
    }
}
