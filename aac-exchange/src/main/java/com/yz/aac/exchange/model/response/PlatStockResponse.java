package com.yz.aac.exchange.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yz.aac.exchange.Constants;
import com.yz.aac.exchange.repository.domian.MerchantTradeRecord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("平台币存量-->商户后台")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlatStockResponse {

    @ApiModelProperty(value = "平台币存量", position = 1)
    private BigDecimal platCurrencySum;

    @ApiModelProperty(value = "分红状态 1、未到分红日 2、分红待发放 3、已完成分红 4、逾期", position = 2)
    private Integer dividendStatus;

    @ApiModelProperty(value = "分红日期 （时间戳）", position = 2)
    private Long dividendDate;

    @ApiModelProperty(value = "固定收益", position = 3)
    private BigDecimal fixedIncome;

    @ApiModelProperty(value = "STO分红 (没有盈利额时 值小于0)", position = 4)
    private BigDecimal stoDividend;

    @ApiModelProperty(value = "总分红 (没有盈利额时 值小于0)", position = 5)
    private BigDecimal incomeSum;

    @ApiModelProperty(value = "平台币交易明细数据量总和", position = 6)
    private Long sum;

    @ApiModelProperty(value = "平台币交易明细", position = 7)
    private List<PlatStockResponse.Item> items;


    @Setter
    @ApiModel("平台币交易明细")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Item {

        @ApiModelProperty(value = "时间", position = 1)
        private Long tradeTime;

        @ApiModelProperty(value = "交易原因:\n" +
                "1、Dapp缴纳押金 \n" +
                "2、Dapp买单成交\n" +
                "3、Dapp卖单成交\n" +
                "4、Dapp充值\n" +
                "5、分红失败返还\n", position = 2)
        private Integer tradeReason;

        @ApiModelProperty(value = "交易量", position = 3)
        private BigDecimal tradeAmount;

        @ApiModelProperty(value = "true->\"+\"   false->\"-\"", position = 3)
        private Boolean flag;

        @ApiModelProperty(value = "来源", hidden = true)
        private String source;

        @ApiModelProperty(value = "平台币单价", hidden = true)
        private BigDecimal platformPrice;

        @ApiModelProperty(value = "交易类型", hidden = true)
        private Integer tradeType;

        public Long getTradeTime() {
            return tradeTime;
        }

        public Integer getTradeReason() {
            return tradeReason;
        }

        public BigDecimal getTradeAmount() {
            return tradeAmount;
        }

        public Boolean getFlag() {
            return flag;
        }

        public Item(String source, Long tradeTime, BigDecimal platformPrice, BigDecimal tradeAmount, Long tradeType) {
            this.tradeTime = tradeTime;
            this.tradeAmount = tradeAmount;
            this.source = source;
            this.platformPrice = platformPrice;
            this.tradeType = tradeType.intValue();

            // 处理交易原因
            switch (source) {
                case "plt":
                    if (Constants.PlatformAssertTradeType.DEPOSIT.code().equals(tradeType))
                    flag = false;
                    tradeReason = 1;
                    if (Constants.PlatformAssertTradeType.BUY.code().equals(tradeType))
                        flag = true;
                    tradeReason = 4;
                    break;
                case "mch":
                    if (Constants.MerchantTradeType.SELL.code().equals(tradeType))
                        flag = false;
                        tradeReason = 2;
                    if (Constants.MerchantTradeType.BUY.code().equals(tradeType))
                        flag = true;
                        tradeReason = 3;
                    break;
                case "div":
                    flag = true;
                    tradeReason = 5;
                    break;
            }
        }

    }

}


