package com.bb1.api.gamerules;

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
public class IntegerGameRule extends GameRule<Integer> {

	public IntegerGameRule(@NotNull String name, int value, Category category) { super(name, value, category); }

	@Override
	public String serialize() { return toString(); }

	@Override
	public void deserialize(String serializedData) { setValue(parse(serializedData)); }

	@Override
	public Integer parse(String string) { return Integer.parseInt(string); }
	
	@Override
	public String toString() { return Integer.toString(value); }
	
	@Override
	public int toInt() { return getValue(); }

}
