package com.yz.aac.opadmin.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MerchantAssertTradeRecord {

    private Long id;

    private Long initiatorId;

    private String initiatorName;

    private Integer tradeType;

    private Long tradeTime;

    private Long beginTradeTime;

    private Long endTradeTime;

    private String currencySymbol;

    private BigDecimal platformPrice;

    private BigDecimal tradeAmount;

    private BigDecimal balance;

    private Long partnerId;

    private String partnerName;

    private String tradeResult;

    private Integer isMerchant;

    private Integer isAdvertiser;

    private String platformCurrencySymbol;

    private String walletAddress;


}
