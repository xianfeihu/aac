package com.yz.aac.exchange.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("商户币交易数据")
public class MerchantCurrencyExchangeRequest {

    @ApiModelProperty(value = "用户ID(用户同步后必传)", position = 1)
    private Long userId;

    @ApiModelProperty(value = "商户ID", position = 2, required = true)
    private Long merchantId;

    @ApiModelProperty(value = "金额", position = 3, required = true)
    private BigDecimal amount;

    @ApiModelProperty(value = "交易方式：\n" +
            "流失\n" +
            "1、小程序充值\n" +
            "2、个人转币\n" +
            "3、挂卖单\n" +
            "增长\n" +
            "4、取消卖单\n" +
            "5、店内消费\n" +
            "6、挂买单成交\n" +
            "7、直接回购\n" +
            "8、个人挖矿", position = 4, required = true)
    private Integer transactionMode;

}
