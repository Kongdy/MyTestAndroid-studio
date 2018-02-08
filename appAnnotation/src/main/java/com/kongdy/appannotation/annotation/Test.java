package com.kongdy.appannotation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author kongdy
 * @date 2018/2/6 16:54
 * @describe 注解测试
 **/

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface Test {
}
