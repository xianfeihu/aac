package com.yz.aac.opadmin.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("更新配置请求")
public class UpdateConfigRequest {

    @ApiModelProperty(value = "大类", position = 1, required = true)
    private Integer category;

    @ApiModelProperty(value = "小类", position = 2, required = true)
    private Integer subCategory;

    @ApiModelProperty(value = "键", position = 3, required = true)
    private String key;

    @ApiModelProperty(value = "值", position = 4, required = true)
    private String value;
}
