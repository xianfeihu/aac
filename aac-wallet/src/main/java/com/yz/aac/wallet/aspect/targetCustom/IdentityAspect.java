package com.yz.aac.wallet.aspect.targetCustom;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义IdentityAspect注解标签
 * 标识实名身份本地校验
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IdentityAspect {


}
