package com.bb1.api.config;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.jetbrains.annotations.Nullable;

/**
 * A way to identify what fields should be saved in a {@link Config}
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface Storable {
	/**
	 * What name to save this value as inside the config
	 * 
	 * @apiNote If it returns the string "null" it will default to the field's name
	 */
	@Nullable String key() default "null";
	
}
