package com.yz.aac.exchange.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("商户和平台币汇率")
@AllArgsConstructor
public class MerchantAndPlatExchangeRateResponse {

    @ApiModelProperty(value = "平台币汇率（平台币-->法币）", position = 1)
    private BigDecimal platExchangeRate;

    @ApiModelProperty(value = "商户币最近一笔交易汇率（商家币-->平台币）", position = 2)
    private BigDecimal merchantExchangeRate;
}
