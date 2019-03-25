package com.yz.aac.mining.repository.domian;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("商户交易记录")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MerchantTradeRecord {

    @ApiModelProperty(value = "商户ID", position = 1)
    private Long merchantId;

    @ApiModelProperty(value = "用户ID", position = 2)
    private Long userId;

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
            "8、个人挖矿", position = 3)
    private Integer transactionMode;

    @ApiModelProperty(value = "流通方向：\n" +
            "1、账户冻结存量\n" +
            "2、市场流通量\n" +
            "3、账户挖矿存量", position = 4)
    private Integer flowDirection;

    @ApiModelProperty(value = "交易额度", position = 5)
    private BigDecimal tradeAmount;

    @ApiModelProperty(value = "平台币单价（每一个单位的商户币兑换多少平台币）", position = 6)
    private BigDecimal platformPrice;

    @ApiModelProperty(value = "交易时间", position = 7)
    private Long tradeTime;

    private Integer addSubtract;

}
