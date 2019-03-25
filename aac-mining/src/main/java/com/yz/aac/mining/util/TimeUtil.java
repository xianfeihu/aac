package com.yz.aac.mining.util;

import java.text.ParseException;

/**
 * 时间工具
 *
 */
public final class TimeUtil {

	/**
	 * 秒转化为天小时分秒字符串
	 * @param seconds 秒单位
	 * @return
	 */
	public static String formatSeconds(long seconds) {
        String timeStr = seconds + "秒";
        if (seconds > 60) {
            long second = seconds % 60;
            long min = seconds / 60;
            timeStr = min + "分" + second + "秒";
            if (min > 60) {
                min = (seconds / 60) % 60;
                long hour = (seconds / 60) / 60;
                timeStr = hour + "小时" + min + "分" + second + "秒";
                if (hour > 24) {
                    hour = ((seconds / 60) / 60) % 24;
                    long day = (((seconds / 60) / 60) / 24);
                    timeStr = day + "天" + hour + "小时" + min + "分" + second + "秒";
                }
            }
        }
        return timeStr;
    }

    /**
     * 秒转化为具体的（天/小时/分钟）
     * @param seconds 秒单位
     * @return
     */
    public static String secondsFormatStr(long seconds) {
        String timeStr;
        if (seconds > 60) {
            long min = seconds / 60;
            timeStr = min + "分钟前";
            if (min > 60) {
                long hour = (seconds / (60 * 60));
                timeStr = hour + "小时前";
                if (hour > 24) {
                    long day = (seconds / (60 * 60 * 24));
                    timeStr = day + "天前";
                }
            }
        } else {
            timeStr = "1分钟前";
        }
        return timeStr;
    }

    public static void main(String[] args) throws ParseException {
        System.out.println(secondsFormatStr((System.currentTimeMillis() - 1550128445249L)/1000));
    }
	
}
