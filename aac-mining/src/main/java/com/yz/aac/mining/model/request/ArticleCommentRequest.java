package com.yz.aac.mining.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("评论提交请求参数")
public class ArticleCommentRequest {

    @ApiModelProperty(value = "文章ID", position = 1, required = true)
    private Long articleId;
    
    @ApiModelProperty(value = "内容", position = 2, required = true)
    private String description;
}
