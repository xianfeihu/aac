package com.yz.aac.opadmin.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

import static com.yz.aac.common.Constants.Misc.PLATFORM_CURRENCY_SYMBOL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("查询用户详情响应")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueryUserDetailResponse {

    @ApiModelProperty(value = "用户属性信息", position = 1, required = true)
    private UserAttribute userAttribute;

    @ApiModelProperty(value = "商户属性信息", position = 2)
    private MerchantAttribute merchantAttribute;

    @ApiModelProperty(value = "广告主属性信息", position = 3)
    private AdvertiserAttribute advertiserAttribute;

    @ApiModel("查询用户详情响应-用户属性")
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserAttribute {

        @ApiModelProperty(value = "基本信息", position = 1, required = true)
        private UserBaseInfo baseInfo;

        @ApiModelProperty(value = "资产信息", position = 2, required = true)
        private UserAssetInfo assetInfo;

        @ApiModelProperty(value = "行为信息", position = 3, required = true)
        private UserBehaviourInfo behaviourInfo;

        @ApiModelProperty(value = "交易信息", position = 4, required = true)
        private UserTradeInfo tradeInfo;

        @ApiModel("查询用户详情响应-用户属性-基础信息")
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class UserBaseInfo {

            @ApiModelProperty(value = "用户编号", position = 1, required = true)
            private String userCode;

            @ApiModelProperty(value = "姓名", position = 2, required = true)
            private String userName;

            @ApiModelProperty(value = "联系方式", position = 3, required = true)
            private Long mobileNumber;

            @ApiModelProperty(value = "身份证号", position = 4, required = true)
            private String idNumber;

            @ApiModelProperty(value = "等级名称", position = 5, required = true)
            private String levelName;

            @ApiModelProperty(value = "元力", position = 6, required = true)
            private Long powerPoint;

            @ApiModelProperty(value = "性别（1-男；2-女）", position = 7, required = true)
            private Integer gender;

            @ApiModelProperty(value = "状态（1-启用；2-停用）", position = 8, required = true)
            private Integer status;

            @ApiModelProperty(value = "状态描述", position = 9, required = true)
            private String statusDescription;
        }

        @ApiModel("查询用户详情响应-用户属性-资产信息")
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class UserAssetInfo {

            @ApiModelProperty(value = "平台币资产信息", position = 1, required = true)
            private PlatformCurrencyAsset platformCurrencyAsset;

            @ApiModelProperty(value = "商家币资产信息", position = 2, required = true)
            private MerchantCurrencyAsset merchantCurrencyAsset;

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            public static class PlatformCurrencyAsset {

                @ApiModelProperty(value = "钱包地址", position = 1, required = true)
                private String walletAddress;

                @ApiModelProperty(value = "总资产", position = 2, required = true)
                private BigDecimal balance;

                @ApiModelProperty(value = "挖矿收入", position = 3, required = true)
                private BigDecimal miningIncome;

                @ApiModelProperty(value = "交易收入", position = 4, required = true)
                private BigDecimal tradeIncome;

                @ApiModelProperty(value = "冻结资产列表", position = 5, required = true)
                private List<FreezingAsset> freezingAssets;
            }

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            public static class MerchantCurrencyAsset {

                @ApiModelProperty(value = "币项", position = 1, required = true)
                private List<MerchantCurrencyAssetItem> merchantCurrencyAssetItems;

                @ApiModelProperty(value = "冻结资产列表", position = 5, required = true)
                private List<FreezingAsset> freezingAssets;

            }

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            public static class FreezingAsset {

                @ApiModelProperty(value = "币种", position = 1, required = true)
                private String currencySymbol;

                @ApiModelProperty(value = "冻结额度", position = 2, required = true)
                private BigDecimal amount;

                @ApiModelProperty(value = "冻结原因", position = 3, required = true)
                private String reason;

            }
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class MerchantCurrencyAssetItem {

            @ApiModelProperty(value = "币种", position = 1, required = true)
            private String currencySymbol;

            @ApiModelProperty(value = "钱包地址", position = 2, required = true)
            private String walletAddress;

            @ApiModelProperty(value = "总资产", position = 3, required = true)
            private BigDecimal balance;

            @ApiModelProperty(value = "是否已过限售期（1-是；2-否）", position = 4, required = true)
            private Integer unRestricted;
        }

        @ApiModel("查询用户详情响应-用户属性-行为信息")
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class UserBehaviourInfo {

            @ApiModelProperty(hidden = true)
            private Integer tradeCount;

            @ApiModelProperty(value = "签到次数", position = 1, required = true)
            private Integer signInCount;

            @ApiModelProperty(value = "答题次数", position = 2, required = true)
            private Integer answerCount;

            @ApiModelProperty(value = "关注公众号次数", position = 3, required = true)
            private Integer attentionPublicNumberCount;

            @ApiModelProperty(value = "阅读文章次数", position = 4, required = true)
            private Integer readArticleCount;

            @ApiModelProperty(value = "发红包次数", position = 5, required = true)
            private Integer sendRedPacketCount;

            @ApiModelProperty(value = "抢到红包次数", position = 6, required = true)
            private Integer grabRedPacketCount;

            @ApiModelProperty(value = "玩游戏次数", position = 7, required = true)
            private Integer playGameCount;

            @ApiModelProperty(value = "挖矿活动参加次数", position = 8, required = true)
            private Integer miningEventCount;

            @ApiModelProperty(value = "邀请好友次数", position = 9, required = true)
            private Integer inviteCount;

            @ApiModelProperty(value = "发帖次数", position = 10, required = true)
            private Integer postArticleCount;

            @ApiModelProperty(value = "有效点击广告次数", position = 11, required = true)
            private Integer clickAdCount;

            @ApiModelProperty(value = "广告表单提交次数", position = 12, required = true)
            private Integer submitAdFormCount;
        }

        @ApiModel("查询用户详情响应-用户属性-交易信息")
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class UserTradeInfo {

            @ApiModelProperty(value = "交易次数", position = 1, required = true)
            private Integer tradeCount;

            @ApiModelProperty(value = "交易项", position = 2, required = true)
            private List<TradeRecord> tradeRecords;

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            public static class TradeRecord {

                @ApiModelProperty(value = "交易时间", position = 1, required = true)
                private Long time;

                @ApiModelProperty(value = "行为（1-买入；2-卖出；3-转账；4-收账；5-给对方分红；6-收到来自对方的分红）", position = 2, required = true)
                private Integer action;

                @ApiModelProperty(value = "币种", position = 3, required = true)
                private String currencySymbol;

                @ApiModelProperty(value = "数额", position = 4, required = true)
                private BigDecimal amount;

                @ApiModelProperty(value = "交易伙伴名称", position = 5, required = true)
                private String partnerName;

            }
        }
    }

    @ApiModel("查询用户详情响应-商户属性")
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MerchantAttribute {

        @ApiModelProperty(value = "基本信息", position = 1, required = true)
        private MerchantBaseInfo baseInfo;

        @ApiModelProperty(value = "发币信息", position = 2, required = true)
        private MerchantIssuanceInfo issuanceInfo;

        @ApiModelProperty(value = "出售信息", position = 3, required = true)
        private MerchantSellInfo sellInfo;

        @ApiModel("查询用户详情响应-商户属性-基础信息")
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class MerchantBaseInfo {

            @ApiModelProperty(value = "商户编号", position = 1, required = true)
            private String merchantCode;

            @ApiModelProperty(value = "商户名称", position = 2, required = true)
            private String merchantName;

            @ApiModelProperty(value = "姓名", position = 3, required = true)
            private String name;

            @ApiModelProperty(value = "联系方式", position = 4, required = true)
            private Long mobileNumber;

            @ApiModelProperty(value = "身份证号", position = 5, required = true)
            private String idNumber;

            @ApiModelProperty(value = "状态（1-启用；2-停用）", position = 6)
            private Integer status;

            @ApiModelProperty(value = "状态描述", position = 7)
            private String statusDescription;
        }

        @ApiModel("查询用户详情响应-商户属性-发币信息")
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class MerchantIssuanceInfo {

            @ApiModelProperty(value = "币种", position = 1, required = true)
            private String currencySymbol;

            @ApiModelProperty(value = "总发行量", position = 2, required = true)
            private BigDecimal total;

            @ApiModelProperty(value = "挖矿比例（%之前的部分）", position = 3, required = true)
            private Double miningRate;

            @ApiModelProperty(value = "固定收益率（%之前的部分）", position = 4)
            private Double fixedIncomeRate;

            @ApiModelProperty(value = "STO分红收益率（%之前的部分）", position = 5)
            private Double stoRate;

            @ApiModelProperty(value = "是否其他模式（1-是；2-否）", position = 6)
            private Integer otherMode;

            @ApiModelProperty(value = "收益周期（单位：天）", position = 7, required = true)
            private Integer incomePeriod;

            @ApiModelProperty(value = "限售期（单位：天）", position = 8, required = true)
            private Integer restrictionPeriod;

            @ApiModelProperty(value = "发币押金", position = 9, required = true)
            private BigDecimal issuanceDeposit;

            @ApiModelProperty(value = "平台币符号", position = 10, required = true)
            private String platformCurrencySymbol = PLATFORM_CURRENCY_SYMBOL.value();

            @ApiModelProperty(value = "白皮书URL", position = 11)
            private String whitePaperUrl;

            @ApiModelProperty(value = "可挖数量", position = 12, required = true)
            private BigDecimal miningRest;

            @ApiModelProperty(value = "已挖数量", position = 13, required = true)
            private BigDecimal miningMind;

        }

        @ApiModel("查询用户详情响应-商户属性-出售信息")
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class MerchantSellInfo {

            @ApiModelProperty(value = "出售次数", position = 1, required = true)
            private Integer sellCount;

            @ApiModelProperty(value = "出售总量", position = 2, required = true)
            private BigDecimal sellAmount;

            @ApiModelProperty(value = "出售项", position = 3, required = true)
            private List<SoldRecord> soldRecords;

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            public static class SoldRecord {

                @ApiModelProperty(value = "出售时间", position = 1, required = true)
                private Long time;

                @ApiModelProperty(value = "买家用户编码", position = 2, required = true)
                private String partnerCode;

                @ApiModelProperty(value = "买家用户姓名", position = 3, required = true)
                private String partnerName;

                @ApiModelProperty(value = "数额", position = 4, required = true)
                private BigDecimal amount;
            }
        }

    }

    @ApiModel("查询用户详情响应-广告主属性")
    @Data
    @AllArgsConstructor
    public static class AdvertiserAttribute {

    }

}
