package com.yz.aac.wallet.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@ApiModel("服务详情")
public class ExchangeServiceInfoResponse {
	
	@ApiModelProperty(value = "类型1-话费 2-油卡", position = 1)
	private Integer category;

	@ApiModelProperty(value = "当前用户手机号码", position = 2)
	private Long phoneNumber;

	@ApiModelProperty(value = "平台币转换率（一个平台币兑换多少法币）", position = 3)
	private BigDecimal plarformCurrencyExchangeRate;

	public ExchangeServiceInfoResponse(Integer category, Long phoneNumber, BigDecimal plarformCurrencyExchangeRate){
		this.category = category;
		this.phoneNumber = phoneNumber;
		this.plarformCurrencyExchangeRate = plarformCurrencyExchangeRate;
	}

	@ApiModelProperty(value = "活动列表", position = 3)
	private List<ExchangeActivityInfoResponse> exchangeActivityInfoResponses;

}
