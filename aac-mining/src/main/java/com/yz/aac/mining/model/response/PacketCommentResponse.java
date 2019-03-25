package com.yz.aac.mining.model.response;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("红信评论信息")
public class PacketCommentResponse {

	@ApiModelProperty(value = "评论人名称", position = 1)
    private String name;
	
	@ApiModelProperty(value = "评论详情", position = 2)
    private String description;
    
    @ApiModelProperty(value = "评论时间", position = 3)
    private Long actionTime;
    
    @ApiModelProperty(value = "点赞数", position = 4)
    private Integer likes;
    
    @ApiModelProperty(value = "是否点赞（针对当前用户：1-是 2-否）", position = 5)
    private Integer isLikes;
    
    @ApiModelProperty(value = "评论ID", position = 6)
    private Long commentId;
    
    @ApiModelProperty(value = "回复列表", position = 7)
    private List<PacketCommentResponse> replyList;
    
}
