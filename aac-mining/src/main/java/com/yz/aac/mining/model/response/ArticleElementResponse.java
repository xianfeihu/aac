package com.yz.aac.mining.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ApiModel("文章元素")
public class ArticleElementResponse {

    @ApiModelProperty(value = "元素类型（1-文字 2-视频 3-图片）", position = 1)
    private Integer elementType;
    
    @ApiModelProperty(value = "元素内容", position = 2)
    private String elementContent;

    @ApiModelProperty(value = "元素排序号", position = 3)
    private Integer orderNumber;

}
