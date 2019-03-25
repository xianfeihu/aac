package com.yz.aac.exchange.repository.domian;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPaymentErrorRecord {

	/** 用户ID  */
	private Long userId;
	
	/** 是否是商户(1-是 2-否) */
	private Long createTime;
	
}
