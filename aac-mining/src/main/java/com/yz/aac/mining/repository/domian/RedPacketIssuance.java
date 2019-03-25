package com.yz.aac.mining.repository.domian;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedPacketIssuance {

	/** ID */
	private Long id;
	
	/** 发布用户ID */
	private Long issuerId;
	
	/** 描述 */
    private String description;
    
    /** 红包个数 */
    private Integer dividingNumber;
    
    /** 发布金额 */
    private BigDecimal dividingAmount;
    
    /** 经度 */
    private BigDecimal lng;
    
    /** 纬度 */
    private BigDecimal lat;
    
    /** 地址 */
    private String location;
    
    /** 公里范围 */
    private Integer radius;
    
    /** 领取限制<1-男 2-女 不传不限制> */
    private Integer grabbingLimit;
    
    /** 链接标题 */
    private String linkTitle;
    
    /** 链接地址 */
    private String linkUrl;
    
    /** 发布时间 */
    private Long issuanceTime;
	
}
