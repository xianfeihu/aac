package com.yz.aac.mining.repository.domian;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserNaturalGrowthRecord {

	/** id */
	private Long id;
	
	/** 用户ID  */
	private Long userId;
	
	/** 增长金额 */
	private BigDecimal growthAmount;
	
	/** 创建时间  */
	private Long createTime;

	public UserNaturalGrowthRecord(Long userId, BigDecimal growthAmount,
			Long createTime) {
		super();
		this.userId = userId;
		this.growthAmount = growthAmount;
		this.createTime = createTime;
	}
	
}
