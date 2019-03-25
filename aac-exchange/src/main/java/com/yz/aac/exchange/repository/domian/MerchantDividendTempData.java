package com.yz.aac.exchange.repository.domian;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantDividendTempData {
	private Long merchantUserId;
	private Long userId;
	private BigDecimal amount;
}
