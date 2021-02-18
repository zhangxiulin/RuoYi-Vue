package com.ruoyi.open.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IOpenLimitService {

    boolean forwardLimit(HttpServletRequest httpServletRequest, HttpServletResponse httpResponse, String forwardCode);

    boolean aggrLimit(HttpServletRequest httpServletRequest, HttpServletResponse httpResponse, String aggrCode);

}
