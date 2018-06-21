package com.test.butterknife;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)

@Retention(RetentionPolicy.RUNTIME)

public @interface EventType {

    Class<?> listenerType();  // 监听接口的类型

    String listenerSetter();  // 设置监听接口的 set 方法

    String methodName();      // 监听接口中的回调方法名称

}