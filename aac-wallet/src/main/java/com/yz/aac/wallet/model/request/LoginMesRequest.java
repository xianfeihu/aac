package com.yz.aac.wallet.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("用户登录信息")
public class LoginMesRequest {

	@ApiModelProperty(value = "手机号", position = 1, required = true)
	private String mobile;
	
	@ApiModelProperty(value = "验证码", position = 2, required = true)
	private String code;
	
	@ApiModelProperty(value = "邀请码", position = 3, required = false)
	private String inviterCode;
	
}
