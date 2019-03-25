package com.yz.aac.mining.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ApiModel("获取文章预览基本元素请求响应对象")
public class ArticleBaseElementsResponse {

    @ApiModelProperty(value = "作者", position = 1)
    private String author;
    
    @ApiModelProperty(value = "文章标签", position = 2)
    private String articleLabel;

}
