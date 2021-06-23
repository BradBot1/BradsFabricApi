package com.bb1.api.translations;

import java.util.HashSet;
import java.util.Set;

import com.bb1.api.managers.AbstractManager;

public class TranslationManager extends AbstractManager<TranslationProvider> {
	
	private static final TranslationManager INSTANCE = new TranslationManager();
	
	public static TranslationManager getInstance() { return INSTANCE; }
	
	private TranslationManager() { }
	
	private Set<Translation> translations = new HashSet<Translation>();
	
	public void registerTranslation(Translation translation) {
		translations.add(translation);
		for (TranslationProvider provider : getProviders()) {
			provider.registerTranslation(translation);
		}
	}
	
	@Override
	public void registerProvider(TranslationProvider provider) {
		for (Translation translation : translations) {
			provider.registerTranslation(translation);
		}
		super.registerProvider(provider);
	}
	
}
