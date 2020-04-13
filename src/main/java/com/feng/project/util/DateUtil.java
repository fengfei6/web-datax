package com.feng.project.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {
    private static final Logger log = LoggerFactory.getLogger(DateUtil.class);

    private static final String PARAMS_Y_M = "yyyy-MM";
    public static final String DATE_FORMAT_NOUD = "yyyyMMdd";
    private static final int MAX_NUM = 1000 * 3600 * 24;


    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String YEAR = "y";
    private static final String MONTH = "m";
    private static final String WEEK = "w";


    private static final String YEAR_FORMAT = "yyyy";
    private static final String MONTH_FORMAT_ = "yyyyMM";
    private static final String MONTH_FORMAT = "yyyy-MM";
    private static final String DATE_FORMAT_ = "yyyyMMdd";
    private static final String DATETIME_FORMAT_ = "yyyyMMdd HH:mm:ss";

    private static final String HOUR_FORMAT = "yyyy-MM-ddHH";
    private static final String HOUR_FORMAT_ = "yyyyMMddHH";
    private static final String MINITER_FORMAT = "yyyy-MM-ddHH:mm";
    private static final String MINITER_FORMAT_ = "yyyyMMddHH:mm";
    private static final String SECOND_FORMAT = "yyyy-MM-ddHH:mm:ss";
    private static final String SECOND_FORMAT_ = "yyyyMMddHH:mm:ss";


    private static final ThreadLocal<Map<String, DateFormat>> dateFormatThreadLocal = new ThreadLocal<Map<String, DateFormat>>();


    public static String getSecond(String controlTime) {
        return controlTime.split(":")[2];
    }

    public static String getMinute(String controlTime) {
        return controlTime.split(":")[1];
    }

    public static String getHourOfDay(String controlTime) {
        return controlTime.split(":")[0];
    }

    /**
     * $bizdate
     * format datetime. like "yyyyMMdd"
     *
     */
    public static String bizdateString() {
        return format(DateUtil.addDays(new Date(),-1), DATE_FORMAT);
    }

    public static Date bizdate() {
        return DateUtil.addDays(new Date(),-1);
    }

    /**
     * $cyctime
     * format datetime. like "yyyy-MM-dd HH:mm:ss"
     *
     */
    public static String cyctimeString() {
        return format(new Date(), DATETIME_FORMAT);
    }


    public static Date cyctime() {
        return new Date();
    }
    /**
     * $gmtdate
     * format datetime. like "yyyyMMdd"
     *
     */
    public static String gmtdate() {
        return format(new Date(), DATE_FORMAT);
    }

    // ---------------------- add date ----------------------

    public static Date addYears(final Date date, final int amount) {
        return add(date, Calendar.YEAR, amount);
    }

    public static Date addMonths(final Date date, final int amount) {
        return add(date, Calendar.MONTH, amount);
    }

    public static Date addDays(final Date date, final int amount) {
        return add(date, Calendar.DAY_OF_MONTH, amount);
    }

    public static String addStringYear(){
        return new SimpleDateFormat(DATE_FORMAT).format(addYears(new Date(),1));
    }

    public static Date addHours(final Date date, final int amount) {
        return add(date, Calendar.HOUR_OF_DAY, amount);
    }

    public static Date addMinutes(final Date date, final int amount) {
        return add(date, Calendar.MINUTE, amount);
    }

    public static Date addSeconds(final Date date, final int amount) {
        return add(date, Calendar.SECOND, amount);
    }
    private static Date add(final Date date, final int calendarField, final int amount) {
        if (date == null) {
            return null;
        }
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }


    /**
     * format date. like "yyyy-MM-dd HH:mm:ss"
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static String formatDateTime(Date date) {
        return format(date, DATETIME_FORMAT);
    }
    /**
     * format date
     *
     * @param date
     * @param patten
     * @return
     * @throws ParseException
     */
    public static String format(Date date, String patten) {

        if(patten.equals(HOUR_FORMAT)||patten.equals(HOUR_FORMAT_)){
            String time =  getDateFormat(patten).format(date);
            String resultTime = time.substring(0,patten.length()-2) + " " + time.substring(patten.length()-2,patten.length());
            return resultTime;
        }else if(patten.equals(MINITER_FORMAT)||patten.equals(MINITER_FORMAT_)){
            String time =  getDateFormat(patten).format(date);
            String resultTime = time.substring(0,patten.length()-5) + " " + time.substring(patten.length()-5,patten.length());
            return resultTime;
        }else if(patten.equals(SECOND_FORMAT)||patten.equals(SECOND_FORMAT_)){
            String time =  getDateFormat(patten).format(date);
            String resultTime = time.substring(0,patten.length()-8) + " " + time.substring(patten.length()-8,patten.length());
            return resultTime;
        }
        return getDateFormat(patten).format(date);
    }


    private static DateFormat getDateFormat(String pattern) {
        if (pattern==null || pattern.trim().length()==0) {
            throw new IllegalArgumentException("pattern cannot be empty.");
        }

        Map<String, DateFormat> dateFormatMap = dateFormatThreadLocal.get();
        if(dateFormatMap!=null && dateFormatMap.containsKey(pattern)){
            return dateFormatMap.get(pattern);
        }

        synchronized (dateFormatThreadLocal) {
            if (dateFormatMap == null) {
                dateFormatMap = new HashMap<String, DateFormat>();
            }
            dateFormatMap.put(pattern, new SimpleDateFormat(pattern));
            dateFormatThreadLocal.set(dateFormatMap);
        }

        return dateFormatMap.get(pattern);
    }
    /**
     *
     * @param dateString
     * @return
     * @throws ParseException
     */
    public static Date parseDate(String dateString){
        return parse(dateString, DATE_FORMAT_NOUD);
    }


    public static Long dealDateFormatToLong(String oldDateStr) {
        oldDateStr = oldDateStr.replace("T"," ").replace(".000+0000","");
        return parse(oldDateStr,DATETIME_FORMAT).getTime();
    }

    /**
     * parse date
     *
     * @param dateString
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date parse(String dateString, String pattern) {
        try {
            Date date = getDateFormat(pattern).parse(dateString);
            return date;
        } catch (Exception e) {
            log.warn("parse date error, dateString = {}, pattern={}; errorMsg = {}", dateString, pattern, e.getMessage());
            return null;
        }
    }


    public static String getNowDateNoTime(){
        return new SimpleDateFormat(DATE_FORMAT).format(new Date());
    }

    /**
     * @Author caozqa
     * @Description //获取当前日期
     * @Date 10:32 2020/3/10
     * @Param []
     * @return java.lang.String
     **/
    public static String getNowDateNoUder(){
        return new SimpleDateFormat(DATE_FORMAT_NOUD).format(new Date());
    }


    public static String getNowDateNoUder(String pattern){
        return new SimpleDateFormat(pattern).format(new Date());
    }


    //
    public static String getPreMothDate(String dateStr){
        Date date = null;
        try {
            date = new SimpleDateFormat(DATE_FORMAT).parse(dateStr);
        } catch (ParseException e) {
            log.error("错误日志",e);
            return dateStr;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        return new SimpleDateFormat(DATE_FORMAT).format(calendar.getTime());
    }


    public static String getNowDate(){
        return new SimpleDateFormat(DATETIME_FORMAT).format(new Date());
    }

    public static String getNowDateByFormat(){
        return new SimpleDateFormat(PARAMS_Y_M).format(new Date());
    }


    /*
     * 将时间转换为时间戳
     */
    public static Long dateToStamp(String timeStr) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(timeStr);
        return date.getTime();
    }

    /*
     * 将时间戳转换为时间yyyy-MM-dd
     */
    public static String stampToDate(Long timeLong){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(timeLong);
        return simpleDateFormat.format(date);
    }

    public static String stampToformatDate(Long timeLong){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATETIME_FORMAT);
        Date date = new Date(timeLong);
        return simpleDateFormat.format(date);
    }
    public static String stampStrToformatDate(String time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATETIME_FORMAT);
        Date date = new Date(Long.valueOf(time));
        return simpleDateFormat.format(date);
    }

    public static String StrToDate(String dateStr) {
        Date date = null;
        try {
            date = new SimpleDateFormat(DATE_FORMAT).parse(dateStr);
        } catch (ParseException e) {
            log.error("错误日志",e);
        }
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }

    public static Date getTimefromStr(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    /**
     * 获取某年第一天日期
     * @param year 年份
     * @return Date
     */
    public static String getYearFirst(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        return format(calendar.getTime(),DATE_FORMAT_NOUD);
    }
    /**
     * 获取某年最后一天日期
     * @param year 年份
     * @return Date
     */
    public static String getYearLast(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        return format(calendar.getTime(),DATE_FORMAT_NOUD);
    }


    public static String getLastDayOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month-1);
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DATE));
        return format(calendar.getTime(),DATE_FORMAT_NOUD);
    }
    public static String getFirstDayOfMonth(int year, int month, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month-1);
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getMinimum(Calendar.DATE));
        return format(calendar.getTime(),format);
    }

    public static String getFirstDayOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month-1);
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getMinimum(Calendar.DATE));
        return format(calendar.getTime(),DATE_FORMAT_);
    }

    /**
     * 返回指定日期的年的第一天
     *
     * @param date
     * @return
     */
    public static String getFirstDayOfYear(Date date,String format) {
        if(format.equals(DATE_FORMAT_)){
            return new SimpleDateFormat(YEAR_FORMAT).format(date)+"0101";
        }else if(format.equals(DATE_FORMAT)){
            return new SimpleDateFormat(YEAR_FORMAT).format(date)+"-01-01";
        }else{
            return new SimpleDateFormat(YEAR_FORMAT).format(date)+"0101";
        }
    }

    /**
     * 返回指定日期的月的第一天
     *周日为一周第一天
     * @param date
     * @return
     */
    public static String getFirstDayOfMonth(Date date,String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH), 1);
        return format(calendar.getTime(),format);
    }

    /**
     * 得到某年某周的第一天
     *
     * @param year
     * @param week
     * @return
     */
    public static String getFirstDayOfWeek(int year, int week) {
        week = week - 1;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DATE, 1);

        Calendar cal = (Calendar) calendar.clone();
        cal.add(Calendar.DATE, week * 7);

        return getFirstDayOfWeek(cal.getTime());
    }

    /**
     * 取得当前日期所在周的第一天
     *
     * @param date
     * @return
     */
    public static String getFirstDayOfWeek(Date date,String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK,
            calendar.getFirstDayOfWeek()); // Sunday
        return format(calendar.getTime(),format);
    }

    /**
     * 得到某年某周的最后一天
     *
     * @param year
     * @param week
     * @return
     */
    public static String getLastDayOfWeek(int year, int week) {
        week = week - 1;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DATE, 1);
        Calendar cal = (Calendar) calendar.clone();
        cal.add(Calendar.DATE, week * 7);

        return getLastDayOfWeek(cal.getTime());
    }
    /**
     * 取得当前日期所在周的第一天
     *
     * @param date
     * @return
     */
    public static String getFirstDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK,
            calendar.getFirstDayOfWeek()); // Sunday
        return format(calendar.getTime(),DATE_FORMAT_NOUD);
    }

    /**
     * 取得当前日期所在周的最后一天
     *
     * @param date
     * @return
     */
    public static String getLastDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK,
            calendar.getFirstDayOfWeek() + 6); // Saturday
        return format(calendar.getTime(),DATE_FORMAT_NOUD);
    }

    /**
     * 返回指定日期的月的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH), 1);
        return calendar.getTime();
    }
    /**
     * 返回指定日期的月的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH), 1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.getTime();
    }


    /**
     * 返回指定日期的季的第一天
     *
     * @return
     */
    //public static String getFirstDayOfQuarter(Date date) {
    //    Calendar calendar = Calendar.getInstance();
    //    calendar.setTime(date);
    //    return getFirstDayOfQuarter(calendar.get(Calendar.YEAR),
    //        getQuarterOfYear(date));
    //}

    /**
     * 返回指定日期的上一季的最后一天
     *
     * @param year
     * @param quarter
     * @return
     */
    public static String getLastDayOfLastQuarter(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getLastDayOfLastQuarter(calendar.get(Calendar.YEAR),
            getQuarterOfYear(date));
    }
    /**
     * 返回指定年季的上一季的最后一天
     *
     * @param year
     * @param quarter
     * @return
     */
    public static String getLastDayOfLastQuarter(Integer year, Integer quarter) {
        Calendar calendar = Calendar.getInstance();
        Integer month = new Integer(0);
        if (quarter == 1) {
            month = 12 - 1;
        } else if (quarter == 2) {
            month = 3 - 1;
        } else if (quarter == 3) {
            month = 6 - 1;
        } else if (quarter == 4) {
            month = 9 - 1;
        } else {
            month = calendar.get(Calendar.MONTH);
        }
        return getLastDayOfMonth(year, month);
    }

    /**
     * 返回指定日期的季的第一天
     *
     * @return
     */
    public static String getFirstDayOfQuarter(Date date,String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getFirstDayOfQuarter(calendar.get(Calendar.YEAR),
            getQuarterOfYear(date),format);
    }

    /**
     * 返回指定年季的季的第一天
     *
     * @param year
     * @param quarter
     * @return
     */
    public static String getFirstDayOfQuarter(Integer year, Integer quarter, String format) {
        Calendar calendar = Calendar.getInstance();
        Integer month = new Integer(0);
        if (quarter == 1) {
            month = 1 ;
        } else if (quarter == 2) {
            month = 4;
        } else if (quarter == 3) {
            month = 7;
        } else if (quarter == 4) {
            month = 10;
        } else {
            month = calendar.get(Calendar.MONTH);
        }
        return getFirstDayOfMonth(year, month, format);
    }
    /**
     * 返回指定日期的季度
     *
     * @param date
     * @return
     */
    public static int getQuarterOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) / 3 + 1;
    }
    /**
     * 返回指定年季的季的最后一天
     *
     * @param year
     * @param quarter
     * @return
     */
    public static String getLastDayOfQuarter(Integer year, Integer quarter) {
        Calendar calendar = Calendar.getInstance();
        Integer month = new Integer(0);
        if (quarter == 1) {
            month = 3 - 1;
        } else if (quarter == 2) {
            month = 6 - 1;
        } else if (quarter == 3) {
            month = 9 - 1;
        } else if (quarter == 4) {
            month = 12 - 1;
        } else {
            month = calendar.get(Calendar.MONTH);
        }
        return getLastDayOfMonth(year, month);
    }



    public static String addTimeWithCyc( String sign, Integer n,String format,Date cyctime) {
        Date time = null;
        if(sign.equals(YEAR)){
            time = addYears(cyctime,-n);
        }else if(sign.equals(MONTH)){
            time = addMonths(cyctime,-n);
        }else if(sign.equals(WEEK)){
            time = addDays(cyctime,-7*n);
        }else{
            return null;
        }
        return format(time,format);

    }

    public static String addTimes( Integer n,String format,Date date) {
        Date time = null;
        if(format.equals(YEAR_FORMAT)){
            time = addYears(date,-n);
        }else if(format.equals(MONTH_FORMAT)||format.equals(MONTH_FORMAT_)){
            time =  addMonths(date,-n);
        }else if(format.equals(DATE_FORMAT)||format.equals(DATE_FORMAT_)){
            time =  addDays(date,-n);
        }else if(format.equals(HOUR_FORMAT)||format.equals(HOUR_FORMAT_)){
            time =  addHours(date,-n);
        }else if(format.equals(MINITER_FORMAT)||format.equals(MINITER_FORMAT_)){
            time =  addMinutes(date,-n);
        }else if(format.equals(SECOND_FORMAT)||format.equals(SECOND_FORMAT_)){
            time =  addSeconds(date,-n);
        }else{
            return null;
        }
        //Inverse  number
        return format(time,format);
    }



    /**
     * @Author caozqa
     * @Description //获取前一天的数据
     * @Date 10:32 2020/3/10
     * @Param []
     * @return java.lang.String
     **/
    public static String preDateString(String pattern,Integer perNum){
        if(null == perNum){
            return getNowDateNoUder(pattern);
        }
        try{
            String now = getNowDateNoUder(pattern);
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            Date day = sdf.parse(now);
            long ms = day.getTime() - perNum*24*3600*1000L;
            Date prevDay = new Date(ms);
            return sdf.format(prevDay).toString();
        }catch (ParseException e){
            log.error("获取前一天的时间出现问题");
        }
        return null;
    }
}
