package com.yz.aac.common;

import static org.springframework.http.HttpStatus.*;

public class Constants {

    /**
     * 响应状态信息
     */
    public enum ResponseStatusInfo {

        HTTP_OK(OK.value()),
        HTTP_UNAUTHORIZED(UNAUTHORIZED.value()),
        HTTP_BAD_REQUEST(BAD_REQUEST.value()),
        HTTP_INTERNAL_SERVER_ERROR(INTERNAL_SERVER_ERROR.value());

        private int code;

        ResponseStatusInfo(int code) {
            this.code = code;
        }

        public int code() {
            return code;
        }
    }

    /**
     * 响应消息信息
     */
    public enum ResponseMessageInfo {

        MSG_SUCCESS("MSG_1001", "处理成功"),
        MSG_UNAUTHORIZED("MSG_2001", "请求未授权"),
        MSG_REQUIRED_FILED("MSG_3001", "字段 [{}] 必须传入值"),
        MSG_INVALID_FILED("MSG_3002", "字段 [{}] 传入了无效值：[{}]"),
        MSG_INVALID_FILED_LENGTH("MSG_3003", "字段 [{}] 长度须保持在 [{}] 至 [{}] 之间]"),
        MSG_INVALID_TIME_PERIOD("MSG_3004", "无效的起止时间：字段[{}]，[{}]"),
        MSG_INVALID_CHAR("MSG_3005", "请勿包含特殊字符：字段[{}]，值[{}]"),
        MSG_INVALID_IMAGE("MSG_3006", "请上传有效图片：字段[{}]"),
        MSG_INVALID_RANGE("MSG_3007", "无效的起止范围：字段[{}]，[{}]"),
        MSG_ONE_REQUIRED("MSG_3008", "请至少输入一项"),
        MSG_INVALID_UPLOAD("MSG_3009", "单个附件大小请控制在[{}]以内；单次请求附件总大小请控制在[{}]以内"),
        MSG_CUSTOMIZED_EXCEPTION("MSG_4001", null), //普通业务异常
        MSG_INTERACTIVE_EXCEPTION("MSG_4002", null), //特殊业务异常，客户端需要根据异常作相应跳转时使用此code
        MSG_SERVER_EXCEPTION("MSG_5001", "服务器内部错误！类型：[{}]；信息：[{}]");

        private String code;
        private String message;

        ResponseMessageInfo(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String code() {
            return code;
        }

        public String message() {
            return message;
        }
    }

    /**
     * TOKEN
     */
    public enum Token {
        TOKEN_KEY("token"),
        TTL_MILLIS("300000"),
        TOKEN_CLAIM_LOGIN_ID("loginId"),
        TOKEN_CLAIM_ROLE_IDS("roleIds");
        private String value;

        Token(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }

    /**
     * 外部服务编号
     */
    public enum ExternalServiceNumber {
    	LOCAL_APP(0),//本地APP来源
        APPLETS_SERVICE(-1),//小程序服务器
        CATERING_SERVICE(-2);//餐饮服务器

        private int code;

        public int code() {
            return code;
        }

        ExternalServiceNumber(int code) {
            this.code = code;
        }
    }

    /**
     * 杂项
     */
    public enum Misc {
        DOT("."),
        PLACEHOLDER("\\{\\}"),
        USER_CODE_PREFIX("AAB-"),
        MERCHANT_CODE_PREFIX("M-"),
        PLATFORM_CURRENCY_SYMBOL("AAB"),
        LEGAL_CURRENCY_SYMBOL("RMB"),
        REQUEST_INFO_KEY("com.yz.aac.loginInfo"),
        PLATFORM_CURRENCY_EXCHANGE_RATE("1"),   //平台币汇率（每一个平台币兑换多少法币）
        PLATFORM_CURRENCY_TOTAL_CIRCULATION("1000000000"),   //平台币总发行量
        API_DOC_TITLE("AAC 接口规范");
        private String value;

        Misc(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }
}