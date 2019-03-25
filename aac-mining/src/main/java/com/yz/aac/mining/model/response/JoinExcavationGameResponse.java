package com.yz.aac.mining.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@ApiModel("加入挖矿游戏响应对象")
public class JoinExcavationGameResponse {

    @ApiModelProperty(value = "itemId，用户参与的挖矿项ID", position = 1)
    private Long itemId;

    @ApiModelProperty(value = "levelId，该场次进入的用户等级-ID", position = 2)
    private Long levelId;

    @ApiModelProperty(value = "activityBeginTime，距离活动开始还有X毫秒" , position = 3)
    private Long activityBeginTime;

    @ApiModelProperty(value = "activityProcessingTime，活动进行时间（毫秒）" , position = 4)
    private Long activityProcessingTime;

    @ApiModelProperty(value = "ajaxBeginTime，距离（获取本场次最大挖矿量）还有X毫秒", position = 5)
    private Long ajaxBeginTime;

    @ApiModelProperty(value = "暴击率（百分号之前的值）", position = 6)
    private BigDecimal luckyRate;

    @ApiModelProperty(value = "暴击倍数", position = 7)
    private Integer luckyTimes;

    @ApiModelProperty(value = "点击第几次出现广告", position = 8)
    private Integer hitAdNumber;

}
