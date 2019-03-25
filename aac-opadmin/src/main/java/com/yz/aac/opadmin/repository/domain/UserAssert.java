package com.yz.aac.opadmin.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
public class UserAssert {

    private Long id;

    private Long userId;

    private String currencySymbol;

    private BigDecimal balance;

    private BigDecimal historyMaxBalance;

    private String walletAddress;

    private Set<Long> userIds;

    private Integer unRestricted;

    private BigDecimal freezingAmount;

    public UserAssert() {

    }
}
