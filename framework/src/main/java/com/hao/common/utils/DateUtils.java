package com.hao.common.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtils {
    private static Date date;
    private static Calendar CALENDAR = Calendar.getInstance();
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

    /**
     * 取得当前时间
     *
     * @return 当前日期（Date）
     */
    public static Date getCurrentDate() {
        return new Date();
    }

    /**
     * 取得昨天此时的时间
     *
     * @return 昨天日期（Date）
     */
    public static Date getYesterdayDate() {
        return new Date(getCurTimeMillis() - 0x5265c00L);
    }

    /**
     * 取得去过i天的时间
     *
     * @param i 过去时间天数
     * @return 昨天日期（Date）
     */
    public static Date getPastdayDate(int i) {
        return new Date(getCurTimeMillis() - 0x5265c00L * i);
    }

    /**
     * 取得当前时间的长整型表示
     *
     * @return 当前时间（long）
     */
    public static long getCurTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 取得当前时间的特定表示格式的字符串
     *
     * @param formatDate 时间格式（如：yyyy/MM/dd hh:mm:ss）
     * @return 当前时间
     */
    public static synchronized String getCurFormatDate(String formatDate) {
        date = getCurrentDate();
        simpleDateFormat.applyPattern(formatDate);
        return simpleDateFormat.format(date);
    }

    /**
     * 取得某日期时间的特定表示格式的字符串
     *
     * @param format 时间格式
     * @param date   某日期（Date）
     * @return 某日期的字符串
     */
    public static synchronized String getDate2Str(String format, Date date) {
        simpleDateFormat.applyPattern(format);
        return simpleDateFormat.format(date);
    }

    /**
     * 将日期转换为长字符串（包含：年-月-日 时:分:秒）
     *
     * @param date 日期
     * @return 返回型如：yyyy-MM-dd HH:mm:ss 的字符串
     */
    public static String getDate2LStr(Date date) {
        return getDate2Str("yyyy-MM-dd HH:mm:ss", date);
    }


    /**
     * 将日期转换为长字符串（包含：年/月/日 时:分:秒）
     *
     * @param date 日期
     * @return 返回型如：yyyy/MM/dd HH:mm:ss 的字符串
     */
    public static String getDate2LStr2(Date date) {
        return getDate2Str("yyyy/MM/dd HH:mm:ss", date);
    }

    /**
     * 将日期转换为中长字符串（包含：年-月-日 时:分）
     *
     * @param date 日期
     * @return 返回型如：yyyy-MM-dd HH:mm 的字符串
     */
    public static String getDate2MStr(Date date) {
        return getDate2Str("yyyy-MM-dd HH:mm", date);
    }

    /**
     * 将日期转换为中长字符串（包含：年/月/日 时:分）
     *
     * @param date 日期
     * @return 返回型如：yyyy/MM/dd HH:mm 的字符串
     */
    public static String getDate2MStr2(Date date) {
        return getDate2Str("yyyy/MM/dd HH:mm", date);
    }

    /**
     * 将日期转换为短字符串（包含：年-月-日）
     *
     * @param date 日期
     * @return 返回型如：yyyy-MM-dd 的字符串
     */
    public static String getDate2SStr(Date date) {
        return getDate2Str("yyyy-MM-dd", date);
    }

    /**
     * 将日期转换为短字符串（包含：年/月/日）
     *
     * @param date 日期
     * @return 返回型如：yyyy/MM/dd 的字符串
     */
    public static String getDate2SStr2(Date date) {
        return getDate2Str("yyyy/MM/dd", date);
    }

    /**
     * 取得型如：yyyyMMddhhmmss的字符串
     *
     * @param date
     * @return 返回型如：yyyyMMddhhmmss 的字符串
     */
    public static String getDate2All(Date date) {
        return getDate2Str("yyyyMMddhhmmss", date);
    }

    /**
     * 将长整型数据转换为Date后，再转换为型如yyyy-MM-dd HH:mm:ss的长字符串
     *
     * @param l 表示某日期的长整型数据
     * @return 日期型的字符串
     */
    public static String getLong2LStr(long l) {
        date = getLongToDate(l);
        return getDate2LStr(date);
    }

    /**
     * 将长整型数据转换为Date后，再转换为型如yyyy-MM-dd的长字符串
     *
     * @param l 表示某日期的长整型数据
     * @return 日期型的字符串
     */
    public static String getLong2SStr(long l) {
        date = getLongToDate(l);
        return getDate2SStr(date);
    }

    /**
     * 将长整型数据转换为Date后，再转换指定格式的字符串
     *
     * @param l          表示某日期的长整型数据
     * @param formatDate 指定的日期格式
     * @return 日期型的字符串
     */
    public static String getLong2SStr(long l, String formatDate) {
        date = getLongToDate(l);
        simpleDateFormat.applyPattern(formatDate);
        return simpleDateFormat.format(date);
    }

    private static synchronized Date getStrToDate(String format, String str) {
        simpleDateFormat.applyPattern(format);
        ParsePosition parseposition = new ParsePosition(0);
        try {
            return simpleDateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将某指定的字符串转换为某类型的字符串
     *
     * @param format 转换格式
     * @param str    需要转换的字符串
     * @return 转换后的字符串
     */
    public static String getStr2Str(String format, String str) {
        Date date = getStrToDate(format, str);
        return getDate2Str(format, date);
    }

    /**
     * 将某指定的字符串转换为型如：yyyy-MM-dd HH:mm:ss的时间
     *
     * @param str 将被转换为Date的字符串
     * @return 转换后的Date
     */
    public static Date getStr2LDate(String str) {
        return getStrToDate("yyyy-MM-dd HH:mm:ss", str);
    }

    /**
     * 将某指定的字符串转换为型如：yyyy/MM/dd HH:mm:ss的时间
     *
     * @param str 将被转换为Date的字符串
     * @return 转换后的Date
     */
    public static Date getStr2LDate2(String str) {
        return getStrToDate("yyyy/MM/dd HH:mm:ss", str);
    }

    /**
     * 将某指定的字符串转换为型如：yyyy-MM-dd HH:mm的时间
     *
     * @param str 将被转换为Date的字符串
     * @return 转换后的Date
     */
    public static Date getStr2MDate(String str) {
        return getStrToDate("yyyy-MM-dd HH:mm", str);
    }

    /**
     * 将某指定的字符串转换为型如：yyyy/MM/dd HH:mm的时间
     *
     * @param str 将被转换为Date的字符串
     * @return 转换后的Date
     */
    public static Date getStr2MDate2(String str) {
        return getStrToDate("yyyy/MM/dd HH:mm", str);
    }

    /**
     * 将某指定的字符串转换为型如：yyyy-MM-dd的时间
     *
     * @param str 将被转换为Date的字符串
     * @return 转换后的Date
     */
    public static Date getStr2SDate(String str) {
        return getStrToDate("yyyy-MM-dd", str);
    }

    /**
     * 将某指定的字符串转换为型如：yyyy-MM-dd的时间
     *
     * @param str 将被转换为Date的字符串
     * @return 转换后的Date
     */
    public static Date getStr2SDate2(String str) {
        return getStrToDate("yyyy/MM/dd", str);
    }

    /**
     * 将某长整型数据转换为日期
     *
     * @param l 长整型数据
     * @return 转换后的日期
     */
    public static Date getLongToDate(long l) {
        return new Date(l);
    }

    /**
     * 以分钟的形式表示某长整型数据表示的时间到当前时间的间隔
     *
     * @param l 长整型数据
     * @return 相隔的分钟数
     */
    public static int getOffMinutes(long l) {
        return getOffMinutes(l, getCurTimeMillis());
    }

    /**
     * 以分钟的形式表示两个长整型数表示的时间间隔
     *
     * @param from 开始的长整型数据
     * @param to   结束的长整型数据
     * @return 相隔的分钟数
     */
    public static int getOffMinutes(long from, long to) {
        return (int) ((to - from) / 60000L);
    }

    /**
     * 以微秒的形式赋值给一个Calendar实例
     *
     * @param l 用来表示时间的长整型数据
     */
    public static void setCalendar(long l) {
        CALENDAR.clear();
        CALENDAR.setTimeInMillis(l);
    }

    /**
     * 以日期的形式赋值给某Calendar
     *
     * @param date 指定日期
     */
    public static void setCalendar(Date date) {
        CALENDAR.clear();
        CALENDAR.setTime(date);
    }

    /**
     * 在此之前要由一个Calendar实例的存在
     *
     * @return 返回某年
     */
    public static int getYear() {
        return CALENDAR.get(1);
    }

    /**
     * 在此之前要由一个Calendar实例的存在
     *
     * @return 返回某月
     */
    public static int getMonth() {
        return CALENDAR.get(2) + 1;
    }

    /**
     * 在此之前要由一个Calendar实例的存在
     *
     * @return 返回某天
     */
    public static int getDay() {
        return CALENDAR.get(5);
    }

    /**
     * 在此之前要由一个Calendar实例的存在
     *
     * @return 返回某小时
     */
    public static int getHour() {
        return CALENDAR.get(11);
    }

    /**
     * 在此之前要由一个Calendar实例的存在
     *
     * @return 返回某分钟
     */
    public static int getMinute() {
        return CALENDAR.get(12);
    }

    /**
     * 在此之前要由一个Calendar实例的存在
     *
     * @return 返回某秒
     */
    public static int getSecond() {
        return CALENDAR.get(13);
    }

    public static String getStandardTime(String time) {
        Date date = getStr2SDate(time);
        return getDate2SStr(date);
    }

    public static boolean isSameDay(Date day1, Date day2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String ds1 = sdf.format(day1);
        String ds2 = sdf.format(day2);
        if (ds1.equals(ds2)) {
            return true;
        } else {
            return false;
        }
    }

    public static long getBefore30min(long time) {
        // TODO Auto-generated method stub
        return time - 30 * 60 * 1000;
    }

    public static long getBefore1day(long time) {
        // TODO Auto-generated method stub
        return time - 1 * 24 * 60 * 60 * 1000;
    }

    public static long getBefore3day(long time) {
        // TODO Auto-generated method stub
        return time - 3 * 24 * 60 * 60 * 1000;
    }

    public static long getBefore7day(long time) {
        // TODO Auto-generated method stub
        return time - 7 * 24 * 60 * 60 * 1000;
    }

    public static long getDifferenceSeconds(String date1, String date2) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = df.parse(date1);
        Date date = df.parse(date2);
        long l = now.getTime() - date.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        return s;
    }

    public static String getMyPictureDate(String date1) throws ParseException {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat df2 = new SimpleDateFormat("ddM月");

        Date now = df1.parse(df1.format(new Date()));
        Date yesterDate = new Date(now.getTime() - 24 * 60 * 60 * 1000);
        Date date = df.parse(date1);

        if (date.getTime() > now.getTime()) {
            return "今天";
        } else if (date.getTime() > yesterDate.getTime()) {
            return "昨天";
        } else {
            return df2.format(date);
        }
    }

    public static String getDateAMPM(String strDate) {
        Date date = DateUtils.getStr2LDate(strDate);
        String nameDate = null;
        if (date.getHours() <= 1) {
            nameDate = "半夜";
        } else if (date.getHours() <= 5) {
            nameDate = "凌晨";
        } else if (date.getHours() <= 11) {
            nameDate = "上午";
        } else if (date.getHours() <= 14) {
            nameDate = "中午";
        } else if (date.getHours() <= 18) {
            nameDate = "下午";
        } else if (date.getHours() <= 23) {
            nameDate = "晚上";
        }
        if (date.getHours() <= 12) {
            return (date.getHours() > 9 ? date.getHours() : "0" + date.getHours()) + ":" + (date.getMinutes() > 9 ? date.getMinutes() : "0" + date.getMinutes()) + nameDate;
        } else {
            int hours = date.getHours() - 12;
            return (hours > 9 ? hours : "0" + hours) + ":" + (date.getMinutes() > 9 ? date.getMinutes() : "0" + date.getMinutes()) + nameDate;
        }

    }

    public static int getWeeks(){
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DATE);
    }

}
