package com.yz.aac.wallet.repository.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@ApiModel("用户资产冻结")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAssetsFreeze {

    @ApiModelProperty(value = "ID", position = 1)
    private Long id;

    @ApiModelProperty(value = "用户ID", position = 2, required = true)
    private Long userId;

    @ApiModelProperty(value = "货币符号", position = 3, required = true)
    private String currencySymbol;

    @ApiModelProperty(value = "用户资产冻结", position = 4)
    private BigDecimal amount;

    @ApiModelProperty(value = "原因 1: 发币押金 2: 挂单购买 3: 挂单出售", position = 5, required = true)
    private Integer reason;

    @ApiModelProperty(value = "冻结时间", position = 6)
    private Long actionTime;


}
