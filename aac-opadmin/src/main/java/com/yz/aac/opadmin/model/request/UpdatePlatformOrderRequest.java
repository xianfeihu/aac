package com.yz.aac.opadmin.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("更新平台币卖单请求")
public class UpdatePlatformOrderRequest {

    @ApiModelProperty(hidden = true)
    private Long id;

    @ApiModelProperty(value = "限价", position = 1, required = true)
    private BigDecimal rmbPrice;

    @ApiModelProperty(value = "最小限购额", position = 2, required = true)
    private BigDecimal minAmountLimit;

    @ApiModelProperty(value = "最大限购额", position = 3, required = true)
    private BigDecimal maxAmountLimit;

    @ApiModelProperty(value = "数量", position = 4, required = true)
    private BigDecimal availableTradeAmount;

    @ApiModelProperty(value = "备注", position = 5)
    private String remark;

    @ApiModelProperty(value = "挂单人ID", position = 6, required = true)
    private Long sellerId;
}
