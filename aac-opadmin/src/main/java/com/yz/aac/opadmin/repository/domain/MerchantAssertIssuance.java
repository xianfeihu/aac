package com.yz.aac.opadmin.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MerchantAssertIssuance {

    private Long id;

    private Long increaseStrategyId;

    private Long merchantId;

    private String currencySymbol;

    private BigDecimal total;

    private Double sellRate;

    private Double miningRate;

    private Double fixedIncomeRate;

    private Double stoDividendRate;

    private Integer otherMode;

    private Integer incomePeriod;

    private Integer restrictionPeriod;

    private String introduction;

    private String whitePaperUrl;

    private Long issuingDate;

    private Long serviceChargeId;

    private String merchantName;

    private String name;

    private Integer status;

    private String statusDescription;

    private Long mobileNumber;

    private String idNumber;

    private Long requestTime;

    private String auditComment;

    private String platformCurrencySymbol;

    private BigDecimal balance;

    private BigDecimal freezingAmount;

    private Long requestId;

    private Long beginTime;

    private Long endTime;

    private BigDecimal rmbPrice;

    private BigDecimal sellRest;

    private BigDecimal miningRest;

    private BigDecimal sellSold;

    private BigDecimal miningMind;

    private Integer tradeCount;

    private Long userId;

    private BigDecimal issuanceFreezingAmount;

    private BigDecimal issuanceDeposit;

}
