package com.bb1.api.gamerules;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.GameRules.Category;

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
public abstract class GameRule<T> {
	
	protected final String name;
	
	protected final Category category;
	
	protected T value;
	
	protected GameRule(@NotNull String name, @NotNull T defaultValue, Category category) { this.name = name; this.value = defaultValue; this.category = category; }
	
	public final String getName() { return this.name; }
	
	public final void setValue(T value) { this.value = value; }
	
	public final T getValue() { return this.value; }
	
	public final Category getCategory() { return this.category; }
	
	public abstract String serialize();
	
	public abstract void deserialize(String serializedData);
	
	public int toInt() { return Objects.hashCode(value); }
	
	public abstract T parse(String string);

}