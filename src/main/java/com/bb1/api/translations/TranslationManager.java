package com.bb1.api.translations;

import com.bb1.api.managers.AbstractInputtableManager;

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
