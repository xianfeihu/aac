package com.yz.aac.wallet.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("AAB订单详情数据响应")
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AABOrderInfoResponse {

	@ApiModelProperty(value = "发行方ID", position = 1)
	private Long partnerId;
	
	@ApiModelProperty(value = "发行方名称", position = 2)
	private String partnerName;
	
	@ApiModelProperty(value = "是否支持支付宝 （1-是 2-否）", position = 3)
	private Integer supportAlipay;
	
	@ApiModelProperty(value = "是否支持支付宝 （1-是 2-否）", position = 4)
	private Integer supportWechat;
	
	@ApiModelProperty(value = "是否支持银行卡（1-是 2-否）", position = 5)
	private Integer supportBankCard;
	
	@ApiModelProperty(value = "支付宝账号", position = 6)
	private String alipayAccount;
	
	@ApiModelProperty(value = "支付宝收码附件路径", position = 7)
	private String alipayQrCodePath;
	
	@ApiModelProperty(value = "微信账号", position = 8)
	private String wechatAccount;
	
	@ApiModelProperty(value = "微信收码附件路径", position = 9)
	private String wechatQrCodePath;
	
	@ApiModelProperty(value = "银行卡卡号 ", position = 10)
	private String bankCardNumber;
	
	@ApiModelProperty(value = "交易数量", position = 11)
	private BigDecimal transactionNum;
	
	@ApiModelProperty(value = "单价", position = 12)
	private BigDecimal unitPrice;
	
	@ApiModelProperty(value = "订单时间", position = 13)
	private Long orderTime;
	
	@ApiModelProperty(value = "参考号", position = 14)
	private String referenceNumber;
	
	@ApiModelProperty(value = "购买人ID", position = 15)
	private Long initiatorId;
	
	@ApiModelProperty(value = "购买人名称", position = 16)
	private String initiatorName;
	
	@ApiModelProperty(value = "交易类型 （1-商户发币押金 2-买入 3-转账）", position = 17)
	private Integer tradeType;
	
	@ApiModelProperty(value = "平台币钱包地址", position = 18)
	private String walletAddress;
	
	@ApiModelProperty(value = "挂单ID", position = 19)
	private Long sellingOrderId;

	public AABOrderInfoResponse(Long partnerId, String partnerName,
			Integer supportAlipay, Integer supportWechat,
			Integer supportBankCard, String alipayAccount,
			String alipayQrCodePath, String wechatAccount,
			String wechatQrCodePath, String bankCardNumber,
			Long sellingOrderId) {
		super();
		this.partnerId = partnerId;
		this.partnerName = partnerName;
		this.supportAlipay = supportAlipay;
		this.supportWechat = supportWechat;
		this.supportBankCard = supportBankCard;
		this.alipayAccount = alipayAccount;
		this.alipayQrCodePath = alipayQrCodePath;
		this.wechatAccount = wechatAccount;
		this.wechatQrCodePath = wechatQrCodePath;
		this.bankCardNumber = bankCardNumber;
		this.sellingOrderId = sellingOrderId;
	}
	
	
}
