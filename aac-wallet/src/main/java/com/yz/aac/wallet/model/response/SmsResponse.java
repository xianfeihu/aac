package com.yz.aac.wallet.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel("短信验证码返回结果")
@AllArgsConstructor
@NoArgsConstructor
public class SmsResponse {

	@ApiModelProperty(value = "状态码", position = 1, required = true)
	private int code;
	
	@ApiModelProperty(value = "发送计次", position = 2, required = true)
	private String msg;
	
	@ApiModelProperty(value = "验证码", position = 3, required = true)
	private String obj;

}
