package com.yz.aac.exchange.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("AAB购买提交信息")
@Data
@NoArgsConstructor
public class PlatformCurrencyOrderSubRequest {

	@ApiModelProperty(value = "购买数量", position = 1, required = true)
	private BigDecimal transactionNum;
	
	@ApiModelProperty(value = "挂单ID", position = 2, required = true)
	private Long orderId;
	
	@ApiModelProperty(value = "参考号", position = 3, required = true)
	private String referenceNumber;
	
	@ApiModelProperty(value = "订单时间", position = 4, required = false)
	private Long orderTime;
	
}
