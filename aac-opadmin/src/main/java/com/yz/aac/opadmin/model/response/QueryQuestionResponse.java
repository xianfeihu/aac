package com.yz.aac.opadmin.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@ApiModel("查询题目响应")
public class QueryQuestionResponse {

    @ApiModelProperty(value = "总数据量", position = 1, required = true)
    private Long totalSize;

    @ApiModelProperty(value = "分页数据明细", position = 2, required = true)
    private List<QueryQuestionResponse.Item> items;

    @ApiModel("查询题目响应分页数据")
    @AllArgsConstructor
    @Data
    public static class Item {

        @ApiModelProperty(value = "编号", position = 1, required = true)
        private Long id;

        @ApiModelProperty(value = "题目名称", position = 2, required = true)
        private String name;

        @ApiModelProperty(value = "所有答案项", position = 3, required = true)
        private List<QueryQuestionResponse.Item.QuestionAnswer> answers;

        @ApiModelProperty(value = "答对奖励元力值", position = 4, required = true)
        private Integer powerPointBonus;

        @ApiModel("题目答案项响应")
        @AllArgsConstructor
        @NoArgsConstructor
        @Data
        public static class QuestionAnswer {

            @ApiModelProperty(value = "答案名称", position = 1, required = true)
            private String name;

            @ApiModelProperty(value = "是否是正确答案（1-是；2-否）", position = 2, required = true)
            private Integer correct;
        }

    }

}
