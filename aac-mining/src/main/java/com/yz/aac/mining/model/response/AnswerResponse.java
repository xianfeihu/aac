package com.yz.aac.mining.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ApiModel("答案信息")
public class AnswerResponse {

    @ApiModelProperty(value = "答案ID", position = 1)
    private Long answerId;
    
    @ApiModelProperty(value = "题目ID", position = 2)
    private Long questionId;
    
    @ApiModelProperty(value = "答案序号", position = 3)
    private Integer orderNumber;
    
    @ApiModelProperty(value = "名称", position = 4)
    private String name;
    
    @ApiModelProperty(value = "是否是正确答案（1-是 2-否）", position = 5)
    private Integer isCorrect;
    
}
