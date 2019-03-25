package com.yz.aac.opadmin.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("创建文章分类请求")
public class CreateArticleCategoryRequest {

    @ApiModelProperty(value = "分类名称", position = 1, required = true)
    private String name;
}
