package com.yz.aac.opadmin.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("登录请求")
public class LoginRequest {

    @ApiModelProperty(value = "账号", position = 1, required = true)
    private String loginName;

    @ApiModelProperty(value = "密码", position = 2, required = true)
    private String password;
}
