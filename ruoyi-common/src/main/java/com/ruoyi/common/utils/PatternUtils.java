package com.ruoyi.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: 正则工具类
 * @author: zhangxiulin
 * @time: 2020/12/17 21:33
 */
public class PatternUtils {

    public static List<String> getMatchedStrs(String reg, String str){
        Pattern patten = Pattern.compile(reg);//编译正则表达式
        Matcher matcher = patten.matcher(str);// 指定要匹配的字符串
        List<String> matchStrs = new ArrayList<>();
        while (matcher.find()) { //此处find（）每次被调用后，会偏移到下一个匹配
            matchStrs.add(matcher.group());//获取当前匹配的值
        }
        return matchStrs;
    }

    public static void main(String[] args) {
        getMatchedStrs("\\$\\{[a-zA-Z0-9_]+\\}", "select * from order where createdUser = ${currentUser_} and  depart = ${currentOrg} and status = 'VALID'");
    }
}
