package cn.kenenjoy.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hefa on 2018/1/11.
 */
public class DateUtils {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getNowDate() {
        Date currentTime = new Date();
        String nowDate = dateFormat.format(currentTime);
        return nowDate;
    }

    public static String getDateString(long time) {
        String nowDate = dateFormat.format(time);
        return nowDate;
    }
}
