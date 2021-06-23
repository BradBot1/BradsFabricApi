package com.bb1.api.config;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.jetbrains.annotations.Nullable;

/**
 * Copyright 2021 BradBot_1
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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