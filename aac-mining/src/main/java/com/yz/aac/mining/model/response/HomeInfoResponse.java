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
@ApiModel("首页响应数据")
public class HomeInfoResponse {
	
	@ApiModelProperty(value = "等级名称", position = 1)
    private String levelName;

    @ApiModelProperty(value = "等级图标路径", position = 2)
    private String levelIconPath;
    
    @ApiModelProperty(value = "总平台币", position = 3)
    private BigDecimal totalPlatformCurrency;

    @ApiModelProperty(value = "总元力币", position = 4)
    private Integer totalPowerPoint;
    
    @ApiModelProperty(value = "今日平台币", position = 5)
    private BigDecimal toDayPlatformCurrency;
	
	@ApiModelProperty(value = "每日增长列表", position = 6)
	private List<GrowthInfoResponse> growthList;
	
	@ApiModelProperty(value = "游戏tags", position = 7)
	private List<HomeTagResponse> tags;
	
}
