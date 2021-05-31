package com.bb1.api.translations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fr.catcore.server.translations.api.ServerTranslations;
import fr.catcore.server.translations.api.resource.language.ServerLanguageDefinition;
import fr.catcore.server.translations.api.resource.language.TranslationMap;
import fr.catcore.server.translations.api.resource.language.TranslationsReloadListener;

public final class TranslationManager implements TranslationsReloadListener {
	/**
	 * The instance of {@link ServerTranslations} that we use to modify translations
	 */
	public static final ServerTranslations SERVER_TRANSLATIONS = ServerTranslations.INSTANCE;
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
	 * The language to register translations to if no translations are known
	 */
	public static final String DEFAULT_LANG = (SERVER_TRANSLATIONS.getDefaultLanguage().definition.getCode()==null) ? "unkown" : SERVER_TRANSLATIONS.getDefaultLanguage().definition.getCode();
	/**
	 * In the format regional -> translationKey -> value
	 */
	private final Map<String, TranslationMap> translations = new HashMap<>();
	
	private TranslationManager() {
		SERVER_TRANSLATIONS.registerReloadListener(this);
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
		for (Entry<String, JsonElement> regional : jsonObject.entrySet()) {
			TranslationMap translationMap = new TranslationMap();
			JsonElement jsonElement = regional.getValue();
			if (!jsonElement.isJsonObject()) continue;
			JsonObject jsonObject2 = jsonElement.getAsJsonObject();
			for (Entry<String, JsonElement> key : jsonObject2.entrySet()) {
				JsonElement jsonElement2 = key.getValue();
				if (!jsonElement2.isJsonPrimitive() || !jsonElement2.getAsJsonPrimitive().isString()) continue;
				translationMap.put(key.getKey(), jsonElement2.getAsString());
			}
			translations.put(regional.getKey(), translationMap);
		}
	}
	
	public String translate(String translation_key, String lang) {
		return translations.getOrDefault(lang, new TranslationMap()).get(translation_key);
	}
	
	public void set(String translation_key, String lang, String value) {
		TranslationMap translationMap = translations.getOrDefault(lang, new TranslationMap());
		translationMap.put(translation_key, value);
		translations.put(lang, translationMap);
	}
	
	public Set<String> getLangs() {
		Set<String> set = new HashSet<String>();
		for (ServerLanguageDefinition s : SERVER_TRANSLATIONS.getAllLanguages()) {
			set.add(s.getCode());
		}
		set.add(DEFAULT_LANG);
		return set;
	}
	
	public void pushAllTranslations(boolean force) {
		for (String s : getLangs()) {
			SERVER_TRANSLATIONS.addTranslations(s, new Supplier<TranslationMap>() { @Override public TranslationMap get() { return translations.getOrDefault(s, new TranslationMap()); }});
		}
	}
	/**
	 * Called when translations should be reloaded
	 */
	@Override
	public void reload() {
		pushAllTranslations(true);
	}
	
	protected void load() {
		
	}
	
	protected void save() {
		
	}
	
}
