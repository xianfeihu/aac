package com.yz.aac.mining.repository.domian;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedPacketDividingGrabbing {

	/** ID */
	private Long id;
	
	/** 红信ID */
	private Long redPacketId;
	
	/** 红信子项ID */
	private Long dividingId;
	
	/** 入手用户ID */
	private Long grabberId;
	
	/** 入手时间 */
	private Long grabbingTime;
    
}
