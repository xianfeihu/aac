package com.yz.aac.wallet.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRecord {

	/** ID */
	private Long id;
	
	/** 用户ID  */
	private Long userId;
	
	/** 兑换服务ID */
	private Long exchangeId;
	
	/** 充值号码  */
	private String chargingNumber;

	/** 兑换法币金额  */
	private Integer rmbAmount;
	
	/** 平台币付款金额  */
	private BigDecimal platformAmount;
	
	/** 兑换时间 */
	private Long recordTime;
	
	/** 状态（1-待充值 2-充值完成）  */
	private Integer status;

}
