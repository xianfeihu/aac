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
@ApiModel("等级信息")
public class LevelInfoResponse {

	@ApiModelProperty(value = "用户平台币", position = 1)
    private BigDecimal platformCurrency;

    @ApiModelProperty(value = "等级名称", position = 2)
    private String levelName;

    @ApiModelProperty(value = "等级图标路径", position = 3)
    private String levelIconPath;
	
	@ApiModelProperty(value = "等级平台币数额", position = 4)
	private BigDecimal levelMatchCondition;
	
	@ApiModelProperty(value = "等级列表", position = 5)
	private List<LevelInfoResponse> levelList;

	public LevelInfoResponse(String levelName, String levelIconPath,
			BigDecimal levelMatchCondition) {
		super();
		this.levelName = levelName;
		this.levelIconPath = levelIconPath;
		this.levelMatchCondition = levelMatchCondition;
	}
	
}
