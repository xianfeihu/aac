package com.yz.aac.opadmin.repository.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@ApiModel("商户分红信息表")
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MerchantDividendRecord {
    private Long id;
    private Long merchantId;
    private Long dividendDate;
    private Long dividendIssueDate;
    private BigDecimal profitAmount;
    private BigDecimal dividendAmount;
    private Integer status;
}