package com.cdzg.xzshop.utils;

import org.apache.http.client.utils.DateUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class DateUtil {
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_PATTERN = "yyyy-MM-dd";

    public static final String YEAR_MONTH_PATTERN = "yyyy-MM";

    public static final String DATE_TIME_PATTERN_WECHAT = "yyyyMMddHHmmss";

    /**
     * @param @param  date
     * @param @return
     * @return String 返回类型
     * @throws
     * @Title: convertDate
     * @Description: 日期转字符串
     */
    public static String convertDate(Date date, int formatType) {
        String format = "";
        if (null == date) {
            return null;
        }
        switch (formatType) {
            case 1:
                format = "yyyy-MM-dd";
                break;
            case 2:
                format = "yyyyMMdd";
                break;
            case 3:
                format = "yyyy";
                break;
            case 4:
                format = "yyyyMMddHHmmss";
                break;
            case 5:
                format = "MM";
                break;
            case 6:
                format = "HH:mm:ss";
                break;
            case 7:
                format = "HH:mm";
                break;
            case 8:
                format = "MM-dd";
                break;
            case 9:
                format = "MMdd";
                break;
            case 10:
                format = "yyyy-MM-dd HH:mm";
                break;
            case 11:
                format = "yyyy-MM";
                break;
            case 12:
                format = "MM-dd HH:mm";
                break;
            default:
                format = "yyyy-MM-dd HH:mm:ss";
                break;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String newDate = sdf.format(date);
        return newDate;
    }

    /**
     * 将字符串转换成日期
     *
     * @param date
     * @return
     */
    public static Date covertStrToDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate = null;
        try {
            newDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("时间转化错误");
        }
        return newDate;
    }

    /**
     * 将字符串转换成日期 时分秒
     *
     * @param date
     * @return
     */
    public static Date covertStrToDateMore(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date newDate = null;
        try {
            newDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }

    /**
     * @return String 返回类型
     * @throws
     * @Title: getNowDate
     * @Description: 获取String类型的当前日期
     */
    public static String getNowDate() {

        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String newDate = sdf.format(new Date());

        return newDate;
    }

    /**
     * @return String 返回类型
     * @throws
     * @Title: getNowDateTime
     * @Description: 获取String类型的当前日期
     */
    public static String getNowDateTime() {

        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String newDate = sdf.format(new Date());
        return newDate;
    }

    /**
     * @param time
     * @return 设定文件
     * @Title: getTrimTime
     * @Description: 时间转成纯数字
     */
    public static String getTrimTime(String time) {
        time = time.replace(" ", "").replace("-", "").replace(":", "").trim();
        return time;
    }

    /**
     * 取得某个月的最后一天
     */
    public static String getLastDayOfMonth(String day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate;
        int maxDayOfMonth = 0;
        try {
            newDate = sdf.parse(day + "-1");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(newDate);
            maxDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return String.valueOf(maxDayOfMonth);
    }

    /**
     * 取得当前月的第一天
     */
    public static String getFirstDayOfMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        String first = sdf.format(c.getTime());
        return first;
    }

    /**
     * @param @param  date
     * @param @return
     * @return String 返回类型
     * @throws
     * @Title: getCurrentYear
     * @Description: 获取当前年份
     */
    public static String getCurrentYear(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return sdf.format(date);
    }

    /**
     * @param @param  date
     * @param @return
     * @return String 返回类型
     * @throws
     * @Title: getCnDate
     * @Description: 获取12月20日
     */
    public static String getCnDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        String[] dateArray = sdf.format(date).split("-");
        String month = dateArray[0].startsWith("0") ? dateArray[0].replace("0", "") : dateArray[0];
        String day = dateArray[1].startsWith("0") ? dateArray[1].replace("0", "") : dateArray[1];
        return month.concat("月").concat(day).concat("日");
    }

    /**
     * @param @param  startDate
     * @param @param  endDate
     * @param @return
     * @param @throws ParseException
     * @return Integer 返回类型
     * @throws
     * @Title: getDateMinusDays
     * @Description: 获取日期相差天数
     */
    public static long getDateMinusDays(Date currDate, String payDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = sdf.parse(payDate);
        } catch (ParseException e) {
            d = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currDate);
        long timethis = calendar.getTimeInMillis();
        calendar.setTime(d);
        long timeend = calendar.getTimeInMillis();
        long theday = (timeend - timethis) / (1000 * 60 * 60 * 24);
        return theday;
    }

    /**
     * @param time    时间字符串
     * @param pattern 格式
     * @return Date
     * @throws
     * @Title: forDateByPattern
     * @Description: 根据指定格式将字符串格式化成日期格式
     */
    public static String formatDateByPattern(Date time, String pattern) {
        SimpleDateFormat sf = new SimpleDateFormat(pattern);
        return sf.format(time);
    }

    /**
     * @Title: formatDate
     * @Description: 将一个日期格式化成对应格式的日期
     */
    public static Date formatDate(Date time, String pattern) {
        String formatTime = formatDateByPattern(time, pattern);
        return formatStringByPattern(formatTime, pattern);
    }

    public static Date formatStringByPattern(String time) {
        return formatStringByPattern(time, DATE_TIME_PATTERN);
    }

    /**
     * @param time    时间字符串
     * @param pattern 格式
     * @return Date
     * @throws
     * @Title: forDateByPattern
     * @Description: 根据指定格式将字符串格式化成日期格式
     */
    public static Date formatStringByPattern(String time, String pattern) {
        Date date = null;
        try {
            date = DateUtils.parseDate(time, new String[]{pattern});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * @param @param  birthday
     * @param @return 设定文件
     * @return Short 返回类型
     * @throws
     * @Title: getAgeByBirthday
     * @Description: 根据生日获取年龄
     */
    public static Short getAgeByBirthday(Date birthday) {
        String birthdayYear = convertDate(birthday, 3);
        String currYear = convertDate(new Date(), 3);
        short age = (short) (Integer.parseInt(currYear) - Integer.parseInt(birthdayYear));
        return (short) (age > 0 ? age + 1 : 0);
    }

    /**
     * @param date 对比的日期
     * @return Integer 对比后返回值
     * @throws
     * @Title: compareDate
     * @Description: 和系统日期对比
     */
    public static Integer compareDate(String date) {
        DateFormat df = new SimpleDateFormat(DATE_PATTERN);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c1.setTime(formatDate(new Date(), DATE_PATTERN));
            c2.setTime(df.parse(date));

        } catch (ParseException e) {

            System.err.println("格式不正确");
        }
        int result = c1.compareTo(c2);
        return result;
    }

    /**
     * @param @param  currDate
     * @param @param  timeInterval
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     * @Title: getDayByMonth
     * @Description: 获取某个日期后N个月的日期
     */
    public static Date getDayByMonth(Date currDate, int timeInterval) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currDate);
        calendar.add(Calendar.MONTH, timeInterval);
        return calendar.getTime();
    }

    /**
     * @param time 对比的时间
     * @return Integer 对比后返回值
     * @throws
     * @Title: compareDate
     * @Description: 和系统时间对比
     */
    public static Integer compareTime(String time) {
        DateFormat df = new SimpleDateFormat("HH:mm");
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c1.setTime(formatDate(new Date(), "HH:mm"));
            c2.setTime(df.parse(time));
        } catch (ParseException e) {
            System.err.println("格式不正确");
        }
        int result = c1.compareTo(c2);
        return result;
    }

    public static long compareTimeForValue(String DateTime, int timeType) {
        DateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long value = 0;
        long num = 0;
        try {
            Date dt1 = new Date();
            Date dt2 = dff.parse(DateTime);
            num = (long) (dt2.getTime() - dt1.getTime());
            switch (timeType) {
                case 1:
                    // 天
                    value = num / (24 * 60 * 60 * 1000);
                    break;
                case 2:
                    // 小时
                    value = num / (1000 * 60 * 60);
                    break;
                case 3:
                    // 分
                    value = num / (1000 * 60);
                    break;
                case 4:
                    // 秒
                    value = num / (1000);
                    break;
                default:
                    // 毫秒
                    value = num;
                    break;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return value;
    }

    // String类型时间对比
    public static boolean compareTime(String bigTime, String smartTime) {
        long bigTimeLong = Long.valueOf(bigTime.replaceAll("[-\\s:]", ""));
        long smartTimeLong = Long.valueOf(smartTime.replaceAll("[-\\s:]", ""));
        if (bigTimeLong >= smartTimeLong) {
            return true;
        }
        return false;
    }

    // 获得昨天
    public static String getYesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        return convertDate(calendar.getTime(), 1);
    }

    // 获得3天前
    public static String getThreeDayAgo() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -2);
        return convertDate(calendar.getTime(), 1);
    }

    // 获取一星期前
    public static String getweekend() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -6);
        return convertDate(calendar.getTime(), 1);
    }

    // 获取15天前
    public static String getFifteenAgo() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -14);
        return convertDate(calendar.getTime(), 1);
    }

    // 获取一月前
    public static String getmonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        return convertDate(calendar.getTime(), 1);
    }

    // 获取二十年前
    public static String getOldYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -20);
        return convertDate(calendar.getTime(), 1);
    }

    public static String getFirstDayInYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(1), 0, 1);
        return convertDate(calendar.getTime(), 1);
    }

    // 当前时间加1年
    public static String getNextyear() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, +1);
        return convertDate(calendar.getTime(), 0);
    }

    // 获得未来x天的日期时间。
    public static Date getNextyearMinuteTime(Integer minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    public static String getNextyearMinuteTimeString(Integer minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minute);
        return convertDate(calendar.getTime(), 0);
    }

    // 获得明天零时yyyy-MM-dd HH:mm:ss
    public static String getNextday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, 1);
        return convertDate(calendar.getTime(), 0);
    }

    // 获得未来x天的日期时间。
    public static String getFutureXDateTime(String dateTime, Integer day) {
        return getFutureXDateTime(DateUtil.covertStrToDateMore(dateTime), day);
    }

    public static String getFutureXDateTime(Date date, Integer day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);
        return convertDate(calendar.getTime(), 9);
    }

    public static String getFutureXDateTime(Integer day) {
        return getFutureXDateTime(new Date(), day);
    }

    // 获取几天后的时间
    public static Date getFutureDate(Integer day) {
        return getFutureDate(day, new Date());
    }

    public static Date getFutureDate(Integer day, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);
        return calendar.getTime();
    }

    // 获得未来x小时的日期时间。
    public static String getFutureXHourTime(String dateTime, Integer hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.covertStrToDateMore(dateTime));
        calendar.add(Calendar.HOUR, hours);
        return convertDate(calendar.getTime(), 9);
    }

    /**
     * 得到当前时间一周后的时间
     *
     * @return
     */
    public static Map<String, String> getCurrWeek() {
        Map<String, String> map = new TreeMap<String, String>();
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i <= 6; i++) {
            cal.add(Calendar.DAY_OF_WEEK, 1);
            map.put(getDateFormate(cal.getTime(), "MM-dd"), getWeek(cal.getTime()));
        }

        return map;
    }

    /**
     * 返回周几
     *
     * @param date
     * @return
     */
    public static String getWeek(Date date) {
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Integer i = cal.get(Calendar.DAY_OF_WEEK);
        return weekDays[i - 1];
    }

    /**
     * 返回周几
     *
     * @param date
     * @return
     */
    public static Integer getNextWeek(Date date) {
        Integer[] weekDays = {0, 1, 2, 3, 4, 5, 6};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Integer i = cal.get(Calendar.DAY_OF_WEEK);
        return weekDays[i - 1];
    }

    /**
     * 本地排班返回周几
     *
     * @param date
     * @return
     */
    public static Integer getNextWeekLocal(Date date) {
        Integer[] weekDays = {7, 1, 2, 3, 4, 5, 6};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Integer i = cal.get(Calendar.DAY_OF_WEEK);
        return weekDays[i - 1];
    }

    /**
     * 得到时间格式
     *
     * @return
     */
    public static String getDateFormate(Date date, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    public static Long getLongByStringTime(String str) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return simpleDateFormat.parse(str).getTime();

    }

    // 获得昨天
    public static String getYesterday(String time, int num) {
        Date datetime = covertStrToDate(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datetime);
        calendar.add(Calendar.DATE, -num);
        return DateUtil.convertDate(calendar.getTime(), 1);
    }

    // 获得明天
    public static String getnextrday(String time, int num) {
        Date datetime = covertStrToDate(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datetime);
        calendar.add(Calendar.DATE, +num);
        return DateUtil.convertDate(calendar.getTime(), 1);
    }

    public static String getNowYear() {

        String format = "yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String newDate = sdf.format(new Date());
        return newDate;
    }

    // 获得7天后日期
    public static String sevenTime(int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, num);
        String time = getDateFormate(calendar.getTime(), "yyyy-MM-dd");
        return time;
    }

    // 获得几天天后日期
    public static String nextTime(int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, num);
        String time = getDateFormate(calendar.getTime(), "MM-dd");
        return time;
    }

    // 获得几天天后日期
    public static String nextTime2(int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, num);
        String time = getDateFormate(calendar.getTime(), "YYYY-MM-dd");
        return time;
    }

    public static String time(String time) {
        String aa = time.substring(0, 4);
        String bb = time.substring(4, 6);
        String cc = time.substring(6, 8);
        String dd = aa + "-" + bb + "-" + cc;
        return dd;

    }

    public static boolean isleafyear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0) ? true : false;
    }

    /**
     * @param @param  startDate
     * @param @param  endDate
     * @param @return
     * @param @throws ParseException
     * @return Integer 返回类型
     * @throws
     * @Title: getDateMinusDays
     * @Description: 获取两个字符串日期相差天数(包含前后)
     */
    public static long getEndDateMinusStartDate(String startDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date start = null;
        Date end = null;
        try {
            start = sdf.parse(startDate);
            end = sdf.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        long timeStart = calendar.getTimeInMillis();
        calendar.setTime(end);
        long timeEnd = calendar.getTimeInMillis();
        long theday = (timeEnd - timeStart) / (1000 * 60 * 60 * 24);
        return theday + 1;
    }

    /**
     * 获取下一个月的第几天
     *
     * @param curTime
     * @param day
     * @return
     */
    public static String getOneDayOfNextMonth(String curTime, int day) {
        Date curDate = covertStrToDate(curTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(curDate);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return convertDate(calendar.getTime(), -1);
    }

    /**
     * 得到某年某月的第一天日期和最后一天的日期
     *
     * @param type  1:第一天；2：最后一天
     * @param year
     * @param month
     * @return 日期字符串
     */
    public static String getFirstOrLastDayOfMonth(int type, int year, int month) {
        String result = "";
        String monthStr = month >= 10 ? "" + month : "0" + month;
        if (type == 1) { // 第1天
            result = year + "-" + monthStr + "-01";
        } else {// 最后一天
            Calendar cal = Calendar.getInstance();
            // 设置年份
            cal.set(Calendar.YEAR, year);
            // 设置月份
            cal.set(Calendar.MONTH, month - 1);
            // 获取某月最大天数
            int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            result = year + "-" + monthStr + "-" + lastDay;
        }
        return result;
    }

    /**
     * @param type 1:第一天；2：最后一天
     * @return
     * @Title: getCurMonthFirstOrLastDayOfMonth
     * @Description: 得到当前月的第一天日期和最后一天的日期
     */
    public static String getCurMonthFirstOrLastDayOfMonth(int type) {
        String result = "";
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1;
        result = getFirstOrLastDayOfMonth(type, year, month);
        return result;
    }

    /**
     * @param type  1日2月3年
     * @param value
     * @return
     * @Title: getFutureDateByType
     * @Description: 得到未来的日期
     */
    public static String getFutureDateByType(int type, int value) {
        String result = "";
        Calendar calendar = Calendar.getInstance();
        if (type == 1) {
            calendar.add(Calendar.DATE, value);
        }
        if (type == 2) {
            calendar.add(Calendar.MONTH, value);
        }
        if (type == 3) {
            calendar.add(Calendar.YEAR, value);
        }
        Date resultDate = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        result = sdf.format(resultDate);
        return result;
    }

    /**
     * 获取当前时间毫秒级别
     *
     * @return
     */
    public static String getNowDateMillisecond() {

        String format = "yyyy-MM-dd HH:mm:ss.SSS";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String newDate = sdf.format(new Date());
        return newDate;
    }

    public static boolean isValidDate(String str) {
        boolean convertSuccess = true;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
        try {
            // 设置lenient为false.
            // 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }
        return convertSuccess;
    }

    // 获得未来x分钟的日期时间。
    public static Date getFutureXMinTime(String dateTime, Integer minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.covertStrToDateMore(dateTime));
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    /**
     * 日期格式字符串转换成时间戳 单位秒
     *
     * @param date_str 字符串日期
     * @param format   如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static long date2TimeStamp(String date_str, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(date_str).getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 订单支付时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010
     *
     * @param datetime
     * @param parttern
     * @return
     */
    public static LocalDateTime convertToLocalDateTime(String datetime, String parttern) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(parttern);
        return LocalDateTime.parse(datetime, dtf);
    }

    /**
     * YYYYMMDD格式转化为LocalDateTime
     *
     * @param date
     * @return
     */
    public static LocalDateTime parseLocalDateTimeYYYYMMDD(String date) {
        DateTimeFormatter sd = DateTimeFormatter.ofPattern(DATE_PATTERN);
        LocalDate parse = LocalDate.parse(date, sd);
        return parse.atStartOfDay();
    }

    /**
     * yyyy-MM-dd HH:mm:ss格式转化为LocalDateTime
     *
     * @param datetime
     * @return
     */
    public static LocalDateTime parseDateTime(String datetime) {
        return convertToLocalDateTime(datetime, DATE_TIME_PATTERN);
    }

    /**
     * 格式化日期为yyyy-MM-dd HH:mm:ss
     *
     * @param data
     * @return
     */
    public static String formatDateTime(Object data) {
        if (data instanceof Date || data instanceof Timestamp) {
            Date endTimeDate = (Date) data;
            return formatDateByPattern(endTimeDate, DATE_TIME_PATTERN);
        } else if (data instanceof String) {
            return String.valueOf(data).substring(0, 19);
        } else if (data instanceof LocalDateTime) {
            LocalDateTime time = (LocalDateTime) data;
            return time.format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
        }
        return formatDateByPattern(new Date(), DATE_TIME_PATTERN);
    }


    /**
     * 获取当前日期是当前月的第几天
     * @param date
     * @return
     */
    public static int getDayOfMonth(Date date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        return ca.get(Calendar.DAY_OF_MONTH);
    }

    public static Date getBeginOfMonth(Date createTime) throws ParseException {
        Calendar c = Calendar.getInstance();
        c.setTime(createTime);
        String firstOrLastDayOfMonth = getFirstOrLastDayOfMonth(1, c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = firstOrLastDayOfMonth + " 00:00:00";
        return  sdf.parse(s);
    }

    public static Date getEndOfMonth(Date createTime) throws ParseException {
        Calendar c = Calendar.getInstance();
        c.setTime(createTime);
        String firstOrLastDayOfMonth = getFirstOrLastDayOfMonth(2, c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = firstOrLastDayOfMonth + " 23:59:59";
        return  sdf.parse(s);
    }


    /**
     * 获取当前时间到凌晨12点的秒数
     * @return
     */
    public static long getSecondsNextEarlyMorning() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }
}
