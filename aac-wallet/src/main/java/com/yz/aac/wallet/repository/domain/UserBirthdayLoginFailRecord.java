package com.yz.aac.wallet.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBirthdayLoginFailRecord {

	/** 用户ID */
	private Long userId;
	
	/** 记录时间  */
	private Long recordTime;
	
}
