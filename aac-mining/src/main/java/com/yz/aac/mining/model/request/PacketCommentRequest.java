package com.yz.aac.mining.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("评论请求参数")
public class PacketCommentRequest {

    @ApiModelProperty(value = "红包ID", position = 1, required = true)
    private Long packetId;
    
    @ApiModelProperty(value = "评论互动ID<回复评论必填>", position = 2, required = false)
    private Long interactionId;
    
    @ApiModelProperty(value = "内容", position = 3, required = true)
    private String description;
}
