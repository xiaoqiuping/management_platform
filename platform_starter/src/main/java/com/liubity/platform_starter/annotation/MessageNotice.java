package com.liubity.platform_starter.annotation;

import java.lang.annotation.*;

/**
 * @Author Liubity
 * @Date 2020-11-27
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MessageNotice {
    String value() default "";
}
