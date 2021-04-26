package com.fan.componentization.apt_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description:
 * @Author: shanhongfan
 * @Date: 2021/4/23 14:42
 * @Modify:
 */

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface ARouter {

    String path() default "";
}
