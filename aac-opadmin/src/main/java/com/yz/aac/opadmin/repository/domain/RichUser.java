package com.yz.aac.opadmin.repository.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RichUser {

    private Long userId;

    private Integer status;

    private String userName;

    private String walletAddress;

    private BigDecimal balance;

    private BigDecimal freezingAmount;

    private String levelName;

    private Long powerPoint;

    private Long tradeCount;

    private Long increaseAlgorithmId;

    private BigDecimal rechargeIncome;

    private BigDecimal sellIncome;

    private BigDecimal transferIncome;

    private BigDecimal miningIncome;

    private Long mobileNumber;

    private String idNumber;

    private Integer gender;

    private String statusDescription;

    //以下为查询条件

    private BigDecimal currentLevelIncome;

    private BigDecimal nextLevelIncome;

    private String currencySymbol;

    private String tradeCountKey;

    private String incomeRechargeKey;

    private String incomeSellKey;

    private String incomeTransferKey;

    private String incomeMiningKey;

    private Long beginRegTime;

    private Long endRegTime;

    private BigDecimal minBalance;

    private BigDecimal maxBalance;

    private Integer isMerchant;

    private Integer isAdvertiser;

}
