package com.yz.aac.wallet.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ApiModel("身份证出生年月登录响应")
public class IdCardLoginResponse {

	@ApiModelProperty(value = "登录结果", position = 1)
	private boolean result; 

}
