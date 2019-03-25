package com.yz.aac.mining.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ApiModel("答题首页信息")
public class AnswerGameResponse {

    @ApiModelProperty(value = "用户当日剩余挑战次数", position = 1)
    private Integer todayFrequency;
    
    @ApiModelProperty(value = "用户当日答题获得总奖励值", position = 2)
    private Integer totalRewardVal;
    
    @ApiModelProperty(value = "活动规则", position = 3)
    private String activityRules;

}
