package com.yz.aac.wallet;

import java.util.ArrayList;
import java.util.List;


public class Constants {
	

	/**
	 * 聚合数据（JUHE.CN）API错误参考码
	 *
	 */
	
	public enum JuheErrorCodeEnum {
		
		CODE_OK(0, "请求成功", 0),
	
		CODE_10001(10001, "错误的请求KEY", 101),
		
		CODE_10002(10002, "该KEY无请求权限", 102),
		
		CODE_10003(10003, "KEY过期", 103),
		
		CODE_10004(10004, "错误的OPENID", 104),
		
		CODE_10005(10005, "应用未审核超时，请提交认证", 105),
		
		CODE_10007(10007, "未知的请求源	", 107),
		
		CODE_10008(10008, "被禁止的IP", 108),
		
		CODE_10009(10009, "被禁止的KEY", 109),
		
		CODE_10011(10011, "当前IP请求超过限制", 111),
		
		CODE_10012(10012, "请求超过次数限制", 112),
		
		CODE_10013(10013, "测试KEY超过请求限制", 113),
		
		CODE_10014(10014, "系统内部异常", 114),
		
		CODE_10020(10020, "接口维护", 120),
		
		CODE_10021(10021, "接口停用", 121);
		
		JuheErrorCodeEnum(int errorCode, String instruction, int oldCode){
			this.errorCode = errorCode;
			this.instruction = instruction;
			this.oldCode = oldCode;
		}
		
		private int errorCode;
		
		private String instruction;
		
		private int oldCode;
		
		public int errorCode(){
			return this.errorCode;
		}
		
		public String instruction(){
			return this.instruction;
		}
		
		public int oldCode(){
			return this.oldCode;
		}
	 	
	}
	
	/**
	 * YUN XIN(SMS消息服务)API错误参考码
	 *
	 */

	public enum YunXinErrorCodeEnum {
		
		CODE_OK(200, "请求成功"),

		CODE_201(201, "客户端版本不对，需升级sdk"),
		
		CODE_301(301, "被封禁"),
		
		CODE_302(302, "用户名或密码错误"),
		
		CODE_315(315, "IP限制"),
		
		CODE_403(403, "非法操作或没有权限"),
		
		CODE_404(404, "对象不存在"),
		
		CODE_405(405, "参数长度过长"),
		
		CODE_406(406, "对象只读"),
		
		CODE_408(408, "客户端请求超时"),
		
		CODE_413(413, "验证失败(短信服务)"),
		
		CODE_414(414, "参数错误"),
		
		CODE_415(415, "客户端网络问题"),
		
		CODE_416(416, "频率控制"),
		
		CODE_417(417, "重复操作"),
		
		CODE_418(418, "通道不可用(短信服务)"),
		
		CODE_419(419, "数量超过上限"),
		
		CODE_422(422, "账号被禁用"),
		
		CODE_423(423, "帐号被禁言"),
		
		CODE_431(431, "HTTP重复请求"),
		
		CODE_500(500, "服务器内部错误"),
		
		CODE_503(503, "服务器繁忙"),
		
		CODE_508(508, "消息撤回时间超限"),
		
		CODE_509(509, "无效协议"),
		
		CODE_514(514, "服务不可用"),
		
		CODE_998(998, "解包错误"),
		
		CODE_999(999, "打包错误");
		
		
		YunXinErrorCodeEnum(int errorCode, String instruction){
			this.errorCode = errorCode;
			this.instruction = instruction;
		}
		
		private int errorCode;
		
		private String instruction;
		
		public int errorCode(){
			return this.errorCode;
		}
		
		public String instruction(){
			return this.instruction;
		}
		
		/**
	     * 根据key查询ThemeTypeEnum
	     * @return
	     */
	    public static YunXinErrorCodeEnum findEnumByCode(Integer code)  {
	        for (YunXinErrorCodeEnum em : YunXinErrorCodeEnum.values()) {
	            if (em.errorCode() == code.intValue()) {
	                return em;
	            }
	        }
	           throw new IllegalArgumentException("YunXinErrorCodeEnum has not this key k=" + code);
	    }
		
	}



    /**
     * 用户注册来源类型
     */
    public enum UserSourceType {

        RUNTINE_REGISTER(1, "常规注册用户"),
        ORDER_REGISTER(2, "挂单注册用户");

        private int code;
        
        private String des;

        UserSourceType(int code, String des) {
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
		SELL(2, "出售"),
		TRANSFER(3, "转账");

		private int code;

		private String des;

		MerchantTradeType(int code, String des) {
			this.code = code;
			this.des = des;
		}

		public int code() {
			return code;
		}

		public String des() {
			return des;
		}

		public static Integer[] getTransactionRecord (){
			return  new Integer[]{BUY.code, SELL.code};
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
		DIVIDEND(4, "分红"),
		EXCHANGE_CALLS(5, "兑换话费"),
		EXCHANGE_CARD(6, "兑换油卡");

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

		public static Integer[] getTransactionRecord (){
			return  new Integer[]{DEPOSIT.code, BUY.code, EXCHANGE_CALLS.code, EXCHANGE_CARD.code};
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
	 *
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

	}
	
	/**
	 * 用户生日登录错误次数配置
	 *
	 */
	public enum DefaultBirthdayEoorLoginNumEnum {

		NUM(5);

		private int code;

		DefaultBirthdayEoorLoginNumEnum(int code) {
			this.code = code;
		}

		public int code() {
			return code;
		}

	}
	
	/**
	 *用户账号状态 
	 *
	 */
	public enum UserAccountStatus {

		ENABLE(1, "启用"),
		DISCONTINUE_USE(2, "停用");

		private int code;
		
		private String message;

		UserAccountStatus(int code, String message) {
			this.code = code;
			this.message = message;
		}

		public int code() {
			return code;
		}
		
		public String message() {
			return message;
		}

	}

	/**
	 * 平台币交易状态
	 *
	 */
	public enum FreezeReasonType {
		TO_BE_TRANSFERRED(1, "发币押金"),
		FINISH(2, "挂单购买"),
		CANCEL(3, "挂单出售");

		private Integer code;

		private String des;

		FreezeReasonType(Integer code, String des) {
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
	 * 商户性别枚举
	 */
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
	}
	
	/**
	 * 手机验证码类型
	 *
	 */
	public enum SmsCodeType {

		LOGIN(1, "登录"),
		UP_PAS(2, "修改密码"),
		CERTIFICATION(3, "商户实名认证"),
		MINI_PROGRAM_SYN(4, "小程序同步");

		private int code;

		private String des;

		SmsCodeType(int code, String des) {
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
	public enum Other {
		PIE("枚"),
		SPEND("花费"),
		GAIN("获得"),
		FREEZE("冻结");
		private String value;

		Other(String value) {
			this.value = value;
		}

		public String value() {
			return value;
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
		
		public static Integer[] codes(){
			List<Integer> codes = new ArrayList<Integer>();
			for(MiningActionEnum en : MiningActionEnum.values()){
				codes.add(en.code);
			}
			return codes.toArray(new Integer[codes.size()]);
		}
		
	}

	/**
	 * 杂项
	 */
	public enum Misc {
		APP_DOWNLOAD_URI("http://132.232.205.107/apk/aab_1_0_0.apk");
		private String value;

		Misc(String value) {
			this.value = value;
		}

		public String value() {
			return value;
		}
	}

	/**
	 * 兑换服务大类
	 */
    public enum ExchangeMaxCategoryEnum {
		TELEPHONE(1, "话费"),
		OIL_CARD(2, "油卡");

        private Integer code;

        private String des;

		ExchangeMaxCategoryEnum(Integer code, String des) {
            this.code = code;
            this.des = des;
        }

        public Integer code() {
            return code;
        }

        public String des() {
            return des;
        }

        public static ExchangeMaxCategoryEnum getKey(Integer code){
			for (ExchangeMaxCategoryEnum eme : ExchangeMaxCategoryEnum.values()) {
				if (eme.code == code) {
					return eme;
				}
			}
			return null;
		}
    }

	/**
	 * 兑换服务小类
	 */
	public enum ExchangeMinCategoryEnum {
		MOBILE_PHONE(1, ExchangeMaxCategoryEnum.TELEPHONE.code,"移动电话"),
		CNPC(1, ExchangeMaxCategoryEnum.OIL_CARD.code,"中石化"),
		SINOPEC(2, ExchangeMaxCategoryEnum.OIL_CARD.code,"中石油");

        private Integer code;

        private Integer parentCode;

        private String des;

		ExchangeMinCategoryEnum(Integer code, Integer parentCode,String des) {
            this.code = code;
            this.parentCode = parentCode;
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
	 * 充值兑换充值选项类型
	 */
	public enum ExchangeRechargeTypeEnum {
		OPTION(1, "选项卡充值"),
		MANUAL(2, "手动充值");

		private Integer code;

		private String des;

		ExchangeRechargeTypeEnum(Integer code, String des) {
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
	 * 兑换类型
	 */
	public enum ExchangeTypeEnum {
		TELEPHONE_BILL(1, "兑换话费"),
		CNPC(2, "中石油兑换"),
		SINOPEC(3, "中石化兑换");

		private Integer code;

		private String des;

		ExchangeTypeEnum(Integer code, String des) {
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
	 * 兑换充值状态
	 */
	public enum ExchangeRecordStatusEnum {
        RECHARGE_IN(1, "待充值"),
        RECHARGE_Y(2, "充值完成");

		private Integer code;

		private String des;

		ExchangeRecordStatusEnum(Integer code, String des) {
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
	 * 兑换服务开通状态
	 */
	public enum ExchangeActivatedStatusEnum {
		OPENING(1, "开通"),
		NOT_OPENED(2, "未开通");

		private Integer code;

		private String des;

		ExchangeActivatedStatusEnum(Integer code, String des) {
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
		TRANSFER_ACCOUNTS(5, "购买平台币转账（出帐）"),
		RECHARGE_TELEPHONE_IN(6, "充值话费（平台币进账）"),
		RECHARGE_CNPC_IN(7, "充值中石油油卡（平台币进账）"),
		RECHARGE_SINOPEC_IN(8, "充值中石化油卡（平台币进账）"),
		RECHARGE_TELEPHONE_OUT(9, "充值话费（法币出账）"),
		RECHARGE_CNPC_OUT(10, "充值中石油油卡（法币出账）"),
		RECHARGE_TSINOPEC_OUT(11, "充值中石化油卡（法币出账）");

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

}