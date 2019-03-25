package com.yz.aac.mining.aspect.targetCustom;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义PageControllerAspect注解标签
 * controller分页数据封装
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PageControllerAspect {


}
