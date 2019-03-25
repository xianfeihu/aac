package com.yz.aac.mining.repository.domian;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserChallengeQuestionsRecord {

	/** 用户ID */
	private Long userId;
	
	/** 答题挑战次数  */
	private Integer frequency;
	
	/** 作答次数 */
	private Integer answerNumber;
	
	/** 当前一轮挑战累计奖励值 */
	private Integer powerPointBonus;
	
	/** 挑战时间  */
	private Long createTime;

	/** 答对次数 */
	private Integer answerTimes;
	
}
