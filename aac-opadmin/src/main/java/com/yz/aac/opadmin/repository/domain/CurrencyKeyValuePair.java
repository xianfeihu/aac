package com.yz.aac.opadmin.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyKeyValuePair {

    private Long id;

    private String key;

    private BigDecimal value;

    public CurrencyKeyValuePair(String key, BigDecimal value) {
        this.key = key;
        this.value = value;
    }

}
