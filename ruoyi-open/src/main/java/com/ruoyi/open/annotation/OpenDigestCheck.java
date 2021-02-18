package com.ruoyi.open.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description:  校验 http digest 认证是否通过
 * @author: zhangxiulin
 * @time: 2021/2/16 12:49
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OpenDigestCheck {

    /**
     * 返回值类型
     */
    Class rtType() default void.class;

    /**
     * 失败返回值，如果想自定义错误信息，请搭配返回值类型使用
     */
    String rtErrValue() default "";

}
