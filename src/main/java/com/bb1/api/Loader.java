package com.bb1.api;

import com.bb1.api.translations.TranslationManager;

import fr.catcore.server.translations.api.resource.language.TranslationsReloadListener;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.minecraft.text.TranslatableText;
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
public class Loader implements DedicatedServerModInitializer, TranslationsReloadListener {
	/**
	 * 
	 * Formats a {@link TranslatableText} in the format "bradsfabricapi." + <i>key</i>;
	 * 
	 * @param key The key for the text
	 */
	public static TranslatableText getTranslatableText(String key) {
		return new TranslatableText("bradsfabricapi."+key);
	}

	@Override
	public void onInitializeServer() { }
	/**
	 * Called when translations should be reloaded
	 */
	@Override
	public void reload() {
		TranslationManager.get().pushAllTranslations(true); // TODO: idfk
	}

}
