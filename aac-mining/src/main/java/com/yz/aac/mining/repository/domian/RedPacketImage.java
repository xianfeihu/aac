package com.yz.aac.mining.repository.domian;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedPacketImage {

	/** ID */
	private Long id;
	
	/** 红信ID */
	private Long redPacketId;
	
	/** 图片URL */
    private String imageUrl;
    
    /** 序号 */
    private Integer orderNumber;

	public RedPacketImage(Long redPacketId, String imageUrl, Integer orderNumber) {
		super();
		this.redPacketId = redPacketId;
		this.imageUrl = imageUrl;
		this.orderNumber = orderNumber;
	}
    
}
