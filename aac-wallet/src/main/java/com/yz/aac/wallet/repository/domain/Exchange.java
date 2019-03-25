package com.yz.aac.wallet.repository.domain;

import lombok.Data;

@Data
public class Exchange {

	/** ID */
	private Long id;
	
	/** 服务名称 */
	private String name;
	
	/** 大类（1-话费 2-油卡）  */
	private Integer category;

	/** 小类（1-1 移动电话 2-1 中石油 2-2 中石化）  */
	private Integer subCategory;
	
	/** 是否支持自定义金额（1-是 2-否）  */
	private Integer customized;

	/** 每月可兑换次数  */
	private Integer limitInMonth;

	/** 是否开通服务（1-是 2-否）  */
	private Integer activated;

	/** 排序号  */
	private Integer orderNumber;
}
