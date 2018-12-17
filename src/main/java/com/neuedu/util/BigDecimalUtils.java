package com.neuedu.util;

import java.math.BigDecimal;

/**
 * 价格计算工具
 */
public class BigDecimalUtils {

    /**
     * 加法运算
     */
    public static BigDecimal add(double d1,double d2){
        BigDecimal bigDecimal=new BigDecimal(String.valueOf(d1));
        BigDecimal bigDecimal2=new BigDecimal(String.valueOf(d2));
        return bigDecimal.add(bigDecimal);
    }

    /**
     * 减法运算
     */

    public static BigDecimal sub(double d1,double d2){
        BigDecimal bigDecimal=new BigDecimal(String.valueOf(d1));
        BigDecimal bigDecimal2=new BigDecimal(String.valueOf(d2));
        return bigDecimal.subtract(bigDecimal);
    }

    /**
     * 乘法运算
     */

    public static BigDecimal mul(double d1,double d2){
        BigDecimal bigDecimal=new BigDecimal(String.valueOf(d1));
        BigDecimal bigDecimal2=new BigDecimal(String.valueOf(d2));
        return bigDecimal.multiply(bigDecimal);
    }

    /**
     * 除法运算  保留两位小数  四舍五入
     * @param d1
     * @param d2
     * @return
     */

    public static BigDecimal div(double d1,double d2){
        BigDecimal bigDecimal=new BigDecimal(String.valueOf(d1));
        BigDecimal bigDecimal2=new BigDecimal(String.valueOf(d2));
        return bigDecimal.divide(bigDecimal,2,BigDecimal.ROUND_HALF_UP);
    }

}
