package com.yz.aac.opadmin.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@ApiModel("查询配置响应")
@AllArgsConstructor
public class QueryConfigResponse {

    @ApiModelProperty(value = "大类", position = 1, required = true)
    private Integer category;

    @ApiModelProperty(value = "小类", position = 2, required = true)
    private Integer subCategory;

    @ApiModelProperty(value = "键", position = 3, required = true)
    private String key;

    @ApiModelProperty(value = "值", position = 4, required = true)
    private String value;
}
