package com.bb1.api.translations;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import com.bb1.api.Loader;
import com.bb1.api.events.Events;
import com.bb1.api.events.Events.ProviderInformationEvent;
import com.bb1.api.providers.TranslationProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fr.catcore.server.translations.api.ServerTranslations;
import fr.catcore.server.translations.api.resource.language.ServerLanguageDefinition;
import fr.catcore.server.translations.api.resource.language.TranslationMap;

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
public final class TranslationManager implements TranslationProvider {
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
	public static final String DEFAULT_LANG = (SERVER_TRANSLATIONS.getDefaultLanguage().definition().code()==null) ? "unkown" : SERVER_TRANSLATIONS.getDefaultLanguage().definition().code();
	/**
	 * In the format regional -> translationKey -> value
	 */
	private final Map<String, TranslationMap> translations = new HashMap<>();
	
	private boolean debug = Loader.CONFIG.debugMode;
	
	private TranslationManager() {
		if (!Loader.CONFIG.loadTranslationProvider) return;
		Events.LOAD_EVENT.register((event)->Events.PROVIDER_INFO_EVENT.onEvent(new ProviderInformationEvent(this)));
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
		if (debug) getProviderLogger().info("Set the translation "+translation_key+" to "+value+" in "+lang);
	}
	
	public void setIfNotPresent(String translation_key, String lang, String value) {
		TranslationMap translationMap = translations.getOrDefault(lang, new TranslationMap());
		if (!translationMap.contains(translation_key)) translationMap.put(translation_key, value);
		translations.put(lang, translationMap);
		if (debug) getProviderLogger().info("Regsitered the translation "+translation_key);
	}
	
	public Set<String> getLangs() {
		Set<String> set = new HashSet<String>();
		for (ServerLanguageDefinition s : SERVER_TRANSLATIONS.getAllLanguages()) {
			set.add(s.code());
		}
		set.add(DEFAULT_LANG);
		return set;
	}
	
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final JsonParser PARSER = new JsonParser();
	private static final String TRANSLATION_FILE = Loader.getRootPath()+File.separator+"config"+File.separator+"translations.json";
	
	// TranslationProvider stuff
	
	@Override
	public void load() {
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
			if (debug) getProviderLogger().info("Loaded translations from file");
		} catch (IOException e) {
			if (debug) getProviderLogger().info("Failed to load transtlations");
		}
	}
	
	@Override
	public void save() {
		try {
			File file = new File(TRANSLATION_FILE);
			if (!file.exists()) file.createNewFile();
			BufferedWriter b = new BufferedWriter(new PrintWriter(file));
			b.write(GSON.toJson(convertLangToJson()));
			b.flush();
			b.close();
			if (debug) getProviderLogger().info("Saved translations to file");
		} catch (Throwable e) {
			if (debug) getProviderLogger().info("Failed to save transtlations");
		}
	}
	
	@Override
	public String getProviderName() { return "TranslationManager"; }
	
	@Override
	public void registerTranslation(String translation_key, String defaultValue) { setIfNotPresent(translation_key, DEFAULT_LANG, (defaultValue==null) ? translation_key : defaultValue); }
	
	@Override
	public void setTranslation(String translation_key, String lang, String value) { setTranslation(translation_key, lang, value); }
	
	@Override
	public String getTranslation(String translation_key, String lang) { return translate(translation_key, lang); }
	
	@Override
	public void pushTranslations() {
		for (String s : getLangs()) {
			SERVER_TRANSLATIONS.addTranslations(s, new Supplier<TranslationMap>() { @Override public TranslationMap get() { return translations.getOrDefault(s, new TranslationMap()); }});
		}
		if (debug) getProviderLogger().info("Pushed all translations");
	}
	
}
