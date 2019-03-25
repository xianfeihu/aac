package com.yz.aac.exchange.aspect.targetCustom;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义IdentityAuthenticationAspect注解标签
 * 标识实名认证校验
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IdentityAuthenticationAspect {


}
