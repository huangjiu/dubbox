package com.alibaba.dubbo.javaconfig.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  auto register dubbox service
 * 
 * @author louis.huang
 * @export
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE })
public @interface EnableDubbo {
	
   String value() default "";

}
