package com.ruoyi.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @description:
 * @author: zhangxiulin
 * @time: 2021/1/23 17:14
 */
public class CalendarUtils {

    /**
     * 指定毫秒数的过期时间
     */
    public static String getExpireTimeStr(int delayMs) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MILLISECOND, delayMs);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(calendar.getTime());
    }

}
