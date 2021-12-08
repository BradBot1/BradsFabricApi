package com.bb1.fabric.bfapi.utils;

import java.lang.reflect.Method;

import org.jetbrains.annotations.NotNull;
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
 * A way of specifying an object without giving an object
 * <br><br>
 * For example you can do the following
 * <br><br>
 * <code>
 * Field<?> field = new Field("exampleString");
 * <br>
 * Integer i = field.get(); // Is null
 * <br>
 * String s = field.get(); // Is "exampleString"
 * </code>
 * <br><br>
 * Furthermore you can check types with
 * <br><br>
 * <code>
 * Field<?> field = new Field("exampleString");
 * <br>
 * if (field.isOfType(Integer.class)) {
 * <br>
 * 		// This will never run
 * <br>
 * }
 * <br>
 * if (field.isOfType(String.class)) {
 * <br>
 * 		// This will run
 * <br>
 * }
 * <br>
 * </code>
 * 
 * @param <T> The type of {@link #field}
 * 
 * @apiNote use over {@link Object}
 */
public final class Field<T> implements Cloneable {
	public static <T> Field<T> of(T object) { return new Field<T>(object); }
	/** The object that is referenced by this field */
	private final @NotNull T field;
	/** Constructs a new field with the given object */
	public Field(@NotNull T field) { this.field = field; }
	/** Attempts to cast {@link #field} to F, if it is unable to it simply returns null */
	public final @Nullable <F> F get() {
		try {
			@SuppressWarnings("unchecked")
			F f = (F) field;
			return f;
		} catch (Throwable t) {
			return null;
		}
	}
	/** @return {@link #field} */
	public final @NotNull T getObject() { return field; }
	/** @return The object as a jsonObject (string form) with field as the {@link #toString()} of {@link #field} and class as the class of {@link #field} and hash as {@link #hashCode()} */
	@Override
	public final @NotNull String toString() { return "{\"field\": \""+this.field.toString()+"\", \"class\": \""+this.field.getClass().getName()+"\", \"hash\": "+Integer.toString(hashCode())+"}"; }
	/** The hashcode of {@link #field} */
	@Override
	public final int hashCode() { return this.field.hashCode(); }
	/** Checks if the object given equals {@link #field} */
	@Override
	public final boolean equals(Object obj) { return this.field.equals(obj); }
	/** Attempts to clone the field, if {@link #field} is not cloneable then it simply returns this instance */
	@SuppressWarnings("unchecked")
	@Override
	public final @NotNull Field<T> clone() {
		try {
			final Method clone = this.field.getClass().getMethod("clone");
			final Object clonedField = clone.invoke(this.field);
			if ((clonedField.equals(this.field))) return new Field<T>((T)clonedField);
		} catch (Throwable t) { }
		return this;
	}
	/** Checks if {@link #field} is assignable to the given class */
	public final boolean isOfType(Class<?> clazz) { return this.field.getClass().isAssignableFrom(clazz); }
	
}
