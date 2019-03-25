package com.yz.aac.mining.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("链接点击请求参数")
public class PacketLinkClickRequest {

    @ApiModelProperty(value = "红包ID", position = 1, required = true)
    private Long packetId;
}
