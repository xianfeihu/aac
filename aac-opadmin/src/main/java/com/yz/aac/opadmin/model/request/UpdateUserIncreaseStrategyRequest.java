package com.yz.aac.opadmin.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("更新用户自然增长策略请求")
public class UpdateUserIncreaseStrategyRequest {

    @ApiModelProperty(hidden = true)
    private Long userId;

    @ApiModelProperty(value = "自然增长策略ID", position = 1, required = true)
    private Long strategyId;

}
