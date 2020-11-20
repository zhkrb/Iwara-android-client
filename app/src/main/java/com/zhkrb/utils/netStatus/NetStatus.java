package com.zhkrb.utils.netStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @description：注解
 * @author：zhkrb
 * @DATE： 2020/8/18 9:24
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NetStatus{

}
