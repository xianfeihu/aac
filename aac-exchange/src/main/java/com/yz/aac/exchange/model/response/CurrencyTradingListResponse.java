package com.yz.aac.exchange.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonInclude;

@ApiModel("货币买卖信息列表响应数据")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CurrencyTradingListResponse {

	@ApiModelProperty(value = "买家/卖家名称", position = 1)
	private String personName;
	
	@ApiModelProperty(value = "购买/出售数量", position = 2)
	private BigDecimal number;
	
	@ApiModelProperty(value = "单价", position = 3)
	private BigDecimal price;
	
	@ApiModelProperty(value = "挂单ID", position = 4)
	private Long orderId;
	
	@ApiModelProperty(value = "货币符号", position = 5)
	private String currencySymbol;
	
	@ApiModelProperty(value = "买卖类型<1-买 2-卖>", position = 5)
	private Integer type;
	
}
