package com.yz.aac.opadmin.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("创建题目请求")
public class CreateQuestionRequest {

    @ApiModelProperty(value = "题目名称", position = 1, required = true)
    private String name;

    @ApiModelProperty(value = "所有答案项", position = 2, required = true)
    private List<QuestionAnswer> answers;

    @ApiModelProperty(value = "答对奖励元力值", position = 3, required = true)
    private Integer powerPointBonus;

    @ApiModel("题目答案项")
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class QuestionAnswer {

        @ApiModelProperty(value = "答案名称", position = 1, required = true)
        private String name;

        @ApiModelProperty(value = "是否是正确答案", position = 2, required = true)
        private Boolean correct;

    }
}
