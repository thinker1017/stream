package com.pukai.stream.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import com.pukai.stream.exception.ReyunParseException;

public class DateUtil {
	
	public static final String DATE_PATTERN = "yyyy-MM-dd";
	public static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String HOUR_PATTERN_TZ = "yyyy-MM-dd'T'HH'Z'";
	public static final String MINUTE_PATTERN_TZ = "yyyy-MM-dd'T'HH:mm'Z'";
	
	/**
     * 判断是否需要删除redis key
     * @return
     */
    public static boolean isTimeDelKey() {
        return new DateTime(new Date()).getHourOfDay() == 10;
    }
    
    /**
     * 获取待删除数据日期
     * @return
     */
    public static String getDelDay() {
        DateTime now = new DateTime(new Date());
        return now.minusDays(2).toString(DATE_PATTERN);
    }
	
	public static String getTodayWithStrike() {
		return new SimpleDateFormat(DATE_PATTERN).format(new Date());
	}
	
	public static Date parseDate(String when) {
		Date result = null;
		try {
			result = new SimpleDateFormat(TIME_PATTERN).parse(when);
		} catch (ParseException e) {
			throw new ReyunParseException(e);
		}
		return result;
	}
	
	public static final String getHour(Date date) {
	    return new SimpleDateFormat(HOUR_PATTERN_TZ).format(date);
	}
	
	public static final String get5MFormat(Date date) {
	    DateTime dateTime = new DateTime(date);
	    return dateTime.minusMinutes(dateTime.getMinuteOfHour() % 5).toString(MINUTE_PATTERN_TZ);
	}
	
	public static final List<String> getNeedSyncHourList() {
	    List<String> result = new ArrayList<String>();
	    DateTime dateTime = new DateTime(new Date());
	    
	    for (int i = 0; i < 3; i ++) {
	        result.add(getHour(dateTime.minusHours(i).toDate()));
	    }
	    
	    return result;
	}
	
	public static final List<String> getNeedSync5MList() {
        List<String> result = new ArrayList<String>();
        DateTime dateTime = new DateTime(new Date());
        for (int i = 0; i < 5; i ++) {
            result.add(get5MFormat(dateTime.minusMinutes(i * 5).toDate()));
        }
        return result;
    }
}
