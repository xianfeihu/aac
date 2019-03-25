package com.yz.aac.wallet.util;

import com.github.pagehelper.util.StringUtil;
import com.yz.aac.common.exception.SerializationException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegularUtil {

    public  final static int CHINA_ID_MIN_LENGTH = 15; //身份证号码最小长度
    public  final static int CHINA_ID_MAX_LENGTH = 18; //公民身份证号码最大长度

    private static final Pattern CHECK_REGULAR = Pattern.compile("(\\d{17}[0-9a-zA-Z]|\\d{14}[0-9a-zA-Z])");//身份证号码正则
    private static final Pattern EXTRACT_REGULAR_MAX = Pattern.compile("\\d{6}(\\d{8}).*"); // 用于18位身份证提取出生日字符串
    private static final Pattern EXTRACT_REGULAR_MIN = Pattern.compile("\\d{6}(\\d{6}).*"); // 用于15位身份证提取出生日字符串

    /**
     * 正则：手机号（精确）
     * <p>移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188、198</p>
     * <p>联通：130、131、132、145、155、156、175、176、185、186、166</p>
     * <p>电信：133、153、173、177、180、181、189、199</p>
     * <p>全球星：1349</p>
     * <p>虚拟运营商：170</p>
     */
    public static final String REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$";
    
    /**
     * 身份证长度验证
     * @param idCard 身份证
     * @return
     */
	public static boolean IdCardLen(String idCard) throws SerializationException {
		Matcher checkMa = CHECK_REGULAR.matcher(idCard);
		if (checkMa.matches()) {
			return true;
		}

		return false;
	}
	
	/**
	 *提取身份证出生日
	 * @param idCard 身份证
	 * @return
	 * @throws SerializationException
	 */
	public static String extractYTD(String idCard) throws SerializationException {
		if (!"".equals(idCard) && null != idCard) {
			String prefix = "";
			Pattern defaultPatt = EXTRACT_REGULAR_MAX;
			if (CHINA_ID_MIN_LENGTH == idCard.length()) {
				defaultPatt = EXTRACT_REGULAR_MIN;
				prefix = "19";
			}
			Matcher extractMa = defaultPatt.matcher(idCard);
			if (extractMa.find()) {
				return prefix + extractMa.group(1);
			}
		}
		
		throw new SerializationException("身份证号码有误！");
	}
	
	/**
	 * 手机号验证
	 * @param phone 手机号
	 * @return
	 * @throws SerializationException
	 */
	public static boolean phoneVerification(String phone) throws SerializationException {
		if (null == phone || "".equals(phone)) {
			return false;
			}
			Pattern pattern = Pattern.compile(REGEX_MOBILE_EXACT);
			Matcher matcher = pattern.matcher(phone);
			boolean b = matcher.matches();
			return b;
	}

	/**
	 * 中石油卡号长度校验
	 * @param cardCode
	 * @return
	 * @throws SerializationException
	 */
	public static boolean cnpcCardVerification(String cardCode) throws SerializationException {
		if (StringUtil.isEmpty(cardCode)) {
			return false;
		}
		Pattern pattern = Pattern.compile("(\\d{15}[0-9])");
		Matcher matcher = pattern.matcher(cardCode);
		boolean b = matcher.matches();
		return b;
	}

	/**
	 * 中石化卡号长度校验
	 * @param cardCode
	 * @return
	 * @throws SerializationException
	 */
	public static boolean sinopecCardVerification(String cardCode) throws SerializationException {
		if (StringUtil.isEmpty(cardCode)) {
			return false;
		}
		Pattern pattern = Pattern.compile("(\\d{18}[0-9])");
		Matcher matcher = pattern.matcher(cardCode);
		boolean b = matcher.matches();
		return b;
	}

}
