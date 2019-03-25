package com.yz.aac.mining.repository.domian;

import lombok.Data;

@Data
public class MiningQuestion {

	/** ID */
	private Long id;
	
	/** 是否是单选题（1-是 2-否）  */
	private Integer isSingleChoice;
	
	/** 名称 */
	private String name;
	
	/** 元力值  */
	private Integer powerPointBonus;

}
