package com.yz.aac.opadmin.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("创建账号请求")
public class CreateAccountRequest {

    @ApiModelProperty(value = "账号", position = 1, required = true)
    private String loginName;

    @ApiModelProperty(value = "密码", position = 2, required = true)
    private String password;

    @ApiModelProperty(value = "姓名", position = 3, required = true)
    private String name;

    @ApiModelProperty(value = "部门", position = 4, required = true)
    private String department;
}
