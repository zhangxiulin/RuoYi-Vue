package com.ruoyi.common.core.domain;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: TCC Try 阶段返回报文
 * @author: zhangxiulin
 * @time: 2021/1/23 16:10
 */
public class TccTryAjaxResult extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;


    public static final String CONFIRM_URI = "confirmUri";

    public static final String EXPIRES = "expires";

    public static final String DATA = "data";

    public static final String SERIAL_NUMBER = "serialNumber";

    public static final String CANCEL_URI = "cancelUri";

    public TccTryAjaxResult(String confirmUri, String cancelUri, String expires, String data) {
        put(CONFIRM_URI, confirmUri);
        put(CANCEL_URI, cancelUri);
        put(EXPIRES, expires);
        put(DATA, data);
    }

    public static TccTryAjaxResult confirm(String confirmUri, String cancelUri, String expires, String serialNum) {
        return new TccTryAjaxResult(confirmUri, cancelUri, expires, JSON.toJSONString(new HashMap<String, Object>(){{put(SERIAL_NUMBER, serialNum);}}));
    }

    public static TccTryAjaxResult downcasting(Map<String, Object> map) {
        return new TccTryAjaxResult((String) map.get(CONFIRM_URI), (String) map.get(CANCEL_URI),
                (String) map.get(EXPIRES), (String) map.get(DATA));
    }

}
