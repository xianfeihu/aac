package com.yz.aac.wallet.repository.domain;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantAssertIssuance {

	/** ID */
	private Long id;

	/** 商家ID  */
	private Long merchantId;

	/** 货币符号 */
	private String currencySymbol;

	/** 总发行量  */
	private BigDecimal total;

	/** 出售占比  */
	private BigDecimal sellRate;

	/** 挖矿占比  */
	private BigDecimal miningRate;

	/** 固定收益占比 */
	private BigDecimal fixedIncomeRate;

	/** STO分红占比  */
	private BigDecimal stoDividendRate;

	/** 其他模式 */
	private Integer otherMode;

	/** 收益周期(天) */
	private Integer incomePeriod;

	/** 投资限售期(天) */
	private Integer restrictionPeriod;

	/** 简介 */
	private String introduction;

	/** 白皮书URL */
	private String whitePaperUrl;

	/** 发行日期 */
	private Long issuingDate;

	/** 服务费策略ID */
	private Long serviceChargeId;

}
