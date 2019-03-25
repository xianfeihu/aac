package com.yz.aac.wallet.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("用户数据提交请求")
public class DemoRequest {

    @ApiModelProperty(value = "用户ID", position = 1, required = true)
    private Long id;

    @ApiModelProperty(value = "用户名称", position = 2, required = true, allowableValues = ("zhang3,li4,wang5"))
    private String name;

    @ApiModelProperty(value = "用户年龄", position = 3, required = true)
    private Integer age;
}
