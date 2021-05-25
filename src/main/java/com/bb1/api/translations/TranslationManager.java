package com.bb1.api.translations;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fr.catcore.server.translations.api.ServerTranslations;
import fr.catcore.server.translations.api.resource.language.TranslationMap;

public class TranslationManager {
	/**
	 * The instance of {@link ServerTranslations} that we use to modify translations
	 */
	private static final ServerTranslations SERVER_TRANSLATIONS = ServerTranslations.INSTANCE;
	/**
	 * An instance of {@link TranslationManager}
	 */
	private static final TranslationManager INSTANCE = new TranslationManager();
	/**
	 * Returns the {@link TranslationManager} instance
	 */
	public static TranslationManager get() {
		return INSTANCE;
	}
	/**
	 * In the format regional -> translationKey -> value
	 */
	private final Map<String, TranslationMap> translations = new HashMap<>();
	
	public TranslationManager() {
		if (INSTANCE!=null && !INSTANCE.equals(this)) {
			throw new IllegalStateException("TranslationManager cannot be instanced twice");
		}
	}
	/**
	 * Converts the translation list to json
	 */
	public JsonObject convertLangToJson() {
		JsonObject regionals = new JsonObject();
		for (Entry<String, TranslationMap> entry : translations.entrySet()) {
			JsonObject translations = new JsonObject();
			for (Entry<String, String> entry2 : entry.getValue().entrySet()) {
				translations.addProperty(entry2.getKey(), entry2.getValue());
			}
			regionals.add(entry.getKey(), translations);
		}
		return regionals;
	}
	/**
	 * 
	 * @param jsonObject
	 */
	public void addFromJson(@NotNull JsonObject jsonObject) {
		for (String regional : jsonObject.keySet()) {
			TranslationMap translationMap = new TranslationMap();
			JsonElement jsonElement = jsonObject.get(regional);
			if (!jsonElement.isJsonObject()) continue;
			JsonObject jsonObject2 = jsonElement.getAsJsonObject();
			for (String key : jsonObject2.keySet()) {
				JsonElement jsonElement2 = jsonObject2.get(key);
				if (!jsonElement2.isJsonPrimitive() || !jsonElement2.getAsJsonPrimitive().isString()) continue;
				translationMap.put(key, jsonElement2.getAsString());
			}
			translations.put(regional, translationMap);
		}
	}
	
	public void pushAllTranslations(boolean force) {
		for (Entry<String, TranslationMap> entry : translations.entrySet()) {
			SERVER_TRANSLATIONS.addTranslations(entry.getKey(), new Supplier<TranslationMap>() { @Override public TranslationMap get() { return entry.getValue(); } });
		}
	}
	
}
