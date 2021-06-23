package com.bb1.api.datapacks;

import java.util.Collection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

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
public abstract class JsonDatapackHandler extends DatapackHandler {

	protected static final JsonParser PARSER = new JsonParser();

	public JsonDatapackHandler(@NotNull Identifier identifier, @NotNull String path) {
		super(identifier, path, ".json");
	}

	@Override
	public void reload(@NotNull ResourceManager resourceManager, @Nullable Collection<Identifier> identifiers) {
		unloadAll();
		for (Identifier identifier : identifiers) {
			try {
				load(PARSER.parse(String.join("", read(resourceManager, identifier))), identifier);
			} catch (Throwable t) {
				
			}
		}
	}

	public abstract void load(@NotNull JsonElement json, @Nullable Identifier resourceIdentifier) throws Throwable;
	/** When called all data should be unloaded and cleared */
	public abstract void unloadAll();

}