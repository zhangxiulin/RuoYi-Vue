package com.ruoyi.open.annotation;

import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.OperatorType;

import java.lang.annotation.*;

/**
 * 自定义开放模块操作日志记录注解
 * 
 * @author 张秀林
 *
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OpenLog
{
    /**
     * 模块 
     */
    public String title() default "";

    /**
     * 功能
     */
    public BusinessType businessType() default BusinessType.OTHER;

    /**
     * 操作人类别
     */
    public OperatorType operatorType() default OperatorType.MANAGE;

    /**
     * 是否保存请求的参数
     */
    public boolean isSaveRequestData() default true;
}
