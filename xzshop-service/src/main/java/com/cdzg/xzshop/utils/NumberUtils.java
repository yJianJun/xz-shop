package com.cdzg.xzshop.utils;

import java.math.BigDecimal;

/**
 * 数字计算
 */
public class NumberUtils {


    /**
     * 除法运算
     * @param v1 被除数
     * @param v2 除数
     * @param scale 保留小数位
     * @return
     */
    public static Double div(int v1, int v2, int scale){
        if (v2 == 0) {
            throw new IllegalArgumentException("Divisor cannot be equal to zero");
        }
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = BigDecimal.valueOf(v1);
        BigDecimal b2 = BigDecimal.valueOf(v2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    /**
     * 除法运算
     * @param v1 被除数
     * @param v2 除数
     * @param scale 保留小数位
     * @return
     */
    public static Double div(long v1, long v2, int scale){
        if (v2 == 0) {
            throw new IllegalArgumentException("Divisor cannot be equal to zero");
        }
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = BigDecimal.valueOf(v1);
        BigDecimal b2 = BigDecimal.valueOf(v2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 除法运算，默认保留两位小数
     * @param v1 被除数
     * @param v2 除数
     * @return
     */
    public static Double div(int v1, int v2){
        return div(v1, v2, 2);
    }

    /**
     * 除法运算，默认保留两位小数
     * @param v1 被除数
     * @param v2 除数
     * @return
     */
    public static Double div(long v1, long v2){
        return div(v1, v2, 2);
    }


}
