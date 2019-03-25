package com.yz.aac.wallet.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("登录信息")
public class ExternalRegisterUserRequest {

    @ApiModelProperty(value = "商户ID", position = 1, required = true)
    private Long merchantId;

    @ApiModelProperty(value = "手机号", position = 2, required = true)
    private Long mobileNumber;

    @ApiModelProperty(value = "姓名", position = 3, required = true)
    private String name;

    @ApiModelProperty(value = "性别(1-男，2-女)", position = 4, required = true)
    private Integer gender;

    @ApiModelProperty(value = "身份证号码", position = 5, required = true)
    private String idNumber;
}
