package com.bb1.fabric.bfapi.config;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({ FIELD, ElementType.LOCAL_VARIABLE })
public @interface ConfigComment{
	
	public String value() default "";
	/**
	 * use {@link #value()}
	 * 
	 * this will only be checked is value() is not set
	 */
	@Deprecated
	public String contents() default "No comment set";
	
	public String prefix() default "comment-";
	
}
