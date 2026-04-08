package com.lvxingyouyang.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 日期工具类
 * 提供日期时间的格式化和解析功能
 */
public class DateUtil {
    /** ISO 8601 日期时间格式化器 */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    /**
     * 将 LocalDateTime 格式化为字符串
     * @param dateTime 日期时间对象
     * @return 格式化后的日期时间字符串
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(formatter);
    }

    /**
     * 将字符串解析为 LocalDateTime
     * @param dateTimeStr 日期时间字符串
     * @return LocalDateTime 对象
     */
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        return LocalDateTime.parse(dateTimeStr, formatter);
    }
}
