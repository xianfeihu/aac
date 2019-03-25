package com.yz.aac.mining.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("签到响应数据")
public class SignInPointResponse {
	
	@ApiModelProperty(value = "奖励详情字符串", position = 1)
    private String rewardStr;

    @ApiModelProperty(value = "签到详情字符串", position = 2)
    private String signInPointStr;
	
}
