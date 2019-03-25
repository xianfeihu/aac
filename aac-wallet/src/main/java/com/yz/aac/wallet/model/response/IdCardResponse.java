package com.yz.aac.wallet.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("用户身份证实名验证返回结果")
public class IdCardResponse {

	@ApiModelProperty(value = "真实姓名", position = 1, required = true)
	private String realname;
	
	@ApiModelProperty(value = "身份证号码", position = 2, required = true)
	private String idcard;
	
	@ApiModelProperty(value = "匹配详情,1匹配,2不匹配", position = 3, required = true)
	private int res;

}
