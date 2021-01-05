package com.ruoyi.common.annotation;

import com.ruoyi.common.enums.DataSourceType;

import java.lang.annotation.*;

/**
 * @description: 自定义多运行时数据源切换注解
 * @author: zhangxiulin
 * @time: 2020/12/11 12:55
 */
@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RuntimeDataSource {

    /**
     * 切换数据源名称
     */
    public String value() default "";

}
