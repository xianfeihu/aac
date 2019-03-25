package com.yz.aac.mining.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("文章评论信息")
public class ArticleCommentResponse {

	@ApiModelProperty(value = "评论人名称", position = 1)
    private String name;
	
	@ApiModelProperty(value = "评论详情", position = 2)
    private String description;
    
    @ApiModelProperty(value = "评论时间", position = 3)
    private Long actionTime;

    @ApiModelProperty(value = "评论时间字符串", position = 4)
    private String actionTimeStr;

    @ApiModelProperty(value = "点赞数", position = 5)
    private Integer likes;
    
    @ApiModelProperty(value = "是否点赞（针对当前用户：1-是 2-否）", position = 6)
    private Integer isLikes;
    
    @ApiModelProperty(value = "评论ID", position = 7)
    private Long commentId;
    
}
