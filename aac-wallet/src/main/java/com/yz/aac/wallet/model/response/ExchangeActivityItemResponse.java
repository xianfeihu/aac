package com.yz.aac.wallet.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@ApiModel("服务活动项")
public class ExchangeActivityItemResponse {

	@ApiModelProperty(value = "ID", position = 1)
	private Long itemId;

	@ApiModelProperty(value = "兑换法币金额", position = 2)
	private Integer rmbAmount;

	@ApiModelProperty(value = "平台币付款金额", position = 3)
	private BigDecimal platformAmount;

}
