package com.yz.aac.exchange;


public class Constants {
	
    /**
     * 常用状态枚举
     *
     */
    public enum StateType {

        OK_STATE(1, "是"),
        NO_STATE(2, "否");

        private int code;
        
        private String des;

        StateType(int code, String des) {
            this.code = code;
            this.des = des;
        }

        public int code() {
            return code;
        }
        
        public String des() {
            return des;
        }
    }
    
    /**
     * 商户发币审批状态
     *
     */
	public enum IssuanceAuditStatus{

		QUAL_NO(1, "已发币，资格资格审核"),
		QUAL_YES(2, "资格审核通过，待付押金"),
		DEPOSIT_VERIFY(3, "押金已付，待审核"),
		DEPOSIT_NO(4, "押金审核失败"),
		DEPOSIT_YES(5, "押金审核通过"),
		QUAL_FAIL(6, "已发币，资格审核失败");

		private int code;

		private String des;

		IssuanceAuditStatus(int code, String des) {
			this.code = code;
			this.des = des;
		}

		public int code() {
			return code;
		}

		public String des() {
			return des;
		}
	}

	/**
	 * 交易记录-交易类型
	 */
	public enum MerchantTradeType {

		BUY(1, "购买"),
		SELL(2, "卖出"),
		TRANSFER(3, "转账");

		private Integer code;

		private String des;

		MerchantTradeType(Integer code, String des) {
			this.code = code;
			this.des = des;
		}

		public Integer code() {
			return code;
		}

		public String des() {
			return des;
		}
	}
	
	/**
	 * 平台币交易类型
	 *
	 */
	public enum PlatformAssertTradeType {
		DEPOSIT(1, "押金"),
		BUY(2, "买入"),
		TRANSFER(3, "转账"),
		DIVIDEND(4, "分红");

		private Integer code;

		private String des;

		PlatformAssertTradeType(Integer code, String des) {
			this.code = code;
			this.des = des;
		}

		public Integer code() {
			return code;
		}

		public String des() {
			return des;
		}
	}
	
	/**
	 * 用户挂单状态
	 */
	public enum UserOrderStatus {

		UPPER_SHELF(1, "上架"),
		LOWER_SHELF(2, "下架");

		private Integer code;

		private String des;

		UserOrderStatus(Integer code, String des) {
			this.code = code;
			this.des = des;
		}

		public Integer code() {
			return code;
		}

		public String des() {
			return des;
		}
	}
	
	/**
	 * 用户冻结资产类型
	 */
	public enum UserRreezeReason {

		CASH_DEPOSIT(1, "发币押金"),
		PURCHASE_LIST(2, "挂单购买"),
		SALE_OF_BILLS(3, "挂单出售");

		private Integer code;

		private String des;

		UserRreezeReason(Integer code, String des) {
			this.code = code;
			this.des = des;
		}

		public Integer code() {
			return code;
		}

		public String des() {
			return des;
		}
	}
	
	/**
	 * 平台币交易状态
	 *
	 */
	public enum PlatformAssertTradeExamineType {
		TO_BE_TRANSFERRED(1, "后台待转账"),
		FINISH(2, "已完成"),
		CANCEL(3, "已取消");

		private Integer code;

		private String des;

		PlatformAssertTradeExamineType(Integer code, String des) {
			this.code = code;
			this.des = des;
		}

		public Integer code() {
			return code;
		}

		public String des() {
			return des;
		}
	}
	
	/**
     * 商户最新统计信息
     */
    public enum MerchantAssertLatestDataCountTypeEnum {
        DAY(1,"日"),
        WEEK(2,"周"),
		MONTH(3,"月");

        private Integer code;
        private String des;

		MerchantAssertLatestDataCountTypeEnum(Integer code,String des) {
            this.code = code;
            this.des = des;
        }
		public Integer code() {
			return code;
		}

		public String des() {
			return des;
		}
    }
    
    /**
     * 平台币进出账类型
     *
     */
    public enum PlatformCurrencyDirectionEnum {
    	ACCOUNT_ADVANCE(1,"进账"),
    	OUT_OF_ACCOUNT(2,"出账");

        private Integer code;
        private String des;

        PlatformCurrencyDirectionEnum(Integer code,String des) {
            this.code = code;
            this.des = des;
        }
		public Integer code() {
			return code;
		}

		public String des() {
			return des;
		}
    }
    
    /**
     * 平台币交易类型
     *
     */
    public enum PlatformCurrencyAtransactionction {
    	RECHARGE(1, "充值平台币（进账）"),
		CASH_DEPOSIT(2, "发币押金（进账）"),
		TRANSACTION_CHARGES(3, "交易手续费（进账）"),
		ADVERTISING_FEE(4, "广告费（进账）"),
		TRANSFER_ACCOUNTS(5, "购买平台币转账（出帐）");

		private Integer code;

		private String des;

		PlatformCurrencyAtransactionction(Integer code, String des) {
			this.code = code;
			this.des = des;
		}

		public Integer code() {
			return code;
		}

		public String des() {
			return des;
		}
	}

	/**
	 * 商户资产统计相关枚举
	 */
	public enum MerchantAssertStatisticsKey {

		UNRESTRICTED("是否已过限售期"),
		TRADED("交易次数"),
		MINING_MIND("被挖量"),
		MINING_REST("剩余可挖量"),
		SELL_SOLD("已售出量"),
		SELL_REST("剩余可售量");

		private String message;

		MerchantAssertStatisticsKey(String message) {
			this.message = message;
		}

		public String message() {
			return message;
		}

		public static MerchantAssertStatisticsKey[] liquidity = { MINING_MIND, SELL_SOLD };

	}

	/**
	 * 后台系统配置大类
	 *
	 */
	public enum ParamConfigLargeClassEnum {
		MINING(1, "挖矿"),
		TRANSACTION(2, "交易");

		private Integer code;

		private String des;

		ParamConfigLargeClassEnum(Integer code, String des) {
			this.code = code;
			this.des = des;
		}

		public Integer code() {
			return code;
		}

		public String des() {
			return des;
		}
	}
	
	/**
	 * 后台系统配置小类
	 *
	 */
	public enum ParamConfigSubclassEnum {
		REAL_NAME_VERIFICATION_CURRENCY(ParamConfigLargeClassEnum.MINING.code, 2, "实名认证奖励平台币数"),
		SING_IN_POINT(ParamConfigLargeClassEnum.MINING.code, 3, "每日签到奖励元力数"),
		ATTENTION_PUBLIC_NUMBER_POINT(ParamConfigLargeClassEnum.MINING.code, 4, "关注公众号奖励元力数"),
		MAX_INVITATION_PER_DAY(ParamConfigLargeClassEnum.MINING.code, 5, "每日邀请好友数最大值"),
		INVITATION_CURRENCY(ParamConfigLargeClassEnum.MINING.code, 5, "每邀请一位好友奖励平台币数"),
		MINING_ROYALTY_RATE(ParamConfigLargeClassEnum.MINING.code, 5, "好友挖矿分成比例，千分之几"),
		GRAB_RED_PACKET_PER_DAY(ParamConfigLargeClassEnum.MINING.code, 6, "每日可抢红包数"),
		MAX_PARTICIPATION_PER_DAY(ParamConfigLargeClassEnum.MINING.code, 7, "每日参与答题机会数"),
		QUESTIONS_PER_PARTICIPATION(ParamConfigLargeClassEnum.MINING.code, 7, "每次参与题目数"),
		PRICE_FLOATING_RATE(ParamConfigLargeClassEnum.TRANSACTION.code, 1, "卖单单价浮动率，百分之几"),
		RMB_EXCHANGE_RATE(ParamConfigLargeClassEnum.TRANSACTION.code, 2, "平台币 => 法币兑换率");

		private Integer maxCode;
		
		private Integer minCode;

		private String des;

		ParamConfigSubclassEnum(Integer maxCode, Integer minCode, String des) {
			this.maxCode = maxCode;
			this.minCode = minCode;
			this.des = des;
		}

		public Integer maxCode() {
			return maxCode;
		}
		
		public Integer minCode() {
			return minCode;
		}

		public String des() {
			return des;
		}
	}
	
	/**
	 * 用户行为统计KEY
	 */
	public enum UserBehaviourStatisticsKey {

		TRADE("交易次数"),
		SING_IN("签到次数"),
		ANSWER("答题次数"),
		ATTENTION_PUBLIC_NUUMBER("关注公众号次数"),
		READ("阅读文章次数"),
		SEND_RED_PACKET("发红包次数"),
		GRAB_RED_PACKET("抢到红包次数"),
		PLAY_GAME("玩游戏次数"),
		MINING_EVENT("挖矿活动参加次数"),
		INVITE("邀请好友次数"),
		POST_COMMENT("发帖次数"),
		CLICK_AD("有效点击广告次数"),
		SUBMIT_AD_FORM("广告表单提交次数");

		private String message;

		UserBehaviourStatisticsKey(String message) {
			this.message = message;
		}

		public String message() {
			return message;
		}

	}
	
	/**
	 * 用户平台币收入统计类型
	 *
	 */
	public enum UserIncomeType {
		RECHARGE("RECHARGE", "充值收入总计"),
		SELL("SELL", "卖出商家币收入总计"),
		TRANSFER("TRANSFER", "转账收入总计"),
		MINING("MINING", "挖矿收入总计");

		private String code;

		private String des;

		UserIncomeType(String code, String des) {
			this.code = code;
			this.des = des;
		}

		public String code() {
			return code;
		}

		public String des() {
			return des;
		}
	}

	/**
	 * 商户交易记录-交易方式
	 */
	public enum MerchantTradeRecordTransactionModeEnum {
		APPLET_RECHARGE(1,"小程序充值",MerchantTradeRecordAddSubtractEnum.SUBTRACT,MerchantTradeRecordFlowDirectionEnum.MARKET_LIQUIDITY),
		PERSONAL_TRANSFER(2,"个人转币",MerchantTradeRecordAddSubtractEnum.SUBTRACT,MerchantTradeRecordFlowDirectionEnum.MARKET_LIQUIDITY),
		RELEASE_SELL_​​ORDER(3,"挂卖单",MerchantTradeRecordAddSubtractEnum.SUBTRACT,MerchantTradeRecordFlowDirectionEnum.ACCOUNT_FREEZE_STOCK),
		CANCEL_SELL_​​ORDER(4,"取消卖单",MerchantTradeRecordAddSubtractEnum.ADD,MerchantTradeRecordFlowDirectionEnum.ACCOUNT_FREEZE_STOCK),
		STORE_CONSUME(5,"店内消费",MerchantTradeRecordAddSubtractEnum.ADD,MerchantTradeRecordFlowDirectionEnum.MARKET_LIQUIDITY),
		BUY_​​ORDER_CLINCH(6,"挂买单成交",MerchantTradeRecordAddSubtractEnum.ADD,MerchantTradeRecordFlowDirectionEnum.MARKET_LIQUIDITY),
		REPURCHASE(7,"直接回购",MerchantTradeRecordAddSubtractEnum.ADD,MerchantTradeRecordFlowDirectionEnum.MARKET_LIQUIDITY),
		PERSONAL_MINING(8,"个人挖矿",MerchantTradeRecordAddSubtractEnum.ADD,MerchantTradeRecordFlowDirectionEnum.ACCOUNT_MINING_STOCK);

		private int code;

		private String des;

		private MerchantTradeRecordAddSubtractEnum addSubtractEnum;

		private MerchantTradeRecordFlowDirectionEnum flowDirectionEnum;

		MerchantTradeRecordTransactionModeEnum(int code, String des,MerchantTradeRecordAddSubtractEnum addSubtractEnum, MerchantTradeRecordFlowDirectionEnum flowDirectionEnum) {
			this.code = code;
			this.des = des;
			this.addSubtractEnum = addSubtractEnum;
			this.flowDirectionEnum = flowDirectionEnum;
		}

		public int code() {
			return code;
		}

		public String des() {
			return des;
		}

		public MerchantTradeRecordAddSubtractEnum addSubtractEnum() {
			return addSubtractEnum;
		}

		public MerchantTradeRecordFlowDirectionEnum flowDirectionEnum() {
			return flowDirectionEnum;
		}
	}

	/**
	 * 商户交易记录-流通方向
	 */
	public enum MerchantTradeRecordFlowDirectionEnum {

		ACCOUNT_FREEZE_STOCK(1,"账户冻结存量"),
		MARKET_LIQUIDITY(2,"市场流通量"),
		ACCOUNT_MINING_STOCK(3,"账户挖矿存量");

		private int code;

		private String des;

		MerchantTradeRecordFlowDirectionEnum(int code, String des) {
			this.code = code;
			this.des = des;
		}

		public int code() {
			return code;
		}

		public String des() {
			return des;
		}
	}

	/**
	 * 商户交易记录-流失增长
	 */
	public enum MerchantTradeRecordAddSubtractEnum {

		SUBTRACT(1,"流失"),
		ADD(2,"增长");

		private int code;

		private String des;

		MerchantTradeRecordAddSubtractEnum(int code, String des) {
			this.code = code;
			this.des = des;
		}

		public int code() {
			return code;
		}

		public String des() {
			return des;
		}
	}

	/**
	 * 商户货币统计-资产类型
	 */
	public enum MerchantCurrencyStatisticsAssetTypeEnum {

		DYNAMIC_STOCK(1,"活跃存量"),
		FREEZE_STOCK(2,"冻结存量"),
		MINING_STOCK(3,"挖矿存量");

		private int code;

		private String des;

		MerchantCurrencyStatisticsAssetTypeEnum(int code, String des) {
			this.code = code;
			this.des = des;
		}

		public int code() {
			return code;
		}

		public String des() {
			return des;
		}
	}

	/**
	 * 商户分红状态
	 */
	public enum MerchantDividendStatus {

		WAIT_DIVIDEND(1,"未到分红日"),
		ISSUE_DIVIDEND(2,"分红待发放"),
		SUCCESS_DIVIDEND(3,"已完成分红"),
		OVERDUE(4,"逾期");

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

		public static MerchantDividendStatus[] dividendStatus = { WAIT_DIVIDEND, ISSUE_DIVIDEND, OVERDUE };
	}

	/**
	 * 商户分红状态
	 */
	public enum MerchantOrderSource {

		SOURCE_APP(1,"APP"),
		SOURCE_APPLETS(2,"小程序");

		private int code;

		private String des;

		MerchantOrderSource(int code, String des) {
			this.code = code;
			this.des = des;
		}

		public int code() {
			return code;
		}

		public String des() {
			return des;
		}
	}

	/**
	 * 杂项
	 */
	public enum Misc {
		TWO_MONTHS_FOR_DAY("61"),
		ONE_YEAR_FOR_WEEK("53"),
		K_CHART_PATH(":8085/#/kChart?date=day&symbol=");

		private String value;

		Misc(String value) {
			this.value = value;
		}

		public String value() {
			return value;
		}
	}
}
