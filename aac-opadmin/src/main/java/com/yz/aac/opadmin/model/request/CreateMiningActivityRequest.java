package com.yz.aac.opadmin.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel("创建挖矿活动请求")
public class CreateMiningActivityRequest {

    @ApiModelProperty(value = "所有活动", position = 1, required = true)
    private List<Activity> activities;

    @ApiModel("挖矿活动场次项")
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Activity {

        @ApiModelProperty(value = "开始时间", position = 1, required = true)
        private Long beginTime;

        @ApiModelProperty(value = "结束时间", position = 2, required = true)
        private Long endTime;

        @ApiModelProperty(value = "所有场次", position = 3, required = true)
        private List<CreateMiningActivityRequest.Activity.Session> sessions;

        @ApiModel("挖矿活动入口项")
        @AllArgsConstructor
        @NoArgsConstructor
        @Data
        public static class Session {

            @ApiModelProperty(value = "用户级别ID（为null时，视为不限等级）", position = 1)
            private Long userLevelId;

            @ApiModelProperty(value = "总奖励", position = 2, required = true)
            private BigDecimal totalBonus;

            @ApiModelProperty(value = "暴击率（百分号之前的值）", position = 3, required = true)
            private Integer luckyRate;

            @ApiModelProperty(value = "暴击率倍数", position = 4, required = true)
            private Integer luckyTimes;

            @ApiModelProperty(value = "点击第几次出现广告", position = 5, required = true)
            private Integer hitAdNumber;
        }
    }
}
