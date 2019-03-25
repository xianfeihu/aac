package com.yz.aac.exchange.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonInclude;

@ApiModel("货币买卖挂单详情响应数据")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CurrencyTradingInfoResponse {

	@ApiModelProperty(value = "挂单ID", position = 1)
	private Long orderId;
	
	@ApiModelProperty(value = "挂单人ID", position = 2)
	private Long sellerId;
	
	@ApiModelProperty(value = "挂单人姓名", position = 3)
	private String sellerName;
	
	@ApiModelProperty(value = "可出售总额", position = 4)
	private BigDecimal availableTradeAmount;
	
	@ApiModelProperty(value = "单笔最小金额", position = 5)
	private BigDecimal minAmountLimit;
	
	@ApiModelProperty(value = "单笔最大限额", position = 6)
	private BigDecimal maxAmountLimit;
	
	@ApiModelProperty(value = "单价", position = 7)
	private BigDecimal price;
	
	@ApiModelProperty(value = "备注", position = 8)
	private String remark;
	
	@ApiModelProperty(value = "货币符号", position = 9)
	private String currencySymbol;
	
	@ApiModelProperty(value = "最大买卖额度", position = 10)
	private BigDecimal maxBalance;
	
	@ApiModelProperty(value = "买卖类型（1-买 2-卖）", position = 11)
	private Integer type;
	
	@ApiModelProperty(value = "最大平台币额度", position = 12)
	private BigDecimal maxPlatformCurrencyBalance;
	
	@ApiModelProperty(value = "是否是商家挂单<1-是 2-否>", position = 13)
	private Integer isMerchant;
	
	@ApiModelProperty(value = "交易费率（不含%）", position = 14)
	private BigDecimal tradeChargeRate;

	public CurrencyTradingInfoResponse(Long orderId, Long sellerId, String sellerName,
			BigDecimal availableTradeAmount, BigDecimal price, String remark,
			String currencySymbol, Integer type) {
		super();
		this.orderId = orderId;
		this.sellerId = sellerId;
		this.sellerName = sellerName;
		this.availableTradeAmount = availableTradeAmount;
		this.price = price;
		this.remark = remark;
		this.currencySymbol = currencySymbol;
		this.type = type;
	}
	
}
