package com.yz.aac.wallet.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel("商户注册基本信息")
@Data
public class MerchantRegisterBaseMessageRequest {

    @ApiModelProperty(value = "姓名（没有商户ID时必填，反之不填）", position = 1)
    private String name;

    @ApiModelProperty(value = "商户ID", position = 2)
    private Long merchantId;

    @ApiModelProperty(value = "商户名称", position = 3, required = true)
    private String merchantName;

    @ApiModelProperty(value = "商家二维码连接", position = 4, required = true)
    private String merchantVisitUrl;

    @ApiModelProperty(value = "身份证号码（没有商户ID时必填，反之不填）", position = 5)
    private String idNumber;

    @ApiModelProperty(value = "性别（默认1 ：1、男---2、女）", position = 6)
    private Integer gender;

    @ApiModelProperty(value = "手机号码", position = 7, required = true)
    private Long mobileNumber;

    @ApiModelProperty(value = "验证码", position = 8, required = true)
    private String code;
}
