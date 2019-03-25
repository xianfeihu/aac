package com.yz.aac.exchange.model.response;

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
    private Integer incomePeriod;

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
    private Long issuingDate;

    @ApiModelProperty(value = "App下载连接", position = 10)
    private String appUri;

    public MerchantIssueCurrencyResponse(BigDecimal stoDividendRate, BigDecimal fixedIncomeRate, Integer incomePeriod, Integer restrictionPeriod, String merchantName, BigDecimal total, String whitePaperUrl, String introduction, Long issuingDate) {
        this.stoDividendRate = stoDividendRate;
        this.fixedIncomeRate = fixedIncomeRate;
        this.incomePeriod = incomePeriod;
        this.restrictionPeriod = restrictionPeriod;
        this.merchantName = merchantName;
        this.total = total;
        this.whitePaperUrl = whitePaperUrl;
        this.introduction = introduction;
        this.issuingDate = issuingDate;
    }
}
