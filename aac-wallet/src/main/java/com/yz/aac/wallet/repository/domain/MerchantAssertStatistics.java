package com.yz.aac.wallet.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class MerchantAssertStatistics {
    private Long id;
    private Long merchantId;
    private String currencySymbol;
    private String key;
    private BigDecimal value;
}
