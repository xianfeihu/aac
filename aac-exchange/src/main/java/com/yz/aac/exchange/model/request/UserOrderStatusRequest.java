package com.yz.aac.exchange.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("买卖挂单状态修改数据")
@Data
@NoArgsConstructor
public class UserOrderStatusRequest {

	@ApiModelProperty(value = "挂单ID", position = 1, required = true)
	private Long orderId;
	
	@ApiModelProperty(value = "状态(1-上架 2-下架<默认>)", position = 2, required = false)
	private Integer status;
	
}
