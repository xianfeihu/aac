package com.yz.aac.mining.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("提交答题请求对象")
public class SubmitAnswerRequest {

    @ApiModelProperty(value = "题目ID", position = 1, required = true)
    private Long questionId;
    
    @ApiModelProperty(value = "答案ID", position = 2, required = true)
    private Long answerId;

}
