package com.yz.aac.mining.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("红信发布历史信息")
public class PacketHistoryResponse {

	@ApiModelProperty(value = "红包ID", position = 1)
    private Long id;
	
	@ApiModelProperty(value = "描述", position = 2)
    private String description;
    
    @ApiModelProperty(value = "领取人数", position = 3)
    private Integer receiveNum;
    
    @ApiModelProperty(value = "评论数", position = 4)
    private Integer commentNum;
    
    @ApiModelProperty(value = "点赞数", position = 5)
    private Integer likeNum;
    
    @ApiModelProperty(value = "头图URL", position = 6)
    private String imageUrl;
    
    @ApiModelProperty(value = "发布人名称", position = 7)
    private String issuerName;
    
}
