package com.bb1.api.datapacks;

import java.util.Collection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public abstract class JsonDatapackAddon extends DatapackAddon {

	protected static final JsonParser PARSER = new JsonParser();

	public JsonDatapackAddon(@NotNull Identifier identifier, @NotNull String path) {
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
