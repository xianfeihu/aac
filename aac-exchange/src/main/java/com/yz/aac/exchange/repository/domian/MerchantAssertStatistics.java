package com.yz.aac.exchange.repository.domian;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MerchantAssertStatistics {
    private Long id;
    private Long merchantId;
    private String currencySymbol;
    private String key;
    private BigDecimal value;
}
