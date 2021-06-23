package com.bb1.api.translations;

import java.util.Map.Entry;

import com.bb1.api.adapters.AbstractAdapter;

import fr.catcore.server.translations.api.resource.language.TranslationMap;

public class ServerTranslations extends AbstractAdapter implements TranslationProvider {

	private fr.catcore.server.translations.api.ServerTranslations translations = fr.catcore.server.translations.api.ServerTranslations.INSTANCE;
	
	public ServerTranslations() { super("server_translations"); }

	@Override
	protected void load() { TranslationManager.getInstance().registerProvider(this); }

	@Override
	public void registerTranslation(Translation translation) {
		for (Entry<String, String> entry : translation.getMap().entrySet()) {
			translations.addTranslations(entry.getKey(), ()->create(translation.getKey(), entry.getValue()));
		}
	}
	
	private TranslationMap create(String key, String value) {
		TranslationMap translationMap = new TranslationMap();
		translationMap.put(key, value);
		return translationMap;
	}

}
