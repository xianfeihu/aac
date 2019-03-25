package com.yz.aac.exchange.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("买卖单提交数据")
@Data
@NoArgsConstructor
public class SalesOrderRequest {

	@ApiModelProperty(value = "出售额度", position = 1, required = true)
	private BigDecimal availableTradeAmount;
	
	@ApiModelProperty(value = "货币符号", position = 2, required = true)
	private String currencySymbol;
	
	@ApiModelProperty(value = "单价", position = 3, required = true)
	private BigDecimal price;
	
	@ApiModelProperty(value = "备注", position = 4, required = false)
	private String remark;
	
	@ApiModelProperty(value = "发布类型(1-买单<默认> 2-卖单)", position = 5, required = false)
	private Integer type;
	
	@ApiModelProperty(value = "挂单ID", position = 6, required = false)
	private Long orderId;
	
	@ApiModelProperty(value = "支付密码", position = 7, required = true)
	private String payPassword;
	
}
