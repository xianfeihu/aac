package com.yz.aac.exchange.repository.domian;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAssertFreeze {

    /** ID */
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 货币符号 */
    private String currencySymbol;

    /** 冻结金额)*/
    private BigDecimal amount;

    /** 原因( 1-发币押金 2-挂单购买 3-挂单出售) */
    private Integer reason;

    /** 用户资产冻结时间 */
    private Long actionTime;

	public UserAssertFreeze(Long userId, String currencySymbol,
			BigDecimal amount, Integer reason, Long actionTime) {
		super();
		this.userId = userId;
		this.currencySymbol = currencySymbol;
		this.amount = amount;
		this.reason = reason;
		this.actionTime = actionTime;
	}
    
}
