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

    public static String genRandomCharAndNumr(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++){
            if (random.nextBoolean()){
                sb.append((char)(65+random.nextInt(26)));
            }else {
                sb.append(String.valueOf(random.nextInt(10)));
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(genRandomNum(2));

        System.out.println(genRandomCharAndNumr(4));
    }

}
