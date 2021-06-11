package com.bb1.api.translations;

import java.lang.reflect.Field;

import com.bb1.api.Loader;

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
public final class DefaultTranslations {
	
	// CONFIG
	
	public static final TranslatableText CONFIG_NOT_FOUND = Loader.getTranslatableText("config.not_found");
	public static final TranslatableText CONFIG_PARSE_FAILED = Loader.getTranslatableText("config.parse_failed");
	public static final TranslatableText CONFIG_WRITE_FAILED = Loader.getTranslatableText("config.write_failed");
	public static final TranslatableText CONFIG_MODIFICATION_SUCCEEDED = Loader.getTranslatableText("config.modification_succeeded");
	/** When the config was modified but the write type and read type were not the same */
	public static final TranslatableText CONFIG_MODIFICATION_SUCCEEDED_BUT_TYPES_DIFFERED = Loader.getTranslatableText("config.modification_succeeded_but_types_different");
	
	// TRANSLATIONS
	
	public static final TranslatableText TRANSLATIONS_UPDATED = Loader.getTranslatableText("translation.updated");
	
	// PERMISSIONS
	
	public static final TranslatableText PERMISSION_HAS = Loader.getTranslatableText("permission.has");
	public static final TranslatableText PERMISSION_DOESNT_HAVE = Loader.getTranslatableText("permission.hasnt");
	public static final TranslatableText PERMISSION_TAKEN = Loader.getTranslatableText("permission.taken");
	public static final TranslatableText PERMISSION_GIVEN = Loader.getTranslatableText("permission.given");
	
	// GENERIC ERRORS
	
	public static final TranslatableText PLAYER_ONLY = Loader.getTranslatableText("error.player_only");
	public static final TranslatableText CONSOLE_ONLY_COMMAND = Loader.getTranslatableText("error.console_only_command");
	public static final TranslatableText PLAYER_ONLY_COMMAND = Loader.getTranslatableText("error.player_only_command");
	public static final TranslatableText NEED_PERMISSIONS = Loader.getTranslatableText("error.required_permission");
	/** When something is executed with improper arguments <i>(This includes if an argument is incorrect)</i>*/
	public static final TranslatableText NEED_ARGUMENTS = Loader.getTranslatableText("error.required_arguments");
	
	private DefaultTranslations() {} // So no instance can be made of this class
	
	public static final void register() {
		TranslationManager translationManager = TranslationManager.get();
		for (Field field : DefaultTranslations.class.getDeclaredFields()) {
			try {
				TranslatableText text = (TranslatableText) field.get(null);
				translationManager.setIfNotPresent(text.getKey(), TranslationManager.DEFAULT_LANG, text.getKey());
			} catch (Throwable e) {}
		}
	}
	
}
