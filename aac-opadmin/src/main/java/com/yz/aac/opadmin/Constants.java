package com.yz.aac.opadmin;

import java.math.BigDecimal;

@SuppressWarnings("unused")
public class Constants {

    /**
     * 请求字段
     */
    public enum RequestFiled {
        TEXTS("texts"),
        TITLE("title"),
        USER_ID("userId"),
        USER_NAME("userName"),
        ID("id"),
        BEGIN_TIME("beginTime"),
        END_TIME("endTime"),
        PAGE_NUMBER("pageNumber"),
        PAGE_SIZE("pageSize"),
        LOGIN_NAME("loginName"),
        MIN_BALANCE("minBalance"),
        MAX_BALANCE("maxBalance"),
        PASSWORD("password"),
        CODE("code"),
        NAME("name"),
        ICON("icon"),
        PAY_NUMBER("payNumber"),
        DIRECTION("direction"),
        USER_ROLE("userRole"),
        CATEGORY("category"),
        SUB_CATEGORY("subCategory"),
        KEYWORD("keyword"),
        KEY("key"),
        VALUE("value"),
        STATUS("status"),
        TRADE_TYPE("tradeType"),
        OUTPUT_STATISTICS("outputStatistics"),
        ALIPAY_QR_CODE_ICON("alipayQrCodeIcon"),
        WECHAT_QR_CODE_ICON("wechatQrCodeIcon"),
        UPDATE_ICON("updateIcon"),
        UPDATE_ALIPAY_QR_CODE_ICON("updateAlipayQrCodeIcon"),
        UPDATE_WECHAT_QR_CODE_ICON("updateWechatQrCodeIcon"),
        DEPARTMENT("department"),
        TRADE_CHARGE_RATE("tradeChargeRate"),
        ISSUANCE_DEPOSIT("issuanceDeposit"),
        INCREASED_POWER_POINT("increasedPowerPoint"),
        CONSUMED_AD("consumedAd"),
        PLATFORM_CURRENCY("platformCurrency"),
        CURRENCY_SYMBOL("currencySymbol"),
        MATCH_CONDITION("matchCondition"),
        SUPPORT_ALIPAY("supportAlipay"),
        SUPPORT_WECHAT("supportWechat"),
        SUPPORT_BANK_CARD("supportBankCard"),
        ALIPAY_ACCOUNT("alipayAccount"),
        WECHAT_ACCOUNT("wechatAccount"),
        BANK_CARD_NUMBER("bankCardNumber"),
        RMB_PRICE("rmbPrice"),
        RMB_AMOUNT("rmbAmount"),
        MIN_AMOUNT_LIMIT("minAmountLimit"),
        MAX_AMOUNT_LIMIT("maxAmountLimit"),
        AVAILABLE_TRADE_AMOUNT("availableTradeAmount"),
        REMARK("remark"),
        SELLER_ID("sellerId"),
        DESCRIPTION("description"),
        STRATEGY_ID("strategyId"),
        SERVICE_CHARGE_ID("serviceChargeId"),
        FREEZING_TYPE("freezingType"),
        POWER_POINT_BONUS("powerPointBonus"),
        ANSWERS("answers"),
        CORRECT("correct"),
        MIN_CORRECT("minCorrect"),
        MAX_CORRECT("maxCorrect"),
        BUSINESS("business"),
        INDEXES("indexes"),
        FILES("files"),
        CATEGORY_ID("categoryId"),
        CUSTOMIZED("customized"),
        LIMIT_IN_MONTH("limitInMonth"),
        ITEMS("items"),
        PLATFORM_AMOUNT("platformAmount"),
        SESSIONS("sessions"),
        ACTIVITIES("activities"),
        TOTAL_BONUS("totalBonus"),
        LUCKY_RATE("luckyRate"),
        LUCK_TIMES("luckyTimes"),
        HIT_AD_NUMBER("hitAdNumber");
        private String value;

        RequestFiled(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }

    /**
     * 错误消息
     */
    public enum ErrorMessage {
        NONE("无"),
        INVALID_ID("无效的ID"),
        INVALID_VALUE("无效的值：[{}]"),
        INVALID_USER_CODE("无效的用户编号"),
        INVALID_MERCHANT_CODE("无效的商户编号"),
        INVALID_CURRENCY_PERIOD("无效的金额区间"),
        INVALID_LOGIN_NAME_OR_PASSWORD("账号或密码错误"),
        DISABLED_ACCOUNT("该账号已被停用"),
        EXISTS_ACCOUNT("该账号已存在"),
        EXISTS_SERVICE("该服务已存在"),
        EXISTS_NAME("该名称已存在"),
        ACCESS_DENY("拒绝访问"),
        TARGET_DATA_MISSING("无法查询到目标数据"),
        DELETE_DEFAULT_DENY("无法删除默认项"),
        DELETE_USING_DENY("此项正在使用中，无法删除"),
        DELETE_SELLER_DENY("该人员名下已有挂出的卖单，不能删除"),
        INVALID_STATUS("数据状态不正确，执行失败"),
        INVALID_CONFIG_PARAM("配置项无效"),
        MISSING_USER_ASSERT("获取用户钱包数据失败"),
        MISSING_SERVICE_CHARGE("获取手续费押金数据失败"),
        MISSING_MERCHANT("获取商户数据失败"),
        MISSING_MERCHANT_USER("获取该商户的用户数据失败"),
        MISSING_FREEZING_ASSET("获取冻结资产数据失败"),
        MISSING_MERCHANT_ISSUANCE("获取商户发币数据失败"),
        MISSING_USER_INCOME("获取用户收入统计数据失败"),
        MISSING_PLATFORM_SELLING_ORDER("获取平台币卖单数据失败"),
        MISSING_PLATFORM_SELLER("获取平台币挂单人数据失败"),
        MISSING_INCREASE_STRATEGY("获取自然增长策略数据失败"),
        MAX_AMOUNT_LIMIT_LESS_THAN_MIN("限购额区间错误"),
        AMOUNT_LESS_THAN_MAX_AMOUNT_LIMIT("数量不可低于最大限购额"),
        UPDATE_READONLY_DATA_DENY("禁止更新只读信息"),
        ONE_CORRECT_ANSWER_REQUIRED("每题正确答案有且只能有一项"),
        MAX_CORRECT_LESS_THAN_MIN("答对题数区间错误"),
        INVALID_TEXTS_COUNT("文本数量无效"),
        INVALID_FILES_COUNT("附件数量无效"),
        CONTENT_REQUIRED("请输入内容"),
        INVALID_ARTICLE_CATEGORY_ID("无效的文章分类ID：[{}]"),
        ARTICLE_ELEMENTS_LIMIT("文章元素最多[{}]个"),
        ARTICLE_CATEGORY_LIMIT("文章分类最多[{}]个"),
        INVALID_CHAR("请勿包含特殊字符"),
        EXCHANGE_ITEM_LIMIT("推荐项需保持在[{}] 至 [{}]个之间");
        private String value;

        ErrorMessage(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }

    /**
     * 是否项
     */
    public enum DefaultItem {
        YES(1),
        NO(2);
        private int value;

        DefaultItem(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    }

    /**
     * 操作员或用户账户状态
     */
    public enum AccountStatus {
        ENABLED(1),
        DISABLED(2);
        private int value;

        AccountStatus(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    }

    /**
     * 操作员角色
     */
    public enum OperatorRole {
        OPERATOR(1),
        SUPER_ADMIN(2);
        private int value;

        OperatorRole(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    }

    /**
     * 用户角色
     */
    public enum UserRole {
        ALL(1),
        GENERAL(2),
        MERCHANT(3),
        ADVERTISER(4);
        private int value;

        UserRole(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    }

    /**
     * 操作日志功能模块
     */
    public enum FeatureModule {
        MERCHANT_AUDIT(1, "商户管理-商户审核"),
        ADVERTISER_AUDIT(2, "广告主管理-广告主审核"),
        AD_AUDIT(3, "广告内容管理-广告审核"),
        PLATFORM_CURRENCY_TRADE(4, "交易管理-AAB交易"),
        PLATFORM_CURRENCY_SELLER_CONFIG(5, "系统管理-挂单人员设置");

        private int id;
        private String description;

        FeatureModule(int value, String description) {
            this.id = value;
            this.description = description;
        }

        public int id() {
            return id;
        }

        public String description() {
            return description;
        }
    }

    /**
     * 操作日志功能模块操作
     */
    public enum FeatureModuleAction {
        MERCHANT_AUDIT_APPROVED(1, 1, "通过了[商户名称]的审核", "选择手续费押金为【{}】"),
        MERCHANT_AUDIT_REJECTED(1, 2, "拒绝了[商户名称]的审核", "拒绝原因【{}】"),
        ADVERTISER_AUDIT_APPROVED(2, 1, "通过了[企业名称]的审核", null),
        ADVERTISER_AUDIT_REJECTED(2, 2, "拒绝了[企业名称]的审核", null),
        AD_AUDIT_APPROVED(3, 1, "通过了[企业名称-广告名称]的审核", null),
        AD_AUDIT_REJECTED(3, 2, "拒绝了[企业名称-广告名称]的审核", null),
        PLATFORM_CURRENCY_TRADE_TRANSFER(4, 1, "转账", "收款用户【{}】，挂单人【{}】，金额【{} {}】"),
        PLATFORM_CURRENCY_TRADE_ADD_SELLING_ORDER(4, 2, "添加挂单", "限价【{} RMB】，限购【{} {}】-【{} {}】，数量【{}】，备注【{}】，挂单人【{}】"),
        PLATFORM_CURRENCY_SELLER_CONFIG_ADD_SELLER(5, 1, "添加挂单人", "姓名【{}】，支付方式：支付宝【{}】，微信【{}】，银行卡【{}】"),
        PLATFORM_CURRENCY_SELLER_CONFIG_REMOVE_SELLER(5, 2, "删除挂单人", "姓名【{}】"),
        PLATFORM_CURRENCY_SELLER_CONFIG_MODIFY_SELLER(5, 3, "修改挂单人", "姓名【{}】，支付方式：支付宝【{}】，微信【{}】，银行卡【{}】");

        private int moduleId;
        private int actionId;
        private String description;
        private String additionalInfo;

        FeatureModuleAction(int moduleId, int actionId, String description, String additionalInfo) {
            this.moduleId = moduleId;
            this.actionId = actionId;
            this.description = description;
            this.additionalInfo = additionalInfo;
        }

        public int moduleId() {
            return moduleId;
        }

        public int actionId() {
            return actionId;
        }

        public String description() {
            return description;
        }

        public String additionalInfo() {
            return additionalInfo;
        }
    }

    /**
     * 商家币交易类型
     */
    public enum MerchantTradeType {
        BUY(1),
        SELL(2),
        TRANSFER(3);
        private int value;

        MerchantTradeType(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    }

    /**
     * 平台币交易类型
     */
    public enum PlatformTradeType {
        ISSUANCE_DEPOSIT(1),
        BUY(2),
        TRANSFER(3);
        private int value;

        PlatformTradeType(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    }

    /**
     * 平台币交易状态
     */
    public enum PlatformTradeStatus {
        WAITING_TRANSFER(1),
        TRANSFERRED(2),
        CANCELED(3);
        private int value;

        PlatformTradeStatus(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    }

    /**
     * 平台币进出帐本交易类型
     */
    public enum LedgerType {
        //充值平台币（进账）
        RECHARGE_PLATFORM_CURRENCY(1),
        //发币押金（进账）
        ISSUANCE_DEPOSIT(2),
        //交易手续费（进账）
        SERVICE_CHARGE(3),
        //广告费（进账）
        AD_CHARGE(4),
        //购买平台币转账（出帐）
        TRANSFER_PLATFORM_CURRENCY(5),
        //充值话费（法币出账）
        MOBILE_RECHARGE(9),
        //充值中石油油卡（法币出账）
        CNPC_RECHARGE(10),
        //充值中石化油卡（法币出账）
        CPDC_RECHARGE(11);
        private int value;

        LedgerType(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    }

    /**
     * 平台币进出帐区分
     */
    public enum LedgerInOut {
        IN(1),
        OUT(2);

        private int value;

        LedgerInOut(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    }


    /**
     * 配置
     */
    public enum Config {
        READING_TIME(1, 1, "READING_TIME"),
        MAX_READING_PER_DAY(1, 1, "MAX_READING_PER_DAY"),
        READING_CURRENCY(1, 1, "READING_CURRENCY"),
        MIN_ARTICLE_LENGTH(1, 1, "MIN_ARTICLE_LENGTH"),
        MAX_PUBLISHING_PER_DAY(1, 1, "MAX_PUBLISHING_PER_DAY"),
        PUBLISHING_CURRENCY(1, 1, "PUBLISHING_CURRENCY"),
        REAL_NAME_VERIFICATION_CURRENCY(1, 2, "REAL_NAME_VERIFICATION_CURRENCY"),
        SING_IN_POINT(1, 3, "SING_IN_POINT"),
        ATTENTION_PUBLIC_NUMBER_POINT(1, 4, "ATTENTION_PUBLIC_NUMBER_POINT"),
        MAX_INVITATION_PER_DAY(1, 5, "MAX_INVITATION_PER_DAY"),
        INVITATION_CURRENCY(1, 5, "INVITATION_CURRENCY"),
        MINING_ROYALTY_RATE(1, 5, "MINING_ROYALTY_RATE"),
        GRAB_RED_PACKET_PER_DAY(1, 6, "GRAB_RED_PACKET_PER_DAY"),
        MAX_PARTICIPATION_PER_DAY(1, 7, "MAX_PARTICIPATION_PER_DAY"),
        QUESTIONS_PER_PARTICIPATION(1, 7, "QUESTIONS_PER_PARTICIPATION"),
        PRICE_FLOATING_RATE(2, 1, "PRICE_FLOATING_RATE"),
        RMB_EXCHANGE_RATE(2, 2, "RMB_EXCHANGE_RATE");

        private int category;

        private int subCategory;

        private String key;

        Config(int category, int subCategory, String key) {
            this.category = category;
            this.subCategory = subCategory;
            this.key = key;
        }

        public int category() {
            return category;
        }

        public int subCategory() {
            return subCategory;
        }

        public String key() {
            return key;
        }
    }

    /**
     * 用户行为
     */
    public enum UserBehaviour {
        //交易次数
        TRADE("TRADE"),
        //签到次数
        SIGN_IN("SIGN_IN"),
        //答题次数
        ANSWER("ANSWER"),
        //关注公众号次数
        ATTENTION_PUBLIC_NUMBER("ATTENTION_PUBLIC_NUMBER"),
        //阅读文章次数
        READ_ARTICLE("READ_ARTICLE"),
        //发红包次数
        SEND_RED_PACKET("SEND_RED_PACKET"),
        //抢到红包次数
        GRAB_RED_PACKET("GRAB_RED_PACKET"),
        //玩游戏次数
        PLAY_GAME("PLAY_GAME"),
        //挖矿活动参加次数
        MINING_EVENT("MINING_EVENT"),
        //邀请好友次数
        INVITE("INVITE"),
        //发帖次数
        POST_ARTICLE("POST_ARTICLE"),
        //有效点击广告次数
        CLICK_AD("CLICK_AD"),
        //广告表单提交次数
        SUBMIT_AD_FORM("SUBMIT_AD_FORM");

        private String value;

        UserBehaviour(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }

    /**
     * 用户收入来源
     */
    public enum UserIncomeSource {
        RECHARGE("RECHARGE"),
        SELL("SELL"),
        TRANSFER("TRANSFER"),
        MINING("MINING");

        private String value;

        UserIncomeSource(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }

    /**
     * 用户资产冻结原因
     */
    public enum UserAssertFreezingReason {
        DEPOSIT(1, "发币押金"),
        BUY(2, "挂单购买"),
        SELL(3, "挂单出售");

        private int code;
        private String desc;

        UserAssertFreezingReason(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int code() {
            return code;
        }

        public String desc() {
            return desc;
        }
    }

    /**
     * 商户审核状态
     */
    public enum MerchantAuditStatus {
        ISSUED(1, "发币待审核"),
        WAITING_DEPOSIT(2, "待商户交纳押金"),
        ISSUED_REJECTED(6, "发币审核失败"),
        WAITING_DEPOSIT_AUDIT(3, "押金已交待审核"),
        DEPOSIT_REJECTED(4, "押金审核失败"),
        DEPOSIT_APPROVED(5, "押金审核通过");
        private int status;

        private String description;

        MerchantAuditStatus(int status, String description) {
            this.status = status;
            this.description = description;
        }

        public int status() {
            return status;
        }

        public String description() {
            return description;
        }
    }

    /**
     * 商户资产统计
     */
    public enum MerchantAssertStatistics {
        UNRESTRICTED("UNRESTRICTED"),
        TRADED("TRADED"),
        MINING_MIND("MINING_MIND"),
        MINING_REST("MINING_REST"),
        SELL_SOLD("SELL_SOLD"),
        SELL_REST("SELL_REST");

        private String value;

        MerchantAssertStatistics(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }

    /**
     * 平台资产统计
     */
    public enum PlatformAssertStatistics {
        UPDATE_TIME("UPDATE_TIME"),
        TOTAL("TOTAL"),
        ACTIVE("ACTIVE"),
        ACTIVE_INCREASE("ACTIVE_INCREASE"),
        ACTIVE_INCREASE_MINED("ACTIVE_INCREASE_MINED"),
        ACTIVE_INCREASE_REST("ACTIVE_INCREASE_REST"),
        ACTIVE_MINING("ACTIVE_MINING"),
        ACTIVE_MINING_MINED("ACTIVE_MINING_MINED"),
        ACTIVE_MINING_REST("ACTIVE_MINING_REST"),
        FIXED("FIXED"),
        FIXED_SOLD("FIXED_SOLD"),
        FIXED_REST("FIXED_REST");
        private String value;

        PlatformAssertStatistics(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }

    /**
     * 首页交易记录类型
     */
    public enum DashboardTradeType {
        BUY(1),
        CONSUME(2),
        SELL(3);

        private int value;

        DashboardTradeType(Integer value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    }

    /**
     * 金额
     */
    public enum Currency {
        MIN(BigDecimal.valueOf(0)),
        MAX(BigDecimal.valueOf(999999999999.99));

        private BigDecimal value;

        Currency(BigDecimal value) {
            this.value = value;
        }

        public BigDecimal value() {
            return value;
        }
    }

    /**
     * 挖矿奖金类型
     */
    public enum MiningBonusType {
        PLATFORM_CURRENCY(1),
        POWER_POINT(2);

        private Integer value;

        MiningBonusType(Integer value) {
            this.value = value;
        }

        public Integer value() {
            return value;
        }
    }

    /**
     * 挖矿操作类型
     */
    public enum MiningActionType {
        READ_ARTICLE(1),
        REAL_NAME_CRT(2),
        SIGN_IN(3),
        ATTENTION_PUBLIC_NUMBER(4),
        INVITE(5),
        RED_PACKET(6),
        ANSWER(7),
        INCREASE(8),
        INVITEE_REG(9);

        private int value;

        MiningActionType(Integer value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    }


    /**
     * 商户分红状态
     */
    public enum MerchantDividendStatus {

        WAIT_DIVIDEND(1, "未到分红日"),
        ISSUE_DIVIDEND(2, "分红待发放"),
        SUCCESS_DIVIDEND(3, "已完成分红"),
        OVERDUE(4, "逾期");

        private int code;

        private String des;

        MerchantDividendStatus(int code, String des) {
            this.code = code;
            this.des = des;
        }

        public int code() {
            return code;
        }

        public String des() {
            return des;
        }

        public static MerchantDividendStatus[] dividendStatus = {WAIT_DIVIDEND, ISSUE_DIVIDEND, OVERDUE};
    }


    /**
     * 文章元素
     */
    public enum ArticleElement {
        TEXT(1),
        VIDEO(2),
        IMAGE(3);
        private int value;

        ArticleElement(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    }

    /**
     * 文章作者类型
     */
    public enum ArticleAuthorType {
        USER(1),
        OPERATOR(2);
        private int value;

        ArticleAuthorType(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    }


    /**
     * 兑换充值业务类型
     */
    public enum ExchangeChargingType {
        MOBILE_RECHARGE(1, 1),
        CNPC_RECHARGE(2, 1),
        CPDC_RECHARGE(2, 2);
        private int category;
        private int subCategory;

        ExchangeChargingType(int category, int subCategory) {
            this.category = category;
            this.subCategory = subCategory;
        }

        public int category() {
            return category;
        }

        public int subCategory() {
            return subCategory;
        }
    }


    /**
     * 兑换充值状态
     */
    public enum ExchangeChargingStatus {
        WAITING(1),
        FINISHED(2);
        private int value;

        ExchangeChargingStatus(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    }


    /**
     * 挖矿活动场次状态
     */
    public enum MiningActivityStatus {
        GENERAL(1),
        CANCELED(2);

        private int value;

        MiningActivityStatus(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    }

    /**
     * SQL错误码
     */
    public enum SQLErrorCode {
        INVALID_CHAR(1366);

        private int value;

        SQLErrorCode(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    }
}