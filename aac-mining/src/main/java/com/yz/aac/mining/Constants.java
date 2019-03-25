package com.yz.aac.mining;

import java.util.ArrayList;
import java.util.List;

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
    * 用户挖矿记录奖励类型
    *
    */
	public enum MiningBonusTypeEnum {
		PLATFORM_CURRENCY(1, "平台币"),
		POWER_POINT(2, "元力");

		private Integer code;

		private String des;

		MiningBonusTypeEnum(Integer code, String des) {
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
	 * 
	 * 用户挖矿记录类型
	 *
	 */
	public enum MiningActionEnum {
		READ(1, "阅读"),
		CERTIFICATION(2, "实名认证"),
		SIGN(3, "签到"),
		FOCUS_ON_WECHAT(4, "关注公众号"),
		INVITE_FRIENDS(5, "邀请好友"),
		RED_LETTER(6, "红信"),
		ANSWER(7, "答题"),
		NATURAL_GROWTH(8, "自然增长"),
		INVITATION_CODE_REGISTER(9, "邀请码注册"),
		MINING(10, "挖"),
		POSTING(11, "发帖");

		private Integer code;

		private String des;

		MiningActionEnum(Integer code, String des) {
			this.code = code;
			this.des = des;
		}

		public Integer code() {
			return code;
		}

		public String des() {
			return des;
		}
		
		public static String getValue(Integer code){
			for(MiningActionEnum en : MiningActionEnum.values()){
				if (code.equals(en.code)) {
					return en.des;
				}
			}
			return "";
		}
		
		public static Integer[] codes(){
			List<Integer> codes = new ArrayList<Integer>();
			for(MiningActionEnum en : MiningActionEnum.values()){
				codes.add(en.code);
			}
			return codes.toArray(new Integer[codes.size()]);
		}
		
	}
	
	/**
	 * 简介文本类型
	 *
	 */
	public enum BriefTextKeyEnum {
		ELEMENTARY_FORCE("元力简介"),
		PLATFORM_CURRENCY("平台币简介"),
		FOCUS_ON_WECHAT("关注公众号"),
		ANSWER_GAME("答题游戏规则"),
		TIPS("红包小贴士"),
		ARTICLE_AWARD("资讯文章奖励规则");

		private String des;

		BriefTextKeyEnum(String des) {
			this.des = des;
		}

		public String des() {
			return des;
		}
		
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
		RMB_EXCHANGE_RATE(ParamConfigLargeClassEnum.TRANSACTION.code, 2, "平台币 => 法币兑换率"),
		READING_TIME(ParamConfigLargeClassEnum.MINING.code, 1, "阅读时长（单位：秒）"),
		MAX_READING_PER_DAY(ParamConfigLargeClassEnum.MINING.code, 1, "每天最大阅读奖励次数"),
		READING_CURRENCY(ParamConfigLargeClassEnum.MINING.code, 1, "每次阅读奖励金额"),
		MIN_ARTICLE_LENGTH(ParamConfigLargeClassEnum.MINING.code, 1, "文章最少字数"),
		MAX_PUBLISHING_PER_DAY(ParamConfigLargeClassEnum.MINING.code, 1, "每天最大发帖奖励次数"),
		PUBLISHING_CURRENCY(ParamConfigLargeClassEnum.MINING.code, 1, "每次发帖奖励金额");

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
		SIGN_IN("签到次数"),
		ANSWER("答题次数"),
		ATTENTION_PUBLIC_NUMBER("关注公众号次数"),
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
	 * 账号状态
	 *
	 */
	public enum AccountStatus {

		ENABLE(1, "启用"),
		BLOCK_UP(2, "停用");

        private int code;
        
        private String des;

        AccountStatus(int code, String des) {
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
     * 平台资产统计
     */
    public enum PlatformAssertStatistics {
        UPDATE_TIME("更新时间"),
        TOTAL("资产总量"),
        ACTIVE("活跃总量"),
        ACTIVE_INCREASE("活跃自然增长量"),
        ACTIVE_INCREASE_MINED("活跃自然增长被挖量"),
        ACTIVE_INCREASE_REST("活跃自然增长剩余量"),
        ACTIVE_MINING("活跃挖矿量"),
        ACTIVE_MINING_MINED("活跃挖矿被挖量"),
        ACTIVE_MINING_REST("活跃挖矿剩余量"),
        FIXED("固定总量"),
        FIXED_SOLD("固定已售出量"),
        FIXED_REST("固定剩余量");
        private String value;

        PlatformAssertStatistics(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }
    
    /**
     * 签到相关枚举
     */
    public enum SignInEnum{
    	TOTAL_DAY(7, "签到有效天数轮询");

        private int code;
        
        private String des;

        SignInEnum(int code, String des) {
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
	 * 游戏类型列表
	 *
	 */
	public enum GameTagEnum {

		ANSWER(1 ,"答题", "题", "每日答题挑战%d/%d", "每日%s次，答题赢元力", "去答题"),
		FOLLOW(2 ,"关注公众号", "关", "关注公众号%d/%d", "一人仅一次，可得%d元力值", "去关注"),
		SIGN_IN(3 ,"签到", "签", "签到%d/%d", "每日签到，每周得%d元力", "签到"),
		AUTHENTICATION(4 ,"实名认证", "证", "认证%d/%d", "一人仅限一次，可得%d%s", "实名认证");

		private Integer key;
		
		private String code;
		
		private String type;
		
		private String ti;
		
		private String des;
		
		private String but;

		GameTagEnum(Integer key, String code, String type, String ti, String des, String but) {
			this.key = key;
			this.code = code;
			this.type = type;
			this.ti = ti;
			this.des = des;
			this.but = but;
		}

		public Integer key() {
			return key;
		}
		
		public String code() {
			return code;
		}
		
		public String type() {
			return type;
		}
		
		public String ti() {
			return ti;
		}
		
		public String des() {
			return des;
		}
		
		public String but() {
			return but;
		}

	}
	
	/**
	 * 红包互动类型
	 *
	 */
	public enum PacketActionType {

		COMMENT(1, "评论"),
		LIKE(2, "点赞"),
		LINK(3, "点击链接");
		
		private Integer code;

		private String message;

		PacketActionType(Integer code, String message) {
			this.code = code;
			this.message = message;
		}

		public Integer code() {
			return code;
		}
		
		public String message() {
			return message;
		}

	}
	
	/**
	 * 红包状态
	 *
	 */
	public enum PacketStatusEnum {

		IN_DISTRIBUTION(1, "派发中"),
		END_OF_COLLECTION(2, "领取完毕");
		
		private Integer code;

		private String message;

		PacketStatusEnum(Integer code, String message) {
			this.code = code;
			this.message = message;
		}

		public Integer code() {
			return code;
		}
		
		public String message() {
			return message;
		}

	}
	
	/**
	 * 红包领取状态
	 *
	 */
	public enum PacketReceivingStatusEnum {

		HAVE_RECEIVED(1, "已领取"),
		UNCOLLECTED(2, "未领取，红包派发进行中"),
		UNCOLLECTED_END(3, "未领取，红包派发结束");
		
		private Integer code;

		private String message;

		PacketReceivingStatusEnum(Integer code, String message) {
			this.code = code;
			this.message = message;
		}

		public Integer code() {
			return code;
		}
		
		public String message() {
			return message;
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
	
	public enum OrtherEnum {

		REDUCED_UNIT("111", "经纬度-千米换算单位"),
		CHINA_LON_MIN("73.66" , "国内经度最小值"),
		CHINA_LON_MAX("135.05", "国内经度最大值"),
		CHINA_LAT_MIN("3.86", "国内纬度最小值"),
		CHINA_LAT_MAX("53.55", "国内纬度最大值");

		private String code;

		private String des;

		OrtherEnum(String code, String des) {
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

	public enum MerchantGenderType {

		MAN(1, "男"),
		WOMAN(2, "女");

		private int code;

		private String des;

		MerchantGenderType(int code, String des) {
			this.code = code;
			this.des = des;
		}

		public int code() {
			return code;
		}

		public String des() {
			return des;
		}

		public static MerchantGenderType getKey(int code){
			for (MerchantGenderType mgt : MerchantGenderType.values()) {
				if (code == mgt.code) {
					return mgt;
				}
			}
			return null;
		}
	}

	/**
	 * 文章标签
	 */
	public enum ArticleLabelEnum {

		ORIGINAL(1, "原创");

		private int code;

		private String des;

		ArticleLabelEnum(int code, String des) {
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
	 * 文章元素类型
	 */
	public enum ArticleElementType {

		TEXT(1, "文字"),
		VIDEO(2, "视频"),
		IMG(3, "图片");

		private int code;

		private String des;

		ArticleElementType(int code, String des) {
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
	 * 文章互动类型
	 */
	public enum ArticleActionType {

		COMMENT(1, "评论"),
		LIKE(2, "点赞"),
		READING(3, "阅读");

		private Integer code;

		private String message;

		ArticleActionType(Integer code, String message) {
			this.code = code;
			this.message = message;
		}

		public Integer code() {
			return code;
		}

		public String message() {
			return message;
		}

	}

	/**
	 * 文章个性化类型
	 */
	public enum ArticlePersonalType {

		FOLLOW(1, "关注"),
		DONT_SEE(2, "不想看");

		private Integer code;

		private String message;

		ArticlePersonalType(Integer code, String message) {
			this.code = code;
			this.message = message;
		}

		public Integer code() {
			return code;
		}

		public String message() {
			return message;
		}

	}


	/**
	 * 挖矿活动状态
	 */
	public enum MiningActivityStatus {

		NORMAL(1, "正常"),
		TERMINATED(2, "已终止");

		private Integer code;

		private String message;

		MiningActivityStatus(Integer code, String message) {
			this.code = code;
			this.message = message;
		}

		public Integer code() {
			return code;
		}

		public String message() {
			return message;
		}

	}

	/**
	 * 资讯文章默认类型
	 */
	public enum ArticleDefaultCategoryEnum {

		RECOMMENDATION(-1, "推荐"),
		FOLLOW(-2, "关注");

		private Integer code;

		private String message;

		ArticleDefaultCategoryEnum(Integer code, String message) {
			this.code = code;
			this.message = message;
		}

		public Integer code() {
			return code;
		}

		public String message() {
			return message;
		}

	}
	/**
	 * 默认文章推荐条数
	 */
	public enum ArticleDefaultReadCountEnum {

		MAX_NUM(200, "获取最大推荐阅读条数");

		private Integer code;

		private String message;

		ArticleDefaultReadCountEnum(Integer code, String message) {
			this.code = code;
			this.message = message;
		}

		public Integer code() {
			return code;
		}

		public String message() {
			return message;
		}

	}

	/**
	 * 发布者类型
	 */
	public enum ArticleAuthorTypeEnum {

		APP(1, "APP用户"),
		OPERATE(2, "运营人员");

		private Integer code;

		private String message;

		ArticleAuthorTypeEnum(Integer code, String message) {
			this.code = code;
			this.message = message;
		}

		public Integer code() {
			return code;
		}

		public String message() {
			return message;
		}

	}

}
