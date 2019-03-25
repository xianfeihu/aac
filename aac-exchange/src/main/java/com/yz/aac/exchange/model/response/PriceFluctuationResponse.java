package com.yz.aac.exchange.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ApiModel("买卖挂单价格波动信息")
public class PriceFluctuationResponse {

	@ApiModelProperty(value = "收盘价", position = 1)
	private BigDecimal closingPrice;
	
	@ApiModelProperty(value = "浮动率", position = 2)
	private Float floatingRate;
	
	@ApiModelProperty(value = "货币额度", position = 3)
	private BigDecimal currencyLimit;
	
	@ApiModelProperty(value = "最小浮动金额", position = 4)
	private BigDecimal minFloatingAmount;
	
	@ApiModelProperty(value = "最大浮动金额", position = 5)
	private BigDecimal maxFloatingAmount;
	
	@ApiModelProperty(value = "是否是商户<1-是 2-否>", position = 6)
	private Integer isMerchant;
	
	@ApiModelProperty(value = "交易费率", position = 7)
	private BigDecimal tradeChargeRate;
	
	@ApiModelProperty(value = "挂单ID（挂单详情显示）", position = 8)
	private Long orderId;
	
	@ApiModelProperty(value = "单价（挂单详情显示）", position = 9)
	private BigDecimal price;
	
	@ApiModelProperty(value = "挂单额度（挂单详情显示）", position = 10)
	private BigDecimal amount;
	
	@ApiModelProperty(value = "备注（挂单详情显示）", position = 11)
	private String remark;

	public PriceFluctuationResponse(BigDecimal closingPrice,
			Float floatingRate, BigDecimal currencyLimit,
			BigDecimal minFloatingAmount, BigDecimal maxFloatingAmount,
			Integer isMerchant, BigDecimal tradeChargeRate) {
		super();
		this.closingPrice = closingPrice;
		this.floatingRate = floatingRate;
		this.currencyLimit = currencyLimit;
		this.minFloatingAmount = minFloatingAmount;
		this.maxFloatingAmount = maxFloatingAmount;
		this.isMerchant = isMerchant;
		this.tradeChargeRate = tradeChargeRate;
	}
	
}
