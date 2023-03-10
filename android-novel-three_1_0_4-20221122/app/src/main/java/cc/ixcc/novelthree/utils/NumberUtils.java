package cc.ixcc.novelthree.utils;

import java.util.logging.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberUtils {
    int context;
    private static final Double THOUSAND = 1000.0;
    private static final Double MILLIONS = 1000000.0;
    private static final Double BILLION = 1000000000.0;
    private static final String THOUSAND_UNIT = "K";
    private static final String MILLION_UNIT = "M";
    private static final String BILLION_UNIT = "B";
    /**
     * 将数字转换成以万为单位或者以亿为单位，因为在前端数字太大显示有问题
     */
    public static String amountConversion(double amount) {
        //最终返回的结果值
        String result = String.valueOf(amount);
        //四舍五入后的值
        double value = 0;
        //转换后的值
        double tempValue = 0;
        //余数
        double remainder = 0;
        //大于1000小于1百万

        if (amount > THOUSAND && amount <= MILLIONS) {
            tempValue = amount/THOUSAND;
            remainder = amount % THOUSAND;
            //余数小于500则不进行四舍五入
            if (remainder < (THOUSAND / 2)) {
                value = formatNumber(tempValue, 2, false);
            } else {
                value = formatNumber(tempValue, 2, true);
            }
            //如果值刚好是1000000，则要变成1m
            if (value == THOUSAND) {
                result = zeroFill(value / THOUSAND) + MILLION_UNIT;
            } else {
                result = zeroFill(value) + THOUSAND_UNIT;
            }
        } else if (amount > MILLIONS && amount <= BILLION) {//大于1百万小于10亿
            tempValue = amount / MILLIONS;
            remainder = amount % MILLIONS;
            Logger.getLogger("tempValue=" + tempValue + ", remainder=" + remainder);

            //余数小于500000则不进行四舍五入
            if (remainder < (MILLIONS / 2)) {
                value = formatNumber(tempValue, 2, false);
            } else {
                value = formatNumber(tempValue, 2, true);
            }
            //如果值刚好是10000万，则要变成1亿
            if (value == MILLIONS) {
                result = zeroFill(value / MILLIONS) + BILLION_UNIT;
            } else {
                result = zeroFill(value) + MILLION_UNIT;
            }
        }
        //大于10亿
        else if (amount > BILLION) {
            tempValue = amount / BILLION;
            remainder = amount % BILLION;
            Logger.getLogger("tempValue=" + tempValue + ", remainder=" + remainder);

            //余数小于50000000则不进行四舍五入
            if (remainder < (BILLION / 2)) {
                value = formatNumber(tempValue, 2, false);
            } else {
                value = formatNumber(tempValue, 2, true);
            }
            result = zeroFill(value) + BILLION_UNIT;
        } else {
            result = zeroFill(amount);
        }
        Logger.getLogger("result=" + result);


        return result;
    }


    /**
     * 对数字进行四舍五入，保留2位小数
     *
     * @param number   要四舍五入的数字
     * @param decimal  保留的小数点数
     * @param rounding 是否四舍五入
     * @return
     */
    public static Double formatNumber(double number, int decimal, boolean rounding) {
        BigDecimal bigDecimal = new BigDecimal(number);

        if (rounding) {
            return bigDecimal.setScale(decimal, RoundingMode.HALF_UP).doubleValue();
        } else {
            return bigDecimal.setScale(decimal, RoundingMode.DOWN).doubleValue();
        }
    }

    /**
     * 对四舍五入的数据进行补0显示，即显示.00
     *
     * @return
     */
    public static String zeroFill(double number) {
        String value = String.valueOf(number);

        if (value.indexOf(".") < 0) {
            value = value + ".00";
        } else {
            String decimalValue = value.substring(value.indexOf(".") + 1);

            if (decimalValue.length() < 2) {
                value = value + "0";
            }
        }
        return value;
    }
}
