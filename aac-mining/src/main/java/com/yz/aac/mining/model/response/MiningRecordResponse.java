package com.yz.aac.mining.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ApiModel("挖矿记录")
public class MiningRecordResponse {

	@ApiModelProperty(value = "挖矿类型挖矿类字符串", position = 1)
	private String action;
	
	@ApiModelProperty(value = "挖矿时间 ", position = 2)
	private Long actionTime;
	
	@ApiModelProperty(value = "奖励", position = 3)
	private BigDecimal bonus;
	
	@ApiModelProperty(value = "奖励类型（1-平台币 2-元力）", position = 4)
	private Integer bonusType;
	
}
