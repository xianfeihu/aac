package com.yz.aac.mining.repository.domian;

import lombok.Data;

@Data
public class MiningAnswer {

	/** ID */
	private Long id;
	
	/** 题目ID */
	private Long questionId;
	
	/** 名称 */
	private String name;
	
	/** 答案序号 */
	private Integer orderNumber;
	
	/** 是否是正确答案  */
	private Integer isCorrect;

}
