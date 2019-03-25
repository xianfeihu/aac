package com.yz.aac.wallet.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MobileVerifyEntityRequest {

    @ApiModelProperty(value = "电话号码", position = 1, required = true)
    private String mobile;

    @ApiModelProperty(value = "验证码", position = 2, required = true)
    private String code;
    
    @ApiModelProperty(value = "验证码类型<1-登录（默认）  2-修改密码  3-商户实名认证>", position = 3, required = true)
    private Integer type;
    
}
