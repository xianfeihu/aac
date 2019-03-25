package com.yz.aac.opadmin.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@ApiModel("查询用户响应")
public class QueryUserResponse {

    @ApiModelProperty(value = "总数据量", position = 1, required = true)
    private Long totalSize;

    @ApiModelProperty(value = "平台币单位", position = 2, required = true)
    private String currencyUnit;

    @ApiModelProperty(value = "分页数据明细", position = 3, required = true)
    private List<QueryUserResponse.Item> items;

    @ApiModel("查询用户响应分页数据")
    @AllArgsConstructor
    @Data
    public static class Item {

        @ApiModelProperty(value = "用户ID", position = 1, required = true)
        private Long userId;

        @ApiModelProperty(value = "用户编号", position = 2, required = true)
        private String userCode;

        @ApiModelProperty(value = "状态（1-启用；2-停用）", position = 3, required = true)
        private Integer userStatus;

        @ApiModelProperty(value = "姓名", position = 4, required = true)
        private String userName;

        @ApiModelProperty(value = "钱包地址", position = 5, required = true)
        private String walletAddress;

        @ApiModelProperty(value = "可用余额", position = 6, required = true)
        private BigDecimal availableBalance;

        @ApiModelProperty(value = "冻结资产总额", position = 7, required = true)
        private BigDecimal freezingAmount;

        @ApiModelProperty(value = "其他资产信息", position = 8, required = true)
        private List<MerchantCurrencyAssert> otherAsserts;

        @ApiModelProperty(value = "等级名称", position = 9, required = true)
        private String levelName;

        @ApiModelProperty(value = "元力", position = 10, required = true)
        private Long powerPoint;

        @ApiModelProperty(value = "交易次数", position = 11, required = true)
        private Long tradeCount;

        @ApiModelProperty(value = "自然增长算法ID", position = 12, required = true)
        private Long increaseAlgorithmId;

        @ApiModelProperty(value = "平台币收入来源", position = 13, required = true)
        private PlatformCurrencyIncomeSource platformCurrencyIncomeSource;

        @ApiModel("查询用户响应分页-平台币收入来源")
        @AllArgsConstructor
        @Data
        public static class PlatformCurrencyIncomeSource {

            @ApiModelProperty(value = "挖矿收入", position = 1, required = true)
            private BigDecimal miningIncome;

            @ApiModelProperty(value = "交易收入", position = 2, required = true)
            private BigDecimal tradeIncome;

        }

        @ApiModel("查询用户响应分页-其他资产数据")
        @AllArgsConstructor
        @Data
        public static class MerchantCurrencyAssert {

            @ApiModelProperty(value = "币种", position = 1, required = true)
            private String currencySymbol;

            @ApiModelProperty(value = "可用余额", position = 2, required = true)
            private BigDecimal availableBalance;

            @ApiModelProperty(value = "是否已过限售期（1-是；2-否）", position = 3, required = true)
            private Integer unRestricted;
        }
    }

}
