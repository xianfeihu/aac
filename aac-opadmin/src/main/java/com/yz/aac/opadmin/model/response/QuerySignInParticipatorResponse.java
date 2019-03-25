package com.yz.aac.opadmin.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@ApiModel("查询签到参与用户响应")
public class QuerySignInParticipatorResponse {

    @ApiModelProperty(value = "总数据量", position = 1, required = true)
    private Long totalSize;

    @ApiModelProperty(value = "分页数据明细", position = 2, required = true)
    private List<QuerySignInParticipatorResponse.Item> items;

    @ApiModel("查询签到参与用户响应分页数据")
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Item {

        @ApiModelProperty(value = "用户编号", position = 1, required = true)
        private String userCode;

        @ApiModelProperty(value = "姓名", position = 2, required = true)
        private String userName;

        @ApiModelProperty(value = "认证时间", position = 3, required = true)
        private Long crtTime;

        @ApiModelProperty(value = "总签到次数", position = 4, required = true)
        private Integer totalSignIn;

        @ApiModelProperty(value = "签到奖励元力值", position = 5, required = true)
        private Integer powerPointBonus;

    }

}
