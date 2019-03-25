package com.yz.aac.wallet.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
@ApiModel("用户数据响应")
public class DemoResponse {

    @ApiModelProperty(value = "用户ID", position = 1, required = true)
    private Long id;

    @ApiModelProperty(value = "用户名称", position = 2, required = true)
    private String name;

    @ApiModelProperty(value = "用户年龄", position = 3, required = true)
    private Integer age;
}
