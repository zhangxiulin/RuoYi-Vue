package com.ruoyi.common.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @description:
 * @author: zhangxiulin
 * @time: 2021/1/26 21:40
 */
public class JsonUtils {

    public static Map<String, Object> json2Map(Map jsonObject) {
        Map<String, Object> map = new HashMap<>();
        Iterator iterator = jsonObject.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iterator.next();
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

}
