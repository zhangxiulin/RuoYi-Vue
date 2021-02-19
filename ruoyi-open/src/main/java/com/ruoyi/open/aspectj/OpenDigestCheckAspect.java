package com.ruoyi.open.aspectj;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.open.annotation.OpenDigestCheck;
import com.ruoyi.open.service.IOpenDigestService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @description: 检验http digest认证是否通过切面
 * @author: zhangxiulin
 * @time: 2021/2/16 0:17
 */
@Aspect
@Component
public class OpenDigestCheckAspect {

    private static final Logger log = LoggerFactory.getLogger(OpenDigestCheckAspect.class);

    // 配置织入点
    @Pointcut("@annotation(com.ruoyi.open.annotation.OpenDigestCheck)")
    public void openDigestCheckPointCut()
    {
    }

    @Around("openDigestCheckPointCut()")
    public Object doBefore(ProceedingJoinPoint point) throws Throwable
    {
        return handleOpenDigestCheck(point);
    }

    protected Object handleOpenDigestCheck(ProceedingJoinPoint point) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        IOpenDigestService digestService = SpringUtils.getBean(IOpenDigestService.class);
        int authRt = digestService.auth(ServletUtils.getRequest(), ServletUtils.getResponse());
        Method invokedMethod = methodSignature.getMethod();
        String invokedMethodName = invokedMethod.getName();
        Annotation[] declaredAnnotations = invokedMethod.getDeclaredAnnotations();
        StringBuilder declaredAnnotationSb = new StringBuilder();
        if (declaredAnnotations != null && declaredAnnotations.length > 0){
            for (int i = 0; i < declaredAnnotations.length; i++) {
                declaredAnnotationSb.append(declaredAnnotations[i].toString()).append(",");
            }
        }
        String declaredAnnotationStr = "";
        if (declaredAnnotationSb.length() > 0) {
            declaredAnnotationStr = declaredAnnotationSb.substring(0, declaredAnnotationSb.length()-1);
        }
        if (authRt == HttpStatus.SUCCESS) {
            log.info("认证成功, 切点：{} 注解：{}", invokedMethodName, declaredAnnotationStr);
            return point.proceed();
        } else {
            log.error("认证失败, 切点：{} 注解：{}", invokedMethodName, declaredAnnotationStr);
            // 先判断注解内有没有指定返回值和返回值类型，若未指定，以默认
            OpenDigestCheck odcAnnotation = invokedMethod.getAnnotation(OpenDigestCheck.class);
            Class odcRtType = odcAnnotation.rtType();
            String odcRtErrValue = odcAnnotation.rtErrValue();
            if (odcRtType != void.class && !"".equals(odcRtErrValue)) {
                if (odcRtType == String.class) {
                    return odcRtErrValue;
                } else {
                    return JSONObject.parseObject(odcRtErrValue, odcRtType);
                }
            } else {
                Class<?> rtType = methodSignature.getMethod().getReturnType();
                if (rtType == AjaxResult.class) {
                    return AjaxResult.error(HttpStatus.UNAUTHORIZED, "认证失败，授权信息请参照响应头");
                } else if (rtType == void.class) {
                    return AjaxResult.error(HttpStatus.UNAUTHORIZED, "认证失败，授权信息请参照响应头");
                } else if (rtType == int.class) {
                    return 401;
                } else if (rtType == long.class) {
                    return 401;
                } else if (rtType == short.class) {
                    return 401;
                } else if (rtType.getSuperclass().equals(Number.class)) {
                    return 401;
                }else {
                    return null;
                }
            }
        }
    }

}
