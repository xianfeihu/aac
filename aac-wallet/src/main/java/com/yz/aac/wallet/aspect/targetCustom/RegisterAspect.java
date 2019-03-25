package com.yz.aac.wallet.aspect.targetCustom;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义RegisterAspect注解标签
 * app用户注册
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RegisterAspect {


}
