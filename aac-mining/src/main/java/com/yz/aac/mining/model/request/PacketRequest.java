package com.yz.aac.mining.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
@ApiModel("红信发布基本信息")
public class PacketRequest {

    @ApiModelProperty(value = "描述", position = 1, required = false)
    private String description;
    
    @ApiModelProperty(value = "红包个数", position = 2, required = true)
    private Integer dividingNumber;
    
    @ApiModelProperty(value = "发布金额", position = 3, required = true)
    private BigDecimal dividingAmount;
    
    @ApiModelProperty(value = "经度（小数点后六位）", position = 4, required = true)
    private BigDecimal lng;
    
    @ApiModelProperty(value = "纬度（小数点后六位）", position = 5, required = true)
    private BigDecimal lat;
    
    @ApiModelProperty(value = "地址", position = 6, required = false)
    private String location;
    
    @ApiModelProperty(value = "公里范围", position = 7, required = true)
    private Integer radius;
    
    @ApiModelProperty(value = "领取限制<1-男 2-女 不传不限制>", position = 8, required = false)
    private Integer grabbingLimit;
    
    @ApiModelProperty(value = "链接标题", position = 9, required = false)
    private String linkTitle;
    
    @ApiModelProperty(value = "链接地址", position = 10, required = false)
    private String linkUrl;
    
    @ApiModelProperty(value = "图片ID<导入红包使用>", position = 11, required = false)
    private List<Long> imgIdList;
    
    @ApiModelProperty(value = "支付密码", position = 12, required = true)
    private String payPasword;
    
}
