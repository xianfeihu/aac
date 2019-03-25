package com.yz.aac.opadmin.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class UserAssertFreeze {

    private Long id;

    private Long userId;

    private String currencySymbol;

    private BigDecimal amount;

    private Integer reason;

    private Long actionTime;

    public UserAssertFreeze() {

    }
}
