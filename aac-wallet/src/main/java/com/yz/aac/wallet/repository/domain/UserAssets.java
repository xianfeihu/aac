package com.yz.aac.wallet.repository.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@ApiModel("用户资产")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAssets {

    @ApiModelProperty(value = "用户资产ID", position = 1)
    private Long id;

    @ApiModelProperty(value = "用户ID", position = 2)
    private Long userId;

    @ApiModelProperty(value = "货币符号", position = 3)
    private String currencySymbol;

    @ApiModelProperty(value = "余额", position = 4)
    private BigDecimal balance;

    @ApiModelProperty(value = "历史最大余额", position = 5)
    private BigDecimal historyMaxBalance;

    @ApiModelProperty(value = "钱包地址", position = 6)
    private String walletAddress;

    public UserAssets() {
    }

	public UserAssets(Long userId, String currencySymbol, BigDecimal balance,
                      BigDecimal historyMaxBalance, String walletAddress) {
		super();
		this.userId = userId;
		this.currencySymbol = currencySymbol;
		this.balance = balance;
		this.historyMaxBalance = historyMaxBalance;
		this.walletAddress = walletAddress;
	}
    
    
}
