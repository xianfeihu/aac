package com.yz.aac.exchange.repository.domian;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LockMoneyTransaction {

	/** 用户ID */
	private Long userId;
	
	/** 挂单ID  */
	private Long orderId;
	
	/** 单价 */
	private BigDecimal price;
	
	/** 创建时间  */
	private Long createTime;
	
}
