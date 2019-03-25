package com.yz.aac.exchange.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("用户交易商户币提交信息")
@Data
@NoArgsConstructor
public class UserOrderSubRequest {

	@ApiModelProperty(value = "购买数量", position = 1, required = true)
	private BigDecimal transactionNum;
	
	@ApiModelProperty(value = "挂单ID", position = 2, required = true)
	private Long orderId;
	
	@ApiModelProperty(value = "支付密码", position = 3, required = true)
	private String payPassword;
	
}
