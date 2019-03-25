package com.yz.aac.mining.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("首页挖矿游戏类型数据")
public class HomeTagResponse {
	
	@ApiModelProperty(value = "标题", position = 1)
    private String title;

    @ApiModelProperty(value = "图标显示类型字符串", position = 2)
    private String typeStr;
    
    @ApiModelProperty(value = "百分比", position = 3)
    private Integer percentage;

    @ApiModelProperty(value = "摘要", position = 4)
    private String synopsis;
    
    @ApiModelProperty(value = "按钮文本", position = 5)
    private String buttonText;
    
    @ApiModelProperty(value = "按钮是否显示（1-是 2-否）", position = 6)
    private Integer buttonIsShow;
    
    @ApiModelProperty(value = "类型(1-答题 2-关注公众号 3-签到 4-实名认证)", position = 7)
    private Integer type;

	public HomeTagResponse(Integer type,String title, String typeStr, Integer percentage,
			String buttonText) {
		super();
		this.type = type;
		this.title = title;
		this.typeStr = typeStr;
		this.percentage = percentage;
		this.buttonText = buttonText;
	}
    
}
