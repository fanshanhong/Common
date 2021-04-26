package com.fan.componentization.apt_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//注解类，只能用来对类进行注解
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface AppLifeCycle {
}