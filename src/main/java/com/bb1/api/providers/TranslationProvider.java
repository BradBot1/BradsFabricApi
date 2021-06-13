package com.bb1.api.providers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract interface TranslationProvider extends Provider {
	
	public void registerTranslation(@NotNull String translation_key, @Nullable String defaultValue);
	
	public void setTranslation(@NotNull String translation_key, @NotNull String lang, @NotNull String value);
	
	public String getTranslation(@NotNull String translation_key, @NotNull String lang);
	
	public void pushTranslations();
	
	@Override
	public default Logger getProviderLogger() { return LogManager.getLogger("TranslationProvider | "+getProviderName()); }
	
}
