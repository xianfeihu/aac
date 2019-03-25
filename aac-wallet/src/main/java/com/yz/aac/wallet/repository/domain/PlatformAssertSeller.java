package com.yz.aac.wallet.repository.domain;

import lombok.Data;

@Data
public class PlatformAssertSeller {

	/** ID */
	private Long id;
	
	/** 挂单人名称  */
	private String name;
	
	/** 是否支持支付宝 （1-是 2-否） */
	private Integer supportAlipay;
	
	/** 是否支持微信 （1-是 2-否）  */
	private Integer supportWechat;
	
	/** 是否支持银行卡（1-是 2-否）  */
	private Integer supportBankCard;
	
	/** 支付宝账号  */
	private String alipayAccount;
	
	/** 支付宝收码附件路径 */
	private String alipayQrCodePath;
	
	/** 微信账号 */
	private String wechatAccount;
	
	/** 微信收码附件路径 */
	private String wechatQrCodePath;
	
	/** 银行卡卡号 */
	private String bankCardNumber;
	
	/** 创建时间 */
	private Long createTime;
	
	/** 最近更新时间 */
	private Long updateTime;
	
}
