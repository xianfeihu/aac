package com.yz.aac.mining.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@ApiModel("每日增长能量详情信息")
public class GrowthInfoResponse {

    @ApiModelProperty(value = "ID", position = 1)
    private Long id;
    
    @ApiModelProperty(value = "增长金额", position = 2)
    private BigDecimal growthAmount;
    
}
