package com.yz.aac.mining.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("红信MAP红包详情")
public class PacketMapDateResponse {

	@ApiModelProperty(value = "红包ID", position = 1)
    private Long packetId;
	
	@ApiModelProperty(value = "发布人ID", position = 2)
    private Long issuanceId;
    
    @ApiModelProperty(value = "发布人名称", position = 3)
    private String issuanceName;
    
    @ApiModelProperty(value = "是否可抢（1-是 2-否）", position = 4)
    private Integer isSnatch;
    
    @ApiModelProperty(value = "经度", position = 5)
    private BigDecimal lng;
    
    @ApiModelProperty(value = "纬度", position = 6)
    private BigDecimal lat;

    @ApiModelProperty(value = "当前用户是否领取（1-是 2-否）", position = 7)
    private Integer isReceive;
    
}
