package com.originlang.webmvc.advice;

import java.lang.annotation.ElementType;

/**
 * 忽略统一响应结果注解, 用于Controller方法上,忽略统一响应结果处理
 */
@java.lang.annotation.Target({ ElementType.METHOD, ElementType.TYPE }) // 类和方法上都可以使用
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
public @interface IgnoreResponseResult {

}
