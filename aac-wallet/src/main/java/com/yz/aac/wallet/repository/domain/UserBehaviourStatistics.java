package com.yz.aac.wallet.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserBehaviourStatistics {
    private Long id;
    private Long userId;
    private String key;
    private Integer value;
    
	public UserBehaviourStatistics(Long userId, String key, Integer value) {
		super();
		this.userId = userId;
		this.key = key;
		this.value = value;
	}
    
}
