package com.yz.aac.wallet.aspect.targetCustom;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义VCodeAspect注解标签(VerificationCode)
 * 短信验证码时效验证
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface VCodeAspect {


}
