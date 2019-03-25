package com.yz.aac.mining.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("发布红信响应信息")
public class PacketInfoResponse {

	@ApiModelProperty(value = "ID", position = 1)
    private Long id;
	
	@ApiModelProperty(value = "描述", position = 2)
    private String description;
    
    @ApiModelProperty(value = "红包个数", position = 3)
    private Integer dividingNumber;
    
    @ApiModelProperty(value = "发布金额", position = 4)
    private BigDecimal dividingAmount;
    
    @ApiModelProperty(value = "经度", position = 5)
    private BigDecimal lng;
    
    @ApiModelProperty(value = "纬度", position = 6)
    private BigDecimal lat;
    
    @ApiModelProperty(value = "地址", position = 7)
    private String location;
    
    @ApiModelProperty(value = "公里范围", position = 8)
    private Integer radius;
    
    @ApiModelProperty(value = "领取限制<1-男 2-女 不传不限制>", position = 9)
    private Integer grabbingLimit;
    
    @ApiModelProperty(value = "链接标题", position = 10)
    private String linkTitle;
    
    @ApiModelProperty(value = "链接地址", position = 11)
    private String linkUrl;
    
    @ApiModelProperty(value = "图片URL", position = 12)
    private List<String> imgs;
	
}
