package com.yz.aac.exchange.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

import static com.yz.aac.exchange.Constants.MerchantTradeType.SELL;

@Setter
@ApiModel("冻结卖单详情")
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MerchantFreezeStockDetailsResponse {

    @ApiModelProperty(value = "交易时间", position = 1)
    private Long tradeTime;

    @ApiModelProperty(value = "交易账号", position = 2)
    private String accountNumber;

    @ApiModelProperty(value = "交易流向", position = 3)
    private String tradeResult;

    /**
     *  挂单类型
     */
    private Long type;

    /**
     * 交易额度
     */
    private BigDecimal tradeAmount;

    public Long getTradeTime() {
        return tradeTime;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getTradeResult() {
        if (type!=null) {
            if (type.equals(SELL.code())) {
                return "-"+tradeAmount;
            } else {
                return "+"+tradeAmount;
            }
        }
        return tradeResult;
    }

    public MerchantFreezeStockDetailsResponse(Long type, Long tradeTime, String accountNumber, BigDecimal tradeAmount) {
        this.tradeTime = tradeTime;
        this.type = type;
        this.accountNumber = accountNumber;
        this.tradeAmount = tradeAmount;
    }
}
