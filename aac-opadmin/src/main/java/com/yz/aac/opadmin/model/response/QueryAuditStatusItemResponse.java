package com.yz.aac.opadmin.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ApiModel("查询所有审核状态项响应")
public class QueryAuditStatusItemResponse {

    @ApiModelProperty(value = "状态值", position = 1, required = true)
    private Integer status;

    @ApiModelProperty(value = "描述", position = 2, required = true)
    private String description;
}
