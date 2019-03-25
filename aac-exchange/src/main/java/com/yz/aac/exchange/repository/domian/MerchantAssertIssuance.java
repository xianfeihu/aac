package com.yz.aac.exchange.repository.domian;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MerchantAssertIssuance {

	 @ApiModelProperty(value = "ID", position = 1)
	 private Long id;

	 @ApiModelProperty(value = "商家ID", position = 1)
	 private Long merchantId;

	 @ApiModelProperty(value = "货币符号", position = 1)
	 private String currencySymbol;

	 @ApiModelProperty(value = "总发行量", position = 1)
	 private BigDecimal total;

	 @ApiModelProperty(value = "出售占比", position = 1)
	 private BigDecimal sellRate;

	 @ApiModelProperty(value = "挖矿占比", position = 1)
	 private BigDecimal miningRate;

	 @ApiModelProperty(value = "固定收益占比", position = 1)
	 private BigDecimal fixedIncomeRate;

	 @ApiModelProperty(value = "STO分红占比", position = 1)
	 private BigDecimal stoDividendRate;

	 @ApiModelProperty(value = "其他模式", position = 1)
	 private Integer otherMode;

	 @ApiModelProperty(value = "收益周期(天)", position = 1)
	 private Integer incomePeriod;

	 @ApiModelProperty(value = "投资限售期(天)", position = 1)
	 private Integer restrictionPeriod;

	 @ApiModelProperty(value = "简介", position = 1)
	 private String introduction;

	 @ApiModelProperty(value = "白皮书URL", position = 1)
	 private String whitePaperUrl;

	 @ApiModelProperty(value = "发行日期", position = 1)
	 private Long issuingDate;

	 @ApiModelProperty(value = "服务费策略ID", position = 1)
	 private Long serviceChargeId;
	
}
