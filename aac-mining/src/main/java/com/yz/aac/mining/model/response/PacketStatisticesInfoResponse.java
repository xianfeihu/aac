package com.yz.aac.mining.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("红包统计信息")
public class PacketStatisticesInfoResponse {

	@ApiModelProperty(value = "金额", position = 1)
    private BigDecimal amount;
	
	@ApiModelProperty(value = "范围", position = 2)
    private String radius;
    
    @ApiModelProperty(value = "群体（1-男 2-女 null-不限）", position = 3)
    private Integer group;
    
    @ApiModelProperty(value = "消耗时间字符串", position = 4)
    private String elapsedTimeStr;
    
    @ApiModelProperty(value = "领取率", position = 5)
    private Integer takeUpRate;
    
    @ApiModelProperty(value = "曝光率", position = 6)
    private Integer exposureRate;
    
    @ApiModelProperty(value = "点赞率", position = 7)
    private Integer pointPraiseRate;
    
    @ApiModelProperty(value = "评论率", position = 8)
    private Integer commentRate;
    
    @ApiModelProperty(value = "点击率", position = 9)
    private Integer clickThroughRate;
    
    @ApiModelProperty(value = "小贴士", position = 10)
    private String tipsStr;

	public PacketStatisticesInfoResponse(BigDecimal amount, String radius,
			Integer group, String tipsStr) {
		super();
		this.amount = amount;
		this.radius = radius;
		this.group = group;
		this.tipsStr = tipsStr;
	}
    
}
