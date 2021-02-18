package com.ruoyi.open.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description:  HTTP认证之摘要认证
 * @author: zhangxiulin
 * @time: 2021/1/27 21:36
 */
public interface IOpenDigestService {

    int auth(HttpServletRequest httpServletRequest, HttpServletResponse httpResponse);

}
