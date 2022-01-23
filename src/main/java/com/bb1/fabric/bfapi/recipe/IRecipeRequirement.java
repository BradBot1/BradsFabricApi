package com.bb1.fabric.bfapi.recipe;

import com.bb1.fabric.bfapi.utils.Field;
import com.google.gson.JsonObject;

import net.minecraft.entity.Entity;

/**
 * 
 * Copyright 2022 BradBot_1
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
 * A simple interface that deals with locking recipes to certain requirements
 * 
 * @author BradBot_1
 */
public interface IRecipeRequirement {
	
	public boolean canCraft(Field<Entity> crafter);
	
	public JsonObject addToObject(JsonObject object);
	
}
