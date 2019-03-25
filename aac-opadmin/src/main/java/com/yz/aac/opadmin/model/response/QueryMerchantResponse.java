package com.yz.aac.opadmin.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@ApiModel("查询商户响应")
public class QueryMerchantResponse {

    @ApiModelProperty(value = "总数据量", position = 1, required = true)
    private Long totalSize;

    @ApiModelProperty(value = "平台币单位", position = 2, required = true)
    private String currencyUnit;

    @ApiModelProperty(value = "收入来源统计", position = 3)
    private PlatformCurrencyIncomeSource incomeSource;

    @ApiModelProperty(value = "分页数据明细", position = 4, required = true)
    private List<QueryMerchantResponse.Item> items;

    @ApiModel("查询商户响应分页-平台币收入来源统计")
    @AllArgsConstructor
    @Data
    public static class PlatformCurrencyIncomeSource {

        @ApiModelProperty(value = "挖矿收入", position = 1, required = true)
        private BigDecimal miningIncome;

        @ApiModelProperty(value = "交易收入", position = 2, required = true)
        private BigDecimal tradeIncome;

    }

    @ApiModel("查询商户响应分页数据")
    @AllArgsConstructor
    @Data
    public static class Item {

        @ApiModelProperty(value = "用户ID", position = 1)
        private Long userId;

        @ApiModelProperty(value = "商户ID", position = 2, required = true)
        private Long merchantId;

        @ApiModelProperty(value = "商户编号", position = 3, required = true)
        private String merchantCode;

        @ApiModelProperty(value = "状态（1-启用；2-停用）", position = 4)
        private Integer status;

        @ApiModelProperty(value = "商户名称", position = 5, required = true)
        private String merchantName;

        @ApiModelProperty(value = "币种", position = 6, required = true)
        private String currencySymbol;

        @ApiModelProperty(value = "可用余额", position = 7, required = true)
        private BigDecimal availableBalance;

        @ApiModelProperty(value = "发币时间", position = 8, required = true)
        private Long issuanceDate;

        @ApiModelProperty(value = "发行总量", position = 9, required = true)
        private BigDecimal totalAmount;

        @ApiModelProperty(value = "出售占有量", position = 10, required = true)
        private BigDecimal sellingAmount;

        @ApiModelProperty(value = "挖矿占有量", position = 11, required = true)
        private BigDecimal miningAmount;

        @ApiModelProperty(value = "单价", position = 12)
        private BigDecimal rmbPrice;

        @ApiModelProperty(value = "剩余可售数量", position = 13, required = true)
        private BigDecimal sellRestAmount;

        @ApiModelProperty(value = "已售数量", position = 14, required = true)
        private BigDecimal sellSoldAmount;

        @ApiModelProperty(value = "被挖数量", position = 15, required = true)
        private BigDecimal miningMindAmount;

        @ApiModelProperty(value = "交易次数", position = 16, required = true)
        private Integer tradeCount;

        @ApiModelProperty(value = "自然增长策略ID", position = 17, required = true)
        private Long increaseStrategyId;

        @ApiModelProperty(value = "是否可执行发币押金解冻操作（1-是；2-否）", position = 18)
        private Integer canUnfreeze;
    }

}
