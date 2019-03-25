package com.yz.aac.exchange.repository.domian;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRole {

	/** ID */
	private Long id;
	
	/** 用户ID  */
	private Long userId;
	
	/** 是否是商户(1-是 2-否) */
	private Integer isMerchant;
	
	/** 是否是广告主(1-是 2-否)  */
	private Integer isAdvertiser;
	
	public UserRole(Long userId, Integer isMerchant, Integer isAdvertiser) {
		super();
		this.userId = userId;
		this.isMerchant = isMerchant;
		this.isAdvertiser = isAdvertiser;
	}
	
}
