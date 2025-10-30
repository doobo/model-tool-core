package com.github.doobo.utils;

import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.*;

/**
 * 常用时间格式化工具
 */
@Slf4j
public abstract class DoDateUtils {

    public static final DateTimeFormatter FORMATTER_1 = DateTimeFormatter.ofPattern(DateFormat.y_m_d_hms.getFt());
    /**
     * 常用日期格式化字段
     */
    public enum DateFormat {
        y_m_d_hms("yyyy-MM-dd HH:mm:ss"),
        y_m_d("yyyy-MM-dd"),
        ym("yyyyMM"),
        hm("HH:mm"),
        hms("HH:mm:ss"),
        yMdHmsS("yyyyMMddHHmmssSSS"),
        yMdH("yyyyMMddHH"),
        y_m("yyyy-MM"),
        y_m_d_hm("yyyy-MM-dd HH:mm"),
        //2020-10-21T15:39:29.666CST
        iso_s_z("yyyy-MM-dd'T'HH:mm:ss.SSSz"),
        //2022-07-14T15:11:49+08:00
        iso_s_X("yyyy-MM-dd'T'HH:mm:ssXXX"),
        ;

        private final String ft;

        DateFormat(String ft) {
            this.ft = ft;
        }

        public String getFt() {
            return ft;
        }
    }

    /**
     * 字符串转本地时间
     */
    public static LocalDate getLocalDate(String date, String format){
        format = format == null? DateFormat.y_m_d.getFt():format;
        try {
            return LocalDate.parse(date,DateTimeFormatter.ofPattern(format));
        } catch (Exception e) {
            log.error("getLocalDateError:{},", date, e);
            return null;
        }
    }

    /**
     * 字符串转本地时间
     */
    public static LocalDate getLocalDate(String date){
        return getLocalDate(date, null);
    }

    /**
     * 字符串转日期
     */
    public static Date getDate(String date, String format){
        format = format == null? DateFormat.y_m_d.getFt():format;
        SimpleDateFormat fm = new SimpleDateFormat(format);
        try {
            return fm.parse(date);
        } catch (Exception e) {
            log.error("getDateError:{},", date, e);
            return null;
        }
    }

    /**
     * 字符串转日期
     */
    public static Date getDate(String date){
        return getDate(date,null);
    }

    /**
     * 字符串转时间
     */
    public static Date getDateTime(String date, String format){
        format = format == null? DateFormat.y_m_d_hms.getFt():format;
        SimpleDateFormat fm = new SimpleDateFormat(format);
        try {
            return fm.parse(date);
        } catch (Exception e) {
            log.error("getDateTime:{},", date, e);
            return null;
        }
    }

    /**
     * 字符串转时间
     */
    public static Date getDateTime(String date){
        return getDateTime(date,null);
    }

    /**
     * 获取当前时间
     */
    public static Date getCurDate(){
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    /**
     * 获取当天开始时间
     */
    public static Date getStartDate() {
        return getStartDate(null);
    }

    /**
     * 获取开始时间
     */
    public static Date getStartDate(Date dayTime) {
        Date date = Objects.isNull(dayTime)? new Date() : dayTime;
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.y_m_d.getFt());
        return getDate(sdf.format(date));
    }

    /**
     * 将带有纳秒的时间字符串转换成LocalDateTime
     */
    public static LocalDateTime timestampStrToLocalDateTime(String str){
        if(str == null){
            return null;
        }
        long millis = Timestamp.valueOf(str).getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        Date date = calendar.getTime();
        SimpleDateFormat sdm = new  SimpleDateFormat(DateFormat.y_m_d_hms.getFt());
        return LocalDateTime.parse(sdm.format(date),FORMATTER_1);
    }

    /**
     * localDateTime时间格式化
     */
    public static String localDateTimeStr(LocalDateTime localDateTime, String format){
        if(localDateTime == null){
            return null;
        }
        format = format == null ? DateFormat.y_m_d_hms.getFt():format;
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        return df.format(localDateTime);
    }

    /**
     * Date类型转LocalDate类型
     */
    public static LocalDate dateToLocalDate(Date date) {
        if(date == null){
            return null;
        }
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDate();
    }

    /**
     * LocalDateTime类型转Date类型
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        if(localDateTime == null){
            return null;
        }
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        return  Date.from(zdt.toInstant());
    }

    /**
     * date转本地时间
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        if(date == null){
            return null;
        }
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * 日期格式化
     */
    public static String formatDate(Date date) {
        return formatDateByFormat(date, DateFormat.y_m_d.getFt());
    }

    /**
     * 获取字符串类型的格式
     */
    public static String formatDateByFormat(Date date, String format) {
        String result;
        format = format == null? DateFormat.y_m_d_hms.getFt():format;
        date = date == null?Calendar.getInstance().getTime():date;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            result = sdf.format(date);
        } catch (Exception ex) {
            log.error("formatDateByFormatError:{},", date, ex);
            return null;
        }
        return result;
    }

    /**
     * 获取字符串类型的格式
     */
    public static String formatDateByYmdHms(Date date) {
        date = date == null?Calendar.getInstance().getTime():date;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.y_m_d_hms.getFt());
            return sdf.format(date);
        } catch (Exception ex) {
            log.error("formatDateByYmdHmsError:{},", date, ex);

        }
        return null;
    }

    /**
     * 获取最大日期
     */
    public static Date getMaxDate(Date... params){
        Date maxDate = params[0];
        for (int i = 1; i < params.length; i++) {
            if(maxDate.before(params[i])){
                maxDate = params[i];
            }
        }
        return maxDate;
    }

    /**
     * 获取当前日期是星期几
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 字符转时间
     */
    public static LocalDate parseLocalDate(String dateStr, String pattern) {
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 字符转时间
     */
    public static LocalDateTime parseLocalDateTime(String dateTimeStr, String pattern) {
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 字符转时间
     */
    public static LocalTime parseLocalTime(String timeStr, String pattern) {
        return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 时间转字符
     */
    public static String formatLocalDate(LocalDate date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 时间转字符
     */
    public static String formatLocalDateTime(LocalDateTime datetime, String pattern) {
        return datetime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 时间转字符
     */
    public static String formatLocalTime(LocalTime time, String pattern) {
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 日期相隔年数
     */
    public static int periodYears(LocalDate startDateInclusive, LocalDate endDateExclusive) {
        return startDateInclusive.until(endDateExclusive).getYears();
    }

    /**
     * 当前天,间隔多少年,算周岁
     */
    public static int periodYears(LocalDate startDateInclusive) {
        return periodYears(startDateInclusive, LocalDate.now());
    }

    /**
     * 日期相隔天数
     */
    public static long periodDays(LocalDate startDateInclusive, LocalDate endDateExclusive) {
        return endDateExclusive.toEpochDay() - startDateInclusive.toEpochDay();
    }

    /**
     * 与当前天间隔天数
     */
    public static long periodDays(LocalDate endDateExclusive) {
        return endDateExclusive.toEpochDay() - LocalDate.now().toEpochDay();
    }

    /**
     * 日期相隔小时
     */
    public static long durationHours(Temporal startInclusive, Temporal endExclusive) {
        return Duration.between(startInclusive, endExclusive).toHours();
    }

    /**
     * 日期相隔分钟
     */
    public static long durationMinutes(Temporal startInclusive, Temporal endExclusive) {
        return Duration.between(startInclusive, endExclusive).toMinutes();
    }

    /**
     * 日期相隔毫秒数
     */
    public static long durationMillis(Temporal startInclusive, Temporal endExclusive) {
        return Duration.between(startInclusive, endExclusive).toMillis();
    }

    /**
     * 获取日期之间的所有日期
     */
    public static List<Date> getBetweenDates(Date startDate, Date endDate) {
        List<Date> datesInRange = new ArrayList<>();
        if(Objects.isNull(startDate) || Objects.isNull(endDate)){
            return datesInRange;
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);
        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(endDate);
        while (calendar.before(endCalendar)) {
            Date result = calendar.getTime();
            if(endCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
                    && endCalendar.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)){
                datesInRange.add(endDate);
            }else{
                datesInRange.add(result);
            }
            calendar.add(Calendar.DATE, 1);
        }
        if(endCalendar.get(Calendar.DATE) == calendar.get(Calendar.DATE)){
            datesInRange.add(endDate);
        }
        return datesInRange;
    }

    public static Date addYears(Date date, int amount) {
        return add(date, 1, amount);
    }

    public static Date addMonths(Date date, int amount) {
        return add(date, 2, amount);
    }

    public static Date addWeeks(Date date, int amount) {
        return add(date, 3, amount);
    }

    public static Date addDays(Date date, int amount) {
        return add(date, 5, amount);
    }

    public static Date addHours(Date date, int amount) {
        return add(date, 11, amount);
    }

    private static Date add(Date date, int calendarField, int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(calendarField, amount);
            return c.getTime();
        }
    }

    /**
     * 字符串时间格式化成国际通用时间，指定时区
     */
    public static String formatDateByUtcTime(String dateStr, String utcTime){
        if(DoObjectUtils.isNotBlank(dateStr)){
            return null;
        }
        LocalDateTime localDateTime  = null;
        if(dateStr.contains("-") && dateStr.length() == 16){
            localDateTime = LocalDateTime.parse(dateStr,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }else {
            localDateTime = LocalDateTime.parse(dateStr,
                    DateTimeFormatter.ofPattern(DateFormat.y_m_d_hms.getFt()));
        }
        ZoneId zoneId = ZoneId.of(utcTime);
        ZoneOffset mexicoOffset = zoneId.getRules().getOffset(Instant.now());
        OffsetDateTime offsetDateTime = OffsetDateTime.of(localDateTime, mexicoOffset);
        return offsetDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    /**
     * 标准ISO 8601 UTC时间转换Date
     */
    public static Date convertUtcTime(String utcTime){
        if(DoObjectUtils.isBlank(utcTime)){
            return null;
        }
        Instant instant = Instant.parse(utcTime);
        return Date.from(instant);
    }

    /**
     * 将系统默认时区的时间转换为指定UTC时区的对应时间
     */
    public static Date convertFromSystemTime(Date dateTime, String utcTime){
        if(Objects.isNull(dateTime)){
            return null;
        }
        ZonedDateTime uTime = Instant.ofEpochMilli(dateTime.getTime())
                .atZone(ZoneId.of(utcTime));
        return getDateTime(formatLocalDateTime(uTime.toLocalDateTime()
                , DateFormat.y_m_d_hms.getFt())
        );
    }

    /**
     * 将指定时区的时间转换为当前系统默认时区的对应时间
     */
    public static Date convertToSystemTime(Date dateTime, String utcTime){
        if(Objects.isNull(dateTime)){
            return null;
        }
        LocalDateTime time = dateToLocalDateTime(dateTime);
        ZonedDateTime usDateTime = ZonedDateTime.of(time, ZoneId.of(utcTime));
        return Date.from(usDateTime.toInstant());
    }
}