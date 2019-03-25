package com.yz.aac.mining.repository.domian;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLevel {

	/** id */
	private Long id;
	
	/** 等级名称  */
	private String name;
	
	/** 等级图标路径 */
	private String iconPath;
	
	/** 等级需要平台数额  */
	private BigDecimal matchCondition;
	
}
