package com.yz.aac.wallet.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ApiModel("商户押金状态")
public class MerchantDepositStatusResponse {
	
	@ApiModelProperty(value = "是否缴纳押金(1-已发币，等待资格审核 2-资格审核通过，待付押金 3-押金已付，待审核 4-押金审核失败 5-押金审核通过)", position = 11)
	private Integer depositStatus;
	
}
