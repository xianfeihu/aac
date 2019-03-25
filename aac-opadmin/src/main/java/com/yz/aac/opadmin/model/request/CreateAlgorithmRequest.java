package com.yz.aac.opadmin.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("创建算法请求")
public class CreateAlgorithmRequest {

    @ApiModelProperty(value = "算法名称", position = 1, required = true)
    private String name;

    @ApiModelProperty(value = "昨日增长元力（基数）", position = 2, required = true)
    private Integer increasedPowerPoint;

    @ApiModelProperty(value = "昨日广告有效点击率或提交信息总数量（基数）", position = 3, required = true)
    private Integer consumedAd;

    @ApiModelProperty(value = "昨日AAC数量（基数）", position = 4, required = true)
    private Double platformCurrency;
}
