package com.yz.aac.wallet.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("兑换数据提交请求")
public class ExchangeRequest {

    @ApiModelProperty(value = "兑换服务ID", position = 1, required = true)
    private Long exchangeId;
    @ApiModelProperty(value = "充值号码", position = 2, required = true)
    private String chargingNumber;
    @ApiModelProperty(value = "充值选项ID", position = 3, required = false)
    private Long optionId;
    @ApiModelProperty(value = "兑换法币金额", position = 4, required = false)
    private Integer rmbAmount;
    @ApiModelProperty(value = "支付密码", position = 5, required = true)
    private String payPass;
    @ApiModelProperty(value = "充值类型（1-选项卡充值 2-手动充值）", position =6, required = true)
    private Integer tagId;
    @ApiModelProperty(value = "兑换类型（1-兑换话费 2-中石油兑换 3-中石化兑换）", position =7, required = true)
    private Integer exchangeType;


}
