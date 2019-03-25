package com.yz.aac.mining.repository.domian;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BriefText {

	/** key  */
	private String key;
	
	/** 简介名称 */
	private String name;
	
	/** 简介内容  */
	private String content;
	
}
