package com.bb1.api.translations;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bb1.api.providers.Provider;

public interface TranslationProvider extends Provider {
	
	public void registerTranslation(Translation translation);
	
	@Override
	public default Logger getProviderLogger() { return LogManager.getLogger("TranslationManager | "+getProviderName()); }
	
}
