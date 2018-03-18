package com.studyhuang.utils;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * 日期转换类
 * Created by huang on 2018/3/18.
 */
public class DateTimeUtils {

    public  static final String  STRING_FORMATE="yyyy-MM-dd HH:mm:ss";

    public  static Date strToDate(String str,String formatDate){
        DateTimeFormatter  dateTimeFormatter = DateTimeFormat.forPattern(formatDate);
        DateTime dateTime = dateTimeFormatter.parseDateTime(str);
        return dateTime.toDate();
    }

    public static String dateToStr(Date date ,String formatDate){
        if (date == null){
          return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return  dateTime.toString(formatDate);
    }

    public  static Date strToDate(String str){
        DateTimeFormatter  dateTimeFormatter = DateTimeFormat.forPattern(STRING_FORMATE);
        DateTime dateTime = dateTimeFormatter.parseDateTime(str);
        return dateTime.toDate();
    }

    public static String dateToStr(Date date){
        if (date == null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return  dateTime.toString(STRING_FORMATE);
    }
}
