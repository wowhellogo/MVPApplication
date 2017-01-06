/**
 * Copyright 2016 bingoogolapple
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hao.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CalendarUtil {
    private static final SimpleDateFormat sHourMinuteSdf = new SimpleDateFormat("HH:mm", Locale.CHINESE);
    private static final SimpleDateFormat sMonthDayHourMinuteSdf = new SimpleDateFormat("MM/dd HH:mm", Locale.CHINESE);
    private static final SimpleDateFormat sYearMonthDayHourMinuteSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINESE);
    private static final SimpleDateFormat sYearMonthDaySdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
    private static final SimpleDateFormat sYearMonthDayWeekSlashSdf = new SimpleDateFormat("yyyy-MM-dd E", Locale.CHINESE);
    private static final SimpleDateFormat sYearMonthDayWeekSdf = new SimpleDateFormat("yyyy/MM/dd E", Locale.CHINESE);
    private static final SimpleDateFormat sChineseMonthDaySdf = new SimpleDateFormat("MM月dd日", Locale.CHINESE);
    private static final SimpleDateFormat sChineseYearMonthDaySdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINESE);
    private static final SimpleDateFormat sChineseYearMonthDayWeekSdf = new SimpleDateFormat("yyyy年MM月dd日 E", Locale.CHINESE);
    private static final long MINUTE_MILLISECONDS = 60 * 1000;
    private static final long HOUR_MILLISECONDS = 60 * MINUTE_MILLISECONDS;
    private static final long DAY_MILLISECONDS = 24 * HOUR_MILLISECONDS;

    public static final int NO_TIME = 0;

    private CalendarUtil() {
    }

    /**
     * 将时，分，秒，以及毫秒值设置为0
     *
     * @param calendar
     */
    public static void zeroFromHour(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    /**
     * 将时间戳转换成当天零点的时间戳
     *
     * @param milliseconds
     * @return
     */
    public static Calendar zeroFromHour(long milliseconds) {
        Calendar calendar = getCalendar(milliseconds);
        zeroFromHour(calendar);
        return calendar;
    }

    /**
     * 将时间戳转换成当天零点的时间戳
     *
     * @param milliseconds
     * @return
     */
    public static long zeroFromHourMilliseconds(long milliseconds) {
        return zeroFromHour(milliseconds).getTimeInMillis();
    }

    public static Calendar getCalendar() {
        return Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"), Locale.CHINA);
    }

    public static Calendar getCalendar(long milliseconds) {
        Calendar calendar = getCalendar();
        calendar.setTimeInMillis(milliseconds);
        return calendar;
    }

    /**
     * 获取当前时间零秒零毫秒的时间戳
     *
     * @return
     */
    public static Calendar getZeroSecondCalendar() {
        Calendar calendar = getCalendar();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    /**
     * 获取今天零点零小时零分零秒零毫秒的时间戳
     *
     * @return
     */
    public static long getTodayZeroTimeInMillis() {
        Calendar calendar = getZeroSecondCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取明天零点零小时零分零秒零毫秒的时间戳
     *
     * @return
     */
    public static long getTomorrowZeroTimeInMillis() {
        Calendar calendar = getZeroSecondCalendar();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 格式化详细的日期时间
     *
     * @param milliseconds
     * @return
     */
    public static CharSequence formatDetailDisplayTime(long milliseconds) {
        if (milliseconds == NO_TIME) {
            return "";
        }

        Calendar ultimateCalendar = getCalendar();
        ultimateCalendar.setTimeInMillis(milliseconds);

        Calendar theDayBeforeYesterday = getCalendar();
        theDayBeforeYesterday.add(Calendar.DAY_OF_YEAR, -2);

        Calendar yesterday = getCalendar();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);

        Calendar today = getCalendar();

        Calendar tomorrow = getCalendar();
        tomorrow.add(Calendar.DAY_OF_YEAR, 1);

        Calendar theDayAfterTomorrow = getCalendar();
        theDayAfterTomorrow.add(Calendar.DAY_OF_YEAR, 2);

        if (ultimateCalendar.get(Calendar.YEAR) == theDayBeforeYesterday.get(Calendar.YEAR) && ultimateCalendar.get(Calendar.DAY_OF_YEAR) == theDayBeforeYesterday.get(Calendar.DAY_OF_YEAR)) {
            return "前天 " + formatHourMinute(ultimateCalendar.getTime());
        } else if (ultimateCalendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && ultimateCalendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
            return "昨天 " + formatHourMinute(ultimateCalendar.getTime());
        } else if (ultimateCalendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && ultimateCalendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            long differenceTime = today.getTimeInMillis() - ultimateCalendar.getTimeInMillis();
            if (differenceTime < MINUTE_MILLISECONDS) {
                return "刚刚";
            } else if (differenceTime < HOUR_MILLISECONDS) {
                return (new Long(today.getTimeInMillis() / MINUTE_MILLISECONDS).intValue() - new Long(ultimateCalendar.getTimeInMillis() / MINUTE_MILLISECONDS).intValue()) + "分钟前";
            } else {
                return "今天 " + formatHourMinute(ultimateCalendar.getTime());
            }
        } else if (ultimateCalendar.get(Calendar.YEAR) == tomorrow.get(Calendar.YEAR) && ultimateCalendar.get(Calendar.DAY_OF_YEAR) == tomorrow.get(Calendar.DAY_OF_YEAR)) {
            return "明天 " + formatHourMinute(ultimateCalendar.getTime());
        } else if (ultimateCalendar.get(Calendar.YEAR) == theDayAfterTomorrow.get(Calendar.YEAR) && ultimateCalendar.get(Calendar.DAY_OF_YEAR) == theDayAfterTomorrow.get(Calendar.DAY_OF_YEAR)) {
            return "后天 " + formatHourMinute(ultimateCalendar.getTime());
        } else {
            return formatYearMonthDayHourMinute(ultimateCalendar.getTime());
        }
    }

    /**
     * 格式化详细的日期
     *
     * @param milliseconds
     * @return
     */
    public static CharSequence formatDetailDisplayDate(long milliseconds) {
        if (milliseconds == NO_TIME) {
            return "";
        }

        Calendar ultimateCalendar = getCalendar();
        ultimateCalendar.setTimeInMillis(milliseconds);

        Calendar theDayBeforeYesterday = getCalendar();
        theDayBeforeYesterday.add(Calendar.DAY_OF_YEAR, -2);

        Calendar yesterday = getCalendar();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);

        Calendar today = getCalendar();

        Calendar tomorrow = getCalendar();
        tomorrow.add(Calendar.DAY_OF_YEAR, 1);

        Calendar theDayAfterTomorrow = getCalendar();
        theDayAfterTomorrow.add(Calendar.DAY_OF_YEAR, 2);

        if (ultimateCalendar.get(Calendar.YEAR) == theDayBeforeYesterday.get(Calendar.YEAR) && ultimateCalendar.get(Calendar.DAY_OF_YEAR) == theDayBeforeYesterday.get(Calendar.DAY_OF_YEAR)) {
            return "前天";
        } else if (ultimateCalendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && ultimateCalendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
            return "昨天";
        } else if (ultimateCalendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && ultimateCalendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            return "今天";
        } else if (ultimateCalendar.get(Calendar.YEAR) == tomorrow.get(Calendar.YEAR) && ultimateCalendar.get(Calendar.DAY_OF_YEAR) == tomorrow.get(Calendar.DAY_OF_YEAR)) {
            return "明天";
        } else if (ultimateCalendar.get(Calendar.YEAR) == theDayAfterTomorrow.get(Calendar.YEAR) && ultimateCalendar.get(Calendar.DAY_OF_YEAR) == theDayAfterTomorrow.get(Calendar.DAY_OF_YEAR)) {
            return "后天";
        } else {
            return formatYearMonthDay(ultimateCalendar.getTime());
        }
    }

    public static String formatHourMinute(Date date) {
        return sHourMinuteSdf.format(date);
    }

    public static String formatHourMinute(long milliseconds) {
        if (milliseconds == NO_TIME) {
            return "";
        }

        return formatHourMinute(new Date(milliseconds));
    }


    public static String formatMonthDayHourMinute(Date date) {
        return sMonthDayHourMinuteSdf.format(date);
    }

    public static String formatMonthDayHourMinute(long milliseconds) {
        if (milliseconds == NO_TIME) {
            return "";
        }

        return formatMonthDayHourMinute(new Date(milliseconds));
    }


    public static String formatYearMonthDayHourMinute(Date date) {
        return sYearMonthDayHourMinuteSdf.format(date);
    }

    public static String formatYearMonthDayHourMinute(long milliseconds) {
        if (milliseconds == NO_TIME) {
            return "";
        }

        return formatYearMonthDayHourMinute(new Date(milliseconds));
    }

    public static String formatYearMonthDay(Date date) {
        return sYearMonthDaySdf.format(date);
    }

    public static String formatYearMonthDay(long milliseconds) {
        if (milliseconds == NO_TIME) {
            return "";
        }

        return formatYearMonthDay(new Date(milliseconds));
    }

    public static String formatYearMonthDayWeek(Date date) {
        return sYearMonthDayWeekSdf.format(date);
    }

    public static String formatYearMonthDayWeek(long milliseconds) {
        if (milliseconds == NO_TIME) {
            return "";
        }

        return formatYearMonthDayWeek(new Date(milliseconds));
    }

    public static String formatYearMonthDayWeekSlash(Date date) {
        return sYearMonthDayWeekSlashSdf.format(date);
    }

    public static String formatYearMonthDayWeekSlash(long milliseconds) {
        if (milliseconds == NO_TIME) {
            return "";
        }

        return formatYearMonthDayWeekSlash(new Date(milliseconds));
    }


    public static long parseYearMonthDay(String date) {
        try {
            return sYearMonthDaySdf.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long parseYearMonthDayHourMinute(String time) {
        try {
            return sYearMonthDayHourMinuteSdf.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String formatChineseMonthDay(Date date) {
        return sChineseMonthDaySdf.format(date);
    }

    public static String formatChineseMonthDay(long milliseconds) {
        if (milliseconds == NO_TIME) {
            return "";
        }

        return formatChineseMonthDay(new Date(milliseconds));
    }

    public static String formatChineseYearMonthDay(Date date) {
        return sChineseYearMonthDaySdf.format(date);
    }

    public static String formatChineseYearMonthDay(long milliseconds) {
        if (milliseconds == NO_TIME) {
            return "";
        }

        return formatChineseYearMonthDay(new Date(milliseconds));
    }

    public static String formatChineseYearMonthDayWeek(Date date) {
        return sChineseYearMonthDayWeekSdf.format(date);
    }

    public static String formatChineseYearMonthDayWeek(long milliseconds) {
        if (milliseconds == NO_TIME) {
            return "";
        }

        return formatChineseYearMonthDayWeek(new Date(milliseconds));
    }

    public static int daysBetween(long start, long end) {
        return new Long(end / DAY_MILLISECONDS).intValue() - new Long(start / DAY_MILLISECONDS).intValue();
    }

    public static int daysBetweenToday(long start) {
        return daysBetween(start, getCalendar().getTimeInMillis());
    }
}