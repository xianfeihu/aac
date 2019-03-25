package com.yz.aac.mining.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import com.yz.aac.mining.model.PageResult;

@Data
@AllArgsConstructor
@ApiModel("元力详情")
public class PowerPointInfoResponse {

    @ApiModelProperty(value = "总元力", position = 1)
    private Integer totalPowerPoint;

    @ApiModelProperty(value = "今日元力", position = 2)
    private Integer todayPowerPoint;

    @ApiModelProperty(value = "简介", position = 3)
    private String introductions;
    
    @ApiModelProperty(value = "元力生长记录", position = 4)
    private PageResult<MiningRecordResponse> pageResult;
}
