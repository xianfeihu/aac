package com.yz.aac.wallet.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("转账数据提交请求")
public class TransferToWalletAddrRequest {

    @ApiModelProperty(value = "转账发起人ID", position = 1)
    private Long userId;
    @ApiModelProperty(value = "币的类型", position = 2, required = true)
    private String coinType;
    @ApiModelProperty(value = "发送地址", position = 3, required = true)
    private String sendAddr;
    @ApiModelProperty(value = "接收地址", position = 4, required = true)
    private String receiveAddr;
    @ApiModelProperty(value = "转账金额", position = 5, required = true)
    private BigDecimal amount;
    @ApiModelProperty(value = "支付密码", position = 6, required = true)
    private Integer payPass;

}
