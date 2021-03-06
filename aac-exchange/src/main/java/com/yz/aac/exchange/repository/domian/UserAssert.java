package com.yz.aac.exchange.repository.domian;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@AllArgsConstructor
@ApiModel("用户资产")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAssert {

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

    @ApiModelProperty(value = "同步时间", position = 6)
    private Long synchronizedTime;

    public UserAssert() {
    }

	public UserAssert(Long userId, String currencySymbol, BigDecimal balance,
			BigDecimal historyMaxBalance, String walletAddress) {
		super();
		this.userId = userId;
		this.currencySymbol = currencySymbol;
		this.balance = balance;
		this.historyMaxBalance = historyMaxBalance;
		this.walletAddress = walletAddress;
	}
    
    
}
