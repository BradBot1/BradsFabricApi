package com.bb1.api.translations;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import com.bb1.api.Loader;
import com.bb1.api.events.Events;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
		Events.LOAD_EVENT.register((event)->load());
		Events.UNLOAD_EVENT.register((event)->save());
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
	
	public void setIfNotPresent(String translation_key, String lang, String value) {
		TranslationMap translationMap = translations.getOrDefault(lang, new TranslationMap());
		if (!translationMap.contains(translation_key)) translationMap.put(translation_key, value);
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
		load();
		pushAllTranslations(true);
		save();
	}
	
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final JsonParser PARSER = new JsonParser();
	private static final String TRANSLATION_FILE = Loader.getRootPath()+File.separator+"config"+File.separator+"translations.json";
	
	protected void load() {
		try {
			File file = new File(TRANSLATION_FILE);
			if (!file.exists()) return; // Can't load nothing
			ArrayList<String> r = new ArrayList<String>();
			Scanner s = new Scanner(file);
			while (s.hasNext()) {
		    	r.add(s.nextLine());
			}
			s.close();
			JsonElement contents = PARSER.parse(String.join("", r));
			addFromJson(contents.getAsJsonObject());
			pushAllTranslations(true);
		} catch (IOException e) {
			// TODO: log that we failed to load
		}
	}
	
	protected void save() {
		try {
			File file = new File(TRANSLATION_FILE);
			if (!file.exists()) file.createNewFile();
			BufferedWriter b = new BufferedWriter(new PrintWriter(file));
			b.write(GSON.toJson(convertLangToJson()));
			b.flush();
			b.close();
			pushAllTranslations(true);
		} catch (Throwable e) {
			// TODO: log that we failed to save
		}
	}
	
}
