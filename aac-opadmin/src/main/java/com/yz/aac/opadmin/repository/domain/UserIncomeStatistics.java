package com.yz.aac.opadmin.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class UserIncomeStatistics {

    private Long id;

    private Long userId;

    private String currencySymbol;

    private String key;

    private BigDecimal value;

    public UserIncomeStatistics() {

    }
}
