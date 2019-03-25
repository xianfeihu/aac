package com.yz.aac.opadmin.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("启用&停用用户请求")
public class UpdateUserStatusRequest {

    @ApiModelProperty(hidden = true)
    private Long userId;

    @ApiModelProperty(value = "状态（1-启用；2-停用）", position = 1, required = true)
    private Integer status;

    @ApiModelProperty(value = "描述信息（status=2时，必须输入）", position = 2)
    private String description;
}
