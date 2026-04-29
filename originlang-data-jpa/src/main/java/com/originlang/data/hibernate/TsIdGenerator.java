package com.originlang.data.hibernate;

import org.hibernate.annotations.IdGeneratorType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * tsid生成器
 *
 */
@SuppressWarnings("unused")
@IdGeneratorType(IdGenerate.class)
@Retention(RUNTIME)
@Target({ METHOD, FIELD })
public @interface TsIdGenerator {

}
