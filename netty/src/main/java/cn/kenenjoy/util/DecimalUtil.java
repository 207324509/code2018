package cn.kenenjoy.util;

import java.text.DecimalFormat;

/**
 * Created by hefa on 2018/1/11.
 */
public class DecimalUtil {
    private static final DecimalFormat df = new DecimalFormat("######0.00");

    public static String formatDouble(Object object) {
        return df.format(object);
    }
}
