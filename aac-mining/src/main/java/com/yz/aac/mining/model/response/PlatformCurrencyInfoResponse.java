package com.yz.aac.mining.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.yz.aac.mining.model.PageResult;

@Data
@AllArgsConstructor
@ApiModel("平台币详情")
public class PlatformCurrencyInfoResponse {

    @ApiModelProperty(value = "总平台币", position = 1)
    private BigDecimal totalPlatformCurrency;

    @ApiModelProperty(value = "今日平台币", position = 2)
    private BigDecimal todayPlatformCurrency;

    @ApiModelProperty(value = "简介", position = 3)
    private String introductions;
    
    @ApiModelProperty(value = "平台币生长记录", position = 4)
    private PageResult<MiningRecordResponse> pageResult;
}
