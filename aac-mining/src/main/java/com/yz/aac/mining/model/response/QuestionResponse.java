package com.yz.aac.mining.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@ApiModel("题目信息")
public class QuestionResponse {

    @ApiModelProperty(value = "名称", position = 1)
    private String name;
    
    @ApiModelProperty(value = "题目序号", position = 2)
    private Integer serialNumber;
    
    @ApiModelProperty(value = "奖励值", position = 3)
    private Integer rewardVal;
    
    @ApiModelProperty(value = "答案选项列表", position = 4)
    private List<AnswerResponse> answerList;
    
}
