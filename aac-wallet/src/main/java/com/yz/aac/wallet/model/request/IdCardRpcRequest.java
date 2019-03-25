package com.yz.aac.wallet.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import com.yz.aac.wallet.model.response.IdCardResponse;

@Data
@ApiModel("实名认证第三方数据返回")
public class IdCardRpcRequest {
	
	@ApiModelProperty(value = "描述", position = 1, required = true)
	private String reason;
	
	@ApiModelProperty(value = "状态码", position = 2, required = true)
	private String error_code;
	
	private IdCardResponse result;
	
}
