package com.lingong.mytest.utils;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: Miracle.Lin
 * @Date:2021/10/8
 * @Desc: 日期格式工具：
 * <p>
 * 每月的一号，二号，三号分别是1st, 2nd, 3rd。
 * 二十一，二十二，二十三是21st, 22nd, 23rd。
 * 三十一是31st。
 * 其他的日期只要在数字后面加th就可以了（包括十一eleventh ，十二twelfth，十三thirteenth）
 * @deprecated
 * TextClock 控件无法自定义text ，只能修改 time_format 而 h 会被转义成 hour小时，d会被转义成 day
 */
public class TimeDateUtils {


    /**
     * 添加序数词
     *
     * @return
     */
    public static String appendOrdinal(String date) {
        if (!Locale.getDefault().getLanguage().equalsIgnoreCase("en"))
            return date;
        else {//英语下才生效
            LogUtil.d("appendOrdinal =  date = " + date);

            String finialOrd = convert(getNumbers(date));
            LogUtil.d("finialOrd = " + finialOrd);
            return date.replace(",", finialOrd);
        }
    }


    //截取数字
    static String getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    static String convert(String str) {
        switch (str) {
            case "1":
            case "21":
            case "31":
                return "st,";
            case "2":
            case "22":
                return "nd,";
            case "3":
            case "23":
                return "rd,";
            default:
                return "th,";

        }
    }

}
