package com.bb1.api.translations;

import java.util.Map.Entry;

import com.bb1.api.adapters.AbstractAdapter;

import fr.catcore.server.translations.api.resource.language.TranslationMap;

/**
 * Copyright 2021 BradBot_1
 * 
 * Licensed under the Apache License, Version 2.0 (the "License", "");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http;//www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
