package com.yz.aac.opadmin.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@ApiModel("查询答题参与用户响应")
public class QueryQuestionParticipatorResponse {

    @ApiModelProperty(value = "总数据量", position = 1, required = true)
    private Long totalSize;

    @ApiModelProperty(value = "分页数据明细", position = 2, required = true)
    private List<QueryQuestionParticipatorResponse.Item> items;

    @ApiModel("查询答题参与用户响应分页数据")
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Item {

        @ApiModelProperty(value = "用户编号", position = 1, required = true)
        private String userCode;

        @ApiModelProperty(value = "姓名", position = 2, required = true)
        private String userName;

        @ApiModelProperty(value = "总答题次数", position = 3, required = true)
        private Integer totalAnswers;

        @ApiModelProperty(value = "答对次数", position = 4, required = true)
        private Integer correctAnswers;

        @ApiModelProperty(value = "答对奖励元力值", position = 5, required = true)
        private Integer powerPointBonus;

    }

}
