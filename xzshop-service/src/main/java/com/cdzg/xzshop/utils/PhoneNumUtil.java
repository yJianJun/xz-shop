package com.cdzg.xzshop.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author liqiang
 */
public final class PhoneNumUtil {

    private static final Pattern phonePattern = Pattern.compile("^(1[3-9])\\d{9}$");

    public static boolean isPhoneValid(String phone) {
        return phonePattern.matcher(phone).matches();
    }
    /**
     * 验证输入的邮箱格式是否符合
     * @param email
     * @return 是否合法
     */
    public static boolean emailFormat(String email)
    {
        boolean tag = true;
        final String pattern1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        final Pattern pattern = Pattern.compile(pattern1);
        final Matcher mat = pattern.matcher(email);
        if (!mat.find()) {
            tag = false;
        }
        return tag;
    }

    /**
     * 校验字符串是全是数字
     * @param str
     * @return
     */
    public static boolean isNumers(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}
