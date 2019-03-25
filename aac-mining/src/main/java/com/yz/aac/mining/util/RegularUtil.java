package com.yz.aac.mining.util;

import com.yz.aac.mining.Constants.OrtherEnum;


public final class RegularUtil {

	/** 
	 * 
	 * * 经纬度校验 
	 * * 经度longitude: (?:[0-9]|[1-9][0-9]|1[0-7][0-9]|180)\\.([0-9]{6}) 
	 * * 纬度latitude： (?:[0-9]|[1-8][0-9]|90)\\.([0-9]{6}) 
	 * * @return 
	 * */ 
	public static boolean checkItude(String longitude,String latitude){ 
		if (Float.valueOf(longitude) < Float.valueOf(OrtherEnum.CHINA_LON_MIN.code()) 
				|| Float.valueOf(longitude) > Float.valueOf(OrtherEnum.CHINA_LON_MAX.code())
				|| Float.valueOf(latitude) < Float.valueOf(OrtherEnum.CHINA_LAT_MIN.code())
				|| Float.valueOf(latitude) > Float.valueOf(OrtherEnum.CHINA_LAT_MAX.code())) {
			return false;
		}
		String reglo = "((?:[0-9]|[1-9][0-9]|1[0-7][0-9])\\.([0-9]{0,6}))|((?:180)\\.([0]{0,6}))"; 
		String regla = "((?:[0-9]|[1-8][0-9])\\.([0-9]{0,6}))|((?:90)\\.([0]{0,6}))"; 
		longitude = longitude.trim(); 
		latitude = latitude.trim(); 
		return longitude.matches(reglo)==true?latitude.matches(regla):false; 
	}
	
}
