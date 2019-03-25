package com.yz.aac.opadmin.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ApiModel("查询商户所有已发行的币种响应")
public class QueryMerchantCurrencyResponse {

    @ApiModelProperty(value = "货币ID", position = 1, required = true)
    private Long id;

    @ApiModelProperty(value = "货币符号", position = 2, required = true)
    private String symbol;

    @ApiModelProperty(value = "商户名称", position = 3, required = true)
    private String merchantName;
}