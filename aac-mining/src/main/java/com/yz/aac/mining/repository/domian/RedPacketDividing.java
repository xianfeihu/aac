package com.yz.aac.mining.repository.domian;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedPacketDividing {

	/** ID */
	private Long id;
	
	/** 红信ID */
	private Long redPacketId;
	
	/** 金额 */
    private BigDecimal dividingAmount;

	public RedPacketDividing(Long redPacketId, BigDecimal dividingAmount) {
		super();
		this.redPacketId = redPacketId;
		this.dividingAmount = dividingAmount;
	}
    
}
