package com.yz.aac.opadmin.repository.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("查询文章分类响应")
public class ArticleCategory {

    @ApiModelProperty(value = "ID", position = 1, required = true)
    private Long id;

    @ApiModelProperty(value = "名称", position = 2, required = true)
    private String name;

    private String accurateName;

    private Long createTime;

    private Integer readonly;

}
