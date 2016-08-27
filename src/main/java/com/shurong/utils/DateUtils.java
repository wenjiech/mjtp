package com.shurong.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtils {
	
	/**
	 * 获得一个月的第一天
	 * @param year
	 * @param month
	 * @return
	 */
	public static String getFirstDayOfMonth(int year, int month){
		return getFirstDayOfMonth(year, month, "yyyyMMdd");
	}
	
	/**
	 * 获得一个月的第一天
	 * @param year
	 * @param month
	 * @param formart
	 * @return
	 */
	public static String getFirstDayOfMonth(int year, int month, String formart){
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month -1 , 1);//Calendar中月份是从0开始计算，故实际月份-1
		return new SimpleDateFormat(formart).format(calendar.getTime());
	}
	
	
	/**
	 * 获得一个月的最后一天
	 * @param year
	 * @param month
	 * @return
	 */
	public static String getLastDayOfMonth(int year, int month){
		return getLastDayOfMonth(year, month, "yyyyMMdd");
	}
	
	/**
	 * 获得一个月的最后一天
	 * @param year
	 * @param month
	 * @param formart
	 * @return
	 */
	public static String getLastDayOfMonth(int year, int month, String formart){
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month -1, 1);//Calendar中月份是从0开始计算，故实际月份-1
		calendar.roll(Calendar.DATE, -1);
		return new SimpleDateFormat(formart).format(calendar.getTime());
	}
	
	public static void main(String[] args) {
		System.out.println(getFirstDayOfMonth(2016, Calendar.FEBRUARY, "yyyyMMdd"));
		System.out.println(getLastDayOfMonth(2016, 1, "yyyyMMdd"));
	}
}
