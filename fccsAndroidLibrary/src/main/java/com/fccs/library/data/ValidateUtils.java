/**
 * 项目名：FccsAndroidLibrary
 * Copyright © 2016 浙江房超信息科技有限公司.All Rights Reserved.
 */
package com.fccs.library.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类名：ValidateUtils<br>
 * 类描述：验证工具<br>
 * 创建人：倪少强<br>
 * 创建日期：2016年4月12日 上午9:23:39
 */
public class ValidateUtils {

	/**
	 * 匹配中文
	 * 添加者：倪少强
	 * 添加时间：2016年4月12日 上午9:23:56
	 * @param input
	 * @return：
	 */
    public static boolean isChinese(String input) {
        return matches("[\u4e00-\u9fa5]{1,}", input);
    }

    /**
     * 匹配Email
     * 添加者：倪少强
     * 添加时间：2016年4月12日 上午9:24:08
     * @param input
     * @return：
     */
    public static boolean isEmail(String input) {
        if (input.length() < 6) {
            return false;
        }
        return matches(
                "^([a-z0-9A-Z]+[-|\\.|_]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$",
                input);
    }

    /**
     * 匹配18位身份证号码
     * 添加者：倪少强
     * 添加时间：2016年4月12日 上午9:24:18
     * @param input
     * @return：
     */
    public static boolean isIDCardNumber(String input) {
        if (input.length() != 18) {
            return false;
        }
        if (matches("^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X)$",
                input)) {
            int year = Integer.valueOf(input.substring(6, 10));
            int month = Integer.valueOf(input.substring(10, 12));
            int date = Integer.valueOf(input.substring(12, 14));
            if ((year >= 1864 && year <= 2016) && (month > 0 && month <= 12)
                    && (date > 0 && date <= 31)) {
                if (month == 4 || month == 6 || month == 9 || month == 11) {
                    if (date <= 30) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (month == 2) {
                    if (((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0))) {
                        if (date <= 29) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        if (date <= 28) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                } else {
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 匹配手机号码
     * 添加者：倪少强
     * 添加时间：2016年4月12日 上午9:24:29
     * @param input
     * @return：
     */
    public static boolean isMobile(String input) {
        if (input.length() != 11) {
            return false;
        }
        if (input.startsWith("1") && (!input.startsWith("10"))
                && (!input.startsWith("11")) && (!input.startsWith("12"))
                && (!input.startsWith("16")) && (!input.startsWith("19"))) {
            return matches("^[0-9]\\d*$", input);
        } else {
            return false;
        }
    }

    /**
     * 判断是否是URL
     * 添加者：倪少强
     * 添加时间：2016年4月12日 上午9:24:45
     * @param input
     * @return：
     */
    public static boolean isURL(String input){
        return matches("(http|ftp|https)://[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?", input);
    }

    private static boolean matches(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    /**
     * 判断是否为纯数字
     * 添加者：倪少强
     * 添加时间：2016年4月12日 上午9:25:00
     * @param input
     * @return：
     */
    public static boolean isNumberOnly(String input) {
    	return matches("^[0-9]\\d*$", input);
    }
    
}
