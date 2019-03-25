package com.yz.aac.mining.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("点赞请求参数")
public class PacketThumbUpRequest {

    @ApiModelProperty(value = "红包ID", position = 1, required = true)
    private Long packetId;
    
    @ApiModelProperty(value = "评论互动ID<红包点赞忽略，评论点赞必填>", position = 2, required = false)
    private Long interactionId;
    
}
