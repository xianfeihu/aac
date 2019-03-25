package com.yz.aac.wallet.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("AAB购买订单提交数据")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AABOrderInfoRequest {

	@ApiModelProperty(value = "购买方ID", position = 1, required = true)
	private Long partnerId;
	
	@ApiModelProperty(value = "购买方名称", position = 2, required = true)
	private String partnerName;
	
	@ApiModelProperty(value = "交易数量", position = 3, required = true)
	private BigDecimal transactionNum;
	
	@ApiModelProperty(value = "单价", position = 4, required = true)
	private BigDecimal unitPrice;
	
	@ApiModelProperty(value = "订单时间", position = 5)
	private Long orderTime;
	
	@ApiModelProperty(value = "参考号", position = 6, required = true)
	private String referenceNumber;
	
	@ApiModelProperty(value = "购买人ID", position = 7, required = true)
	private Long initiatorId;
	
	@ApiModelProperty(value = "购买人名称", position = 8, required = true)
	private String initiatorName;
	
	@ApiModelProperty(value = "交易类型 （1-商户发币押金 2-买入 3-转账）", position = 9, required = true)
	private Integer tradeType;
	
	@ApiModelProperty(value = "平台币钱包地址", position = 10, required = true)
	private String walletAddress;
	
	@ApiModelProperty(value = "挂单ID", position = 11, required = true)
	private Long sellingOrderId;
	
}
