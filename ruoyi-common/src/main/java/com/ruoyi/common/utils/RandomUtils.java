package com.ruoyi.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * @description:
 * @author: zhangxiulin
 * @time: 2020/12/15 23:47
 */
public class RandomUtils {

    public static String genRandomString(String mainDate, int leftLength) {
        return (new SimpleDateFormat(mainDate)).format(new Date()) + UUID.randomUUID().toString().replaceAll("[-]", "").substring(0, leftLength);
    }

    public static int genRandomNum(int maxNum) {
        return Math.abs((new Random()).nextInt() % maxNum);
    }

}
