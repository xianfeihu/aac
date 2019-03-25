package com.yz.aac.opadmin.repository.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeItem {

    private Long id;

    private Long exchangeId;

    private Set<Long> exchangeIds;

    @ApiModelProperty(value = "充值法币金额", position = 1, required = true)
    private Integer rmbAmount;

    @ApiModelProperty(value = "支付平台币金额", position = 2, required = true)
    private BigDecimal platformAmount;

    private Integer orderNumber;

}
