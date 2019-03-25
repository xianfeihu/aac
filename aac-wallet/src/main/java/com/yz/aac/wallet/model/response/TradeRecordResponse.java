package com.yz.aac.wallet.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yz.aac.common.util.EnumUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.function.Function;

import static com.yz.aac.wallet.Constants.*;

@Setter
@ApiModel("交易记录")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TradeRecordResponse {

    private Long userId;

    private Long initiatorId;

    private Long partnerId;

    private Long tradeType;

    private BigDecimal tradeAmount;

    private String currencySymbol;

    private BigDecimal platformPrice;

    @ApiModelProperty(value = "交易时间", position = 1)
    private Long tradeTime;

    @ApiModelProperty(value = "购买信息", position = 2)
    private String buyMessage;

    @ApiModelProperty(value = "花费信息", position = 3)
    private String sellMessage;

    public TradeRecordResponse(Long userId, Integer initiatorId, Integer partnerId, Long tradeType, BigDecimal tradeAmount, String currencySymbol, BigDecimal platformPrice, Long tradeTime) {
        this.userId = userId;
        this.initiatorId = initiatorId.longValue();
        this.partnerId = partnerId.longValue();
        this.tradeType = tradeType;
        this.tradeAmount = tradeAmount;
        this.currencySymbol = currencySymbol;
        this.platformPrice = platformPrice;
        this.tradeTime = tradeTime;

        // 初始化数据
        StringBuilder str;
        if (StringUtils.isEmpty(currencySymbol)) {
            //交易信息
            Function<Integer,Boolean> tradeTypeFunction = f -> {
                if (f == PlatformAssertTradeType.EXCHANGE_CALLS.code() || f == PlatformAssertTradeType.EXCHANGE_CARD.code()) {
                    return true;
                }
                return false;
            };

            buyMessage = new StringBuilder()
                    .append( EnumUtil.getByCode(PlatformAssertTradeType.class,tradeType.intValue()).des() )
                    .append(tradeTypeFunction.apply(tradeType.intValue()) ? " " : com.yz.aac.common.Constants.Misc.PLATFORM_CURRENCY_SYMBOL.value() + " ")
                    .append( tradeAmount )
                    .append( tradeTypeFunction.apply(tradeType.intValue()) ? "" : Other.PIE.value() )
                    .toString();
            sellMessage = new StringBuilder()
                    .append( Other.SPEND.value() + tradeAmount.multiply(platformPrice).setScale(2, BigDecimal.ROUND_HALF_UP) )
                    .append( tradeTypeFunction.apply(tradeType.intValue()) ? com.yz.aac.common.Constants.Misc.PLATFORM_CURRENCY_SYMBOL.value() :  com.yz.aac.common.Constants.Misc.LEGAL_CURRENCY_SYMBOL.value())
                    .toString();
        } else {
            // 购买商户币交易信息
            if ( userId.longValue() == initiatorId ) {
                buyMessage = new StringBuilder()
                        .append( EnumUtil.getByCode(MerchantTradeType.class,tradeType.intValue()).des() )
                        .append( currencySymbol + " " )
                        .append( tradeAmount )
                        .append( Other.PIE.value() )
                        .toString();
                sellMessage = new StringBuilder()
                        .append( tradeType == MerchantTradeType.BUY.code() ? Other.SPEND.value() : Other.GAIN.value() )
                        .append( tradeAmount.multiply(platformPrice).setScale(2, BigDecimal.ROUND_HALF_UP) )
                        .append( com.yz.aac.common.Constants.Misc.PLATFORM_CURRENCY_SYMBOL.value() )
                        .toString();
            } else {
                str = new StringBuilder();
                String strr = "";
                if ( tradeType == MerchantTradeType.BUY.code() ) {
                    str.append(MerchantTradeType.SELL.des());
                    strr = Other.GAIN.value();
                } else {
                    str.append(MerchantTradeType.BUY.des());
                    strr = Other.SPEND.value();
                }
                str.append( currencySymbol  + " " );
                str.append( tradeAmount );
                str.append( Other.PIE.value() );
                buyMessage = str.toString();
                sellMessage = new StringBuilder()
                        .append( strr )
                        .append( tradeAmount.multiply(platformPrice).setScale(2, BigDecimal.ROUND_HALF_UP) )
                        .append( com.yz.aac.common.Constants.Misc.PLATFORM_CURRENCY_SYMBOL.value() )
                        .toString();
            }
        }
    }

    public Long getTradeTime() {
        return tradeTime;
    }

    public String getBuyMessage() {
        return buyMessage;
    }

    public String getSellMessage() {
        return sellMessage;
    }
}
