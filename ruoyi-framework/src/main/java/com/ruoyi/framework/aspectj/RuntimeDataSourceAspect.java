package com.ruoyi.framework.aspectj;

import com.ruoyi.common.annotation.RuntimeDataSource;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.datasource.runtime.RuntimeDynamicDataSourceContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @description: 运行时多数据源处理，拦截事务方法的切面
 * @author: zhangxiulin
 * @time: 2020/12/11 22:29
 */
@Aspect
@Order(1)
@Component
public class RuntimeDataSourceAspect {

    @Pointcut("@annotation(com.ruoyi.common.annotation.RuntimeDataSource)"
            + "|| @within(com.ruoyi.common.annotation.RuntimeDataSource)")
    public void dsPointCut()
    {

    }

    @Around("dsPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable
    {
        RuntimeDataSource runtimeDataSource = getDataSource(point);

        if (StringUtils.isNotNull(runtimeDataSource))
        {
            RuntimeDynamicDataSourceContextHolder.setDataSourceType(runtimeDataSource.value());
        }

        try
        {
            return point.proceed();
        }
        finally
        {
            // 销毁数据源 在执行方法之后
            RuntimeDynamicDataSourceContextHolder.clearDataSourceType();
        }
    }

    /**
     * 获取需要切换的数据源
     */
    public RuntimeDataSource getDataSource(ProceedingJoinPoint point)
    {
        MethodSignature signature = (MethodSignature) point.getSignature();
        RuntimeDataSource runtimeDataSource = AnnotationUtils.findAnnotation(signature.getMethod(), RuntimeDataSource.class);
        if (Objects.nonNull(runtimeDataSource))
        {
            return runtimeDataSource;
        }

        return AnnotationUtils.findAnnotation(signature.getDeclaringType(), RuntimeDataSource.class);
    }

}
