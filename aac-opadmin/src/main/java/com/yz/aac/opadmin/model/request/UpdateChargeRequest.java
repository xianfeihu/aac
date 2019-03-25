package com.yz.aac.opadmin.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("更新收费策略请求")
public class UpdateChargeRequest {

    @ApiModelProperty(hidden = true)
    private Long id;

    @ApiModelProperty(value = "配置名称", position = 1, required = true)
    private String name;

    @ApiModelProperty(value = "交易费率", position = 2, required = true)
    private Double tradeChargeRate;

    @ApiModelProperty(value = "发币押金", position = 3, required = true)
    private Double issuanceDeposit;
}
