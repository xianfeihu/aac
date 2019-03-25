package com.yz.aac.wallet.repository.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
@ApiModel(value = "用户地址信息")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserWalletAddress {

    @ApiModelProperty(value = "地址ID", position = 1)
    private Integer id;

    @ApiModelProperty(value = "用户姓名", position = 2, required = true)
    private String userName;

    @ApiModelProperty(value = "备注", position = 3, required = true)
    private String remarks;

    @ApiModelProperty(value = "钱包地址", position = 4, required = true)
    private String walletAddress;

    @ApiModelProperty(value = "货币符号", position = 5, required = true)
    private String currencySymbol;

    @ApiModelProperty(value = "用户ID", position = 6)
    private Long userId;

    public UserWalletAddress() {
    }
}
