package com.yz.aac.mining.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("挖矿奖励请求数据")
public class MerchantCurrencyExchangeRequest {

    @ApiModelProperty(value = "用户ID", position = 1)
    private Long userId;

    @ApiModelProperty(value = "商户ID", position = 2, required = true)
    private Long merchantId;

    @ApiModelProperty(value = "挖矿获得的商户币金额", position = 3, required = true)
    private BigDecimal amount;

}
