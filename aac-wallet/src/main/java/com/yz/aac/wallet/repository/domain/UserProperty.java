package com.yz.aac.wallet.repository.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserProperty {

	/** ID */
	private Long id;
	
	/** 用户ID  */
	private Long userId;
	
	/** 元力值 */
	private Integer powerPoint;
	
	/** 自然增长策略ID  */
	private Long increaseStrategyId;
	
	/** 状态(1-启用 2-停用) */
	private Integer status;
	
	/** 状态描述  */
	private String statusDescription;

	public UserProperty(Long userId, Integer powerPoint,
			Long increaseStrategyId, Integer status) {
		super();
		this.userId = userId;
		this.powerPoint = powerPoint;
		this.increaseStrategyId = increaseStrategyId;
		this.status = status;
	}
	
	
	
}
