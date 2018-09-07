package com.xl.canary.utils;

import java.util.regex.Pattern;

/**
 * 数学运算符工具类
 * Created by xzhang on 2018/9/7.
 */
public class ArithmeticOperatorUtils {

    /**
     * 匹配所有数字
     */
    private static final Pattern NUMBER_PATTERN = Pattern.compile("-?[0-9]+.?[0-9]+");

    /**
     * 匹配所有整数
     */
    private static final Pattern INTEGER_PATTERN = Pattern.compile("-?[\\d]*");

    /**
     * 判断字符串是不是数字
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        return NUMBER_PATTERN.matcher(str).matches();
    }

    /**
     * 判断字符串是不是整数
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        return INTEGER_PATTERN.matcher(str).matches();
    }
}
