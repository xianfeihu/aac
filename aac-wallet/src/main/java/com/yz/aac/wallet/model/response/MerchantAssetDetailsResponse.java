package com.yz.aac.wallet.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("商户资产类别")
public class MerchantAssetDetailsResponse {

    @ApiModelProperty(value = "可出售量", position = 1)
    private BigDecimal sellRest;

    @ApiModelProperty(value = "被挖量", position = 2)
    private BigDecimal miningMind;

    @ApiModelProperty(value = "剩余可挖量", position = 3)
    private BigDecimal miningRest;
}
