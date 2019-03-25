package com.yz.aac.mining.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("红包图片信息")
public class PacketImgResponse {

	@ApiModelProperty(value = "红包图片ID", position = 1)
    private Long imgId;
	
    @ApiModelProperty(value = "红包图片URL", position = 2)
    private String imgPath;
    
}
