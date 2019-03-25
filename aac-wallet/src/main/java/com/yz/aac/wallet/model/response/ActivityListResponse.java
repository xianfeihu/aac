package com.yz.aac.wallet.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ApiModel("兑换服务列表")
public class ActivityListResponse {
	
	@ApiModelProperty(value = "类型1-话费 2-油卡", position = 1)
	private Integer category;

	@ApiModelProperty(value = "服务名称", position = 2)
	private String name;

}
