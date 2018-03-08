package com.foodBasket.util;

import java.text.DecimalFormat;

/**
 * Created by programmer on 2018/3/8.
 */

public final class NumberUtil {
    public static String decimalFormat(float num) {
        //使用0.00不足位补0，#.##仅保留有效位
        return new DecimalFormat("0.00").format(num);
    }

    public static String decimalFormat(Double num) {
        //使用0.00不足位补0，#.##仅保留有效位
        return new DecimalFormat("0.00").format(num);
    }


}
