package com.yz.aac.mining.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("文章点赞请求参数")
public class ArticleThumbUpRequest {

    @ApiModelProperty(value = "文章ID", position = 1, required = true)
    private Long articleId;
    
    @ApiModelProperty(value = "评论互动ID<文章点赞忽略，评论点赞必填>", position = 2, required = false)
    private Long interactionId;
    
}
