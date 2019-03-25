package com.yz.aac.wallet.repository.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MerchantAssertIssuanceAudit {

	@ApiModelProperty(value = "ID", position = 1)
	private Long id;
	
	@ApiModelProperty(value = "商家发币信息ID", position = 2)
	private Long issuanceId;
	
	@ApiModelProperty(value = "状态：1-已发币，资格资格审核 2-资格审核通过，待付押金 3-押金已付，待审核 4-押金审核失败 5-押金审核通过 6-发币审核失败", position = 3)
	private Integer status;
	
	@ApiModelProperty(value = "审批备注", position = 4)
	private String auditComment;
	
	@ApiModelProperty(value = "审批时间", position = 5)
	private Long requestTime;
	
	@ApiModelProperty(value = "审核时间", position = 6)
	private Long auditTime;

	public MerchantAssertIssuanceAudit(Integer status, String auditComment, Long requestTime, Long auditTime) {
		this.status = status;
		this.auditComment = auditComment;
		this.requestTime = requestTime;
		this.auditTime = auditTime;
	}
}
