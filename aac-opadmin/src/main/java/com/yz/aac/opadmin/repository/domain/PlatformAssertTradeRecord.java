package com.yz.aac.opadmin.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlatformAssertTradeRecord {

    private Long id;

    private Long initiatorId;

    private String initiatorName;

    private Integer tradeType;

    private Long tradeTime;

    private Long beginTradeTime;

    private Long endTradeTime;

    private BigDecimal rmbPrice;

    private BigDecimal availableTradeAmount;

    private BigDecimal tradeAmount;

    private BigDecimal minAmountLimit;

    private BigDecimal maxAmountLimit;

    private String walletAddress;

    private BigDecimal balance;

    private Long partnerId;

    private String partnerName;

    private String payNumber;

    private Integer status;

    private Long sellingOrderId;

    private Integer isMerchant;

    private Integer isAdvertiser;

    private List<Integer> tradeTypes;

}
