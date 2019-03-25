package com.yz.aac.mining.repository.domian;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PlatformAssertStatistics {

	/** ID */
	private Long id;
	
	/** key  */
	private String key;
	
	/** value */
	private BigDecimal value;

}
