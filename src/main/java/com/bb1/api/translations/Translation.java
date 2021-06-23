package com.bb1.api.translations;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Translation {
	
	public static TranslationBuilder builder(@NotNull String key) { return new TranslationBuilder(key); }
	
	private final String key;
	
	private final Map<String, String> translationMap;
	
	public Translation(@NotNull String key) { this(key, null); }
	
	public Translation(@NotNull String key, @Nullable Map<String, String> translationMap) {
		this.key = key;
		this.translationMap = (translationMap==null) ? new HashMap<String, String>() : translationMap;
	}

	public final String getKey() { return this.key; }
	
	public final String getTranslation(@NotNull String lang) { return this.translationMap.getOrDefault(lang, this.key); }
	
	public final Map<String, String> getMap() { return this.translationMap; }
	
	public static class TranslationBuilder {
		
		private final String key;
		
		private final Map<String, String> map = new HashMap<String, String>();

		private TranslationBuilder(@NotNull String key) {
			this.key = key;
		}
		
		public TranslationBuilder add(@NotNull String translation, @NotNull String lang, @Nullable String... langs) {
			this.map.put(lang, translation);
			if (langs!=null && langs.length>0) {
				for (String s : langs) {
					this.map.put(s, translation);
				}
			}
			return this;
		}
		
		public TranslationBuilder addEnglish(@NotNull String translation) { return add(translation, "en_us", "en_gb", "en_au", "en_ca", "en_nz"); }
		
		public Translation build() {
			return new Translation(this.key, map);
		}
		
	}

}
