package com.yz.aac.common.util;

import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public final class DateUtil extends DateUtils {


	/**
	 * 某天开始时间(时分秒毫秒)
	 */
	public static Date startOfTodDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获取上周一时间
	 */
	public static Date lastMonday() {
		Calendar calendar = Calendar.getInstance();
		while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
			calendar.add(Calendar.DAY_OF_WEEK, -1);
		}
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		int offset = 1 - dayOfWeek;
		calendar.add(Calendar.DATE, offset - 7);

		calendar.setTime(calendar.getTime());
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获取本周周一时间
	 */
	public static Date mondayTime() {
		Calendar calendar = Calendar.getInstance();
		while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
			calendar.add(Calendar.DAY_OF_WEEK, -1);
		}
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-1;
		int offset = 1 - dayOfWeek;
		calendar.add(Calendar.DATE, offset);

		calendar.setTime(calendar.getTime());
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 上月开始的时间
	 * @return
	 * @throws ParseException
	 */
	public static Date lastMonthBeginTime() throws ParseException {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-01 00:00:00");
		return DateUtils.parseDate(sdf2.format(c.getTime()), Locale.CHINA, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 本月开始的时间
	 * @return
	 * @throws ParseException
	 */
	public static Date monthBeginTime() throws ParseException {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 0);
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-01 00:00:00");
		return DateUtils.parseDate(sdf2.format(c.getTime()), Locale.CHINA, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 想要获取的日期与传入日期的差值 比如想要获取传入日期前四天的日期 day=-4即可
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date getSomeDay(Date date, Integer day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, day);
		return calendar.getTime();
	}

	public static void main(String[] args) throws ParseException {
		System.out.println(mondayTime().getTime());

		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(getSomeDay(mondayTime(),-7)));
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2019-02-19 10:43:00").getTime());
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2019-02-19 10:46:00").getTime());
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(monthBeginTime()));
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(1548240999000l)));
		System.out.println(1550221440000l-1550221425036l);
	}

}
