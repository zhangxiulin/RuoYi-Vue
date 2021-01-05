package com.ruoyi.common.utils;

import java.util.Map;
import java.util.Properties;

/**
 * @description: JDK Properties 工具类
 * @author: zhangxiulin
 * @time: 2020/12/13 3:02
 */
public class PropertiesUtils {

    /**
     * JSON转Properties
     *  为了兼容不同厂商的JSON工具包，参数使用Map
     */
    public static Properties json2Properties(Map<String, Object> map){
        Properties properties = new Properties();
        map.forEach( (k, v) -> {
            properties.setProperty(k, String.valueOf(v));
        });
        return properties;
    }

}
