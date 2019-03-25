package com.yz.aac.exchange.repository.domian;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MerchantAssertTodayTradeRecord {

    @ApiModelProperty(value = "ID", position = 1)
    private Long id;

    @ApiModelProperty(value = "货币符号", position = 2)
    private String currencySymbol;

    @ApiModelProperty(value = "交易类型1: 买入 2: 售出 3: 转账", position = 3)
    private Integer tradeType;

    @ApiModelProperty(value = "平台币单价（每一个单位的商户币兑换多少平台币）", position = 4)
    private BigDecimal platformPrice;

    @ApiModelProperty(value = "交易额度", position = 5)
    private BigDecimal tradeAmount;

    @ApiModelProperty(value = "交易时间", position = 6)
    private Long tradeTime;

    @ApiModelProperty(value = "挂单ID", position = 7)
    private Long orderId;
}
