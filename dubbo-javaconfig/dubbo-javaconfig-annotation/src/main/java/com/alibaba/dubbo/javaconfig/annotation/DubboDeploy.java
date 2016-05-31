package com.alibaba.dubbo.javaconfig.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  auto register dubbox service of the build assembly
 * 
 * @author louis.huang
 * @export
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE })
public @interface DubboDeploy {
	
   String value() default "";

}
