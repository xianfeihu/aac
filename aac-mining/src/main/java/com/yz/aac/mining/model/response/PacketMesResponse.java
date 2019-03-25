package com.yz.aac.mining.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("红包领取基本信息")
public class PacketMesResponse {

	@ApiModelProperty(value = "红包ID", position = 1)
    private Long packetId;
	
    @ApiModelProperty(value = "发布人Id", position = 2)
    private Long issuerId;
    
    @ApiModelProperty(value = "发布人名称", position = 3)
    private String issuerName;
    
    @ApiModelProperty(value = "红包领取状态（1-已领取 2-未领取，红包派发进行中 3-未领取，红包派发结束）<正对当前用户>", position = 4)
    private Integer receivingStatus;
    
    @ApiModelProperty(value = "领取/剩余金额", position = 5)
    private BigDecimal amount;
    
    @ApiModelProperty(value = "红包状态（1-派发中 2-领取完毕）", position = 6)
    private Integer packetStatus;
    
    @ApiModelProperty(value = "点赞数", position = 7)
    private Integer praisePoints;
    
    @ApiModelProperty(value = "描述", position = 8)
    private String description;
    
    @ApiModelProperty(value = "链接标题", position = 9)
    private String linkTitle;
    
    @ApiModelProperty(value = "链接地址", position = 10)
    private String linkUrl;
    
    @ApiModelProperty(value = "领取人列表", position = 11)
    private List<String> grabberList;
    
    @ApiModelProperty(value = "图片列表", position = 12)
    private List<String> imgList;
    
    @ApiModelProperty(value = "评论列表", position = 13)
    private List<PacketCommentResponse> commentList;
    
    @ApiModelProperty(value = "是否点赞（针对当前用户：1-是 2-否）", position = 14)
    private Integer isLikes;
    
	public PacketMesResponse(Long packetId, Long issuerId, String issuerName,
			Integer packetStatus, Integer praisePoints, String description, 
			String linkTitle, String linkUrl) {
		super();
		this.issuerId = issuerId;
		this.packetId = packetId;
		this.issuerName = issuerName;
		this.packetStatus = packetStatus;
		this.praisePoints = praisePoints;
		this.description = description;
		this.linkTitle = linkTitle;
		this.linkUrl = linkUrl;
	}
	
}
