package com.yz.aac.mining.util;

import com.yz.aac.common.exception.SerializationException;

public final class DigitalConversionUtil {

	public  final static String[] NUMBER_STRING = new String[]{"零", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十"};

	/**
	 * 根据数字返回大写数字字符串（最大为十）
	 * @param number
	 * @return
	 * @throws SerializationException
	 */
	public static String numberCapitalization(Integer number) throws SerializationException {
		if ((number + "").length() == 1) {
			if (number > NUMBER_STRING.length) {
				return NUMBER_STRING[NUMBER_STRING.length - 1];
			}
			return NUMBER_STRING[number];
		} else {
			return NUMBER_STRING[NUMBER_STRING.length - 1];
		}
		
	}
}
