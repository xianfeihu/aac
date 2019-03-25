package com.yz.aac.mining.repository.domian;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMiningRecord {

	/** ID */
	private Long id;
	
	/** 用户ID */
	private Long userId;
	
	/** 邀请人ID */
	private Long inviterId;
	
	/** 挖矿类型挖矿类型（1-阅读 2-实名认证 3-签到 4-关注公众号 5-邀请好友 6-红信 7-答题） */
	private Integer action;
	
	/** 挖矿时间  */
	private Long actionTime;
	
	/** 奖励  */
	private BigDecimal bonus;
	
	/** 奖励类型（1-平台币 2-元力） */
	private Integer bonusType;

	public UserMiningRecord(Long userId, Long inviterId, Integer action,
			Long actionTime, BigDecimal bonus, Integer bonusType) {
		super();
		this.userId = userId;
		this.inviterId = inviterId;
		this.action = action;
		this.actionTime = actionTime;
		this.bonus = bonus;
		this.bonusType = bonusType;
	}
	
}
