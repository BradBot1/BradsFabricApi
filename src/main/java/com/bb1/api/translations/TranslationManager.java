package com.bb1.api.translations;

import com.bb1.api.managers.AbstractInputtableManager;

public class TranslationManager extends AbstractInputtableManager<TranslationProvider, Translation> {
	
	private static final TranslationManager INSTANCE = new TranslationManager();
	
	public static TranslationManager getInstance() { return INSTANCE; }
	
	private TranslationManager() { }
	
	@Override
	protected void onRegister(TranslationProvider provider) {
		for (Translation translation : getInput()) {
			provider.registerTranslation(translation);
		}
	}
	
	@Override
	protected void onInput(Translation input) {
		for (TranslationProvider provider : getProviders()) {
			provider.registerTranslation(input);
		}
	}
	
}
