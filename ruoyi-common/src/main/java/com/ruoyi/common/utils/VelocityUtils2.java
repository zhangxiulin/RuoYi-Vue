package com.ruoyi.common.utils;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;

/**
 * @description:
 * @author: zhangxiulin
 * @time: 2020/12/17 14:40
 */
public class VelocityUtils2 {

    private static VelocityEngine velocityEngine = new VelocityEngine();

    static {
        velocityEngine.init();
    }

    public static String evaluate(String content, VelocityContext velocityContext){
        StringWriter writer = new StringWriter();
        velocityEngine.evaluate(velocityContext, writer, "", content);
        return writer.toString();
    }

    public static VelocityContext getVelocityContext(Map data){
        VelocityContext velocityContext = new VelocityContext();
        Iterator<String> keys = data.keySet().iterator();
        while (keys.hasNext()){
            String key = keys.next();
            Object val = data.get(key);
            velocityContext.put(key, val);
        }
        return velocityContext;
    }

    public static String evaluate(String content, Map vars){
        return evaluate(content, getVelocityContext(vars));
    }

}
