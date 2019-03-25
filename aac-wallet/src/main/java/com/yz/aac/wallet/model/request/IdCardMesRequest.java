package com.yz.aac.wallet.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("实名认证身份信息")
public class IdCardMesRequest {
	
	@ApiModelProperty(value = "身份证", position = 1, required = true)
	private String idcard;
	
	@ApiModelProperty(value = "姓名", position = 2, required = true)
	private String realname;
	
	@ApiModelProperty(value = "性别(1-男，2-女)", position = 3, required = true)
	private Integer gender;
	
	@ApiModelProperty(value = "认证时间", position = 4, required = false)
	private Long realNameCrtTime;
}
