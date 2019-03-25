package com.yz.aac.mining.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("微信验证码校验提交请求")
public class WechatCheckCodeRequest {

    @ApiModelProperty(value = "验证码", position = 1, required = true)
    private String code;

}
