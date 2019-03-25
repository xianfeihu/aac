package com.yz.aac.exchange.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;

@Setter
@NoArgsConstructor
@ApiModel("币简介")
public class CurrencyIntroductionResponse {

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

    @ApiModelProperty(value = "市场流通量", position = 7)
    private BigDecimal liquidity;

    @ApiModelProperty(value = "白皮书URL", position = 8)
    private String whitePaperUrl;

    @ApiModelProperty(value = "简介", position = 9)
    private String introduction;

    @ApiModelProperty(value = "发行日期", position = 10)
    private Long issuingDate;

    @ApiModelProperty(value = "K线图地址", position = 11)
    private String kChartPath;

    public BigDecimal getStoDividendRate() {
        return stoDividendRate;
    }

    public BigDecimal getFixedIncomeRate() {
        return fixedIncomeRate;
    }

    public Long getIncomePeriod() {
        if (issuingDate==null) return incomePeriod;
        return incomePeriod * DateUtils.MILLIS_PER_DAY + issuingDate;
    }

    public Integer getRestrictionPeriod() {
        return restrictionPeriod;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public BigDecimal getLiquidity() {
        return liquidity == null ? BigDecimal.ZERO : liquidity ;
    }

    public String getWhitePaperUrl() {
        return whitePaperUrl;
    }

    public String getIntroduction() {
        return introduction;
    }

    public Long getIssuingDate() {
        return issuingDate;
    }

    public String getkChartPath() {
        return kChartPath;
    }

    public CurrencyIntroductionResponse(BigDecimal stoDividendRate, BigDecimal fixedIncomeRate, Long incomePeriod, Integer restrictionPeriod, String merchantName, BigDecimal total, BigDecimal liquidity, String whitePaperUrl, String introduction, Long issuingDate) {
        this.stoDividendRate = stoDividendRate;
        this.fixedIncomeRate = fixedIncomeRate;
        this.incomePeriod = incomePeriod;
        this.restrictionPeriod = restrictionPeriod;
        this.merchantName = merchantName;
        this.total = total;
        this.liquidity = liquidity;
        this.whitePaperUrl = whitePaperUrl;
        this.introduction = introduction;
        this.issuingDate = issuingDate;
    }
}
