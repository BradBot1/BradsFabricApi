package com.bb1.fabric.bfapi.config;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({ FIELD, ElementType.LOCAL_VARIABLE })
public @interface ConfigName{
	
	public String name();
	
}
