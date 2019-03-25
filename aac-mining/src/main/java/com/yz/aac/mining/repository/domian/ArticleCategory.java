package com.yz.aac.mining.repository.domian;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("文章类型")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleCategory {

    @ApiModelProperty(value = "类型ID", position = 1)
    private Long id;

    @ApiModelProperty(value = "类型名称", position = 2)
    private String name;

    @ApiModelProperty(value = "添加时间", position = 3)
    private Long createTime;

}
