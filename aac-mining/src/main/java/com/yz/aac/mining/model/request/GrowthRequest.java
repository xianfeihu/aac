package com.yz.aac.mining.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("每日增长收集提交请求")
public class GrowthRequest {

    @ApiModelProperty(value = "能量ID", position = 1, required = true)
    private Long growthId;

}
