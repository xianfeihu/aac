package com.yz.aac.mining.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ApiModel("关注公众号详情")
public class FocusOnWechatInfoResponse {

    @ApiModelProperty(value = "简介", position = 1)
    private String introductions;

    @ApiModelProperty(value = "二维码图片地址", position = 2)
    private String imgPath;

}
