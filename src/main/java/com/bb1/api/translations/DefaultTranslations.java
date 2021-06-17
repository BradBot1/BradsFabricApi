package com.bb1.api.translations;

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
public final class DefaultTranslations {
	
	// CONFIG
	
	public static final Translation CONFIG_NOT_FOUND = Translation.createGenericEnglishTranslation("bfapi.config.not_found", "Config not found");
	public static final Translation CONFIG_PARSE_FAILED = Translation.createGenericEnglishTranslation("bfapi.config.parse_failed", "Failed to parse config");
	public static final Translation CONFIG_WRITE_FAILED = Translation.createGenericEnglishTranslation("bfapi.config.write_failed", "Failed to write to config");
	public static final Translation CONFIG_MODIFICATION_SUCCEEDED = Translation.createGenericEnglishTranslation("bfapi.config.modification_succeeded", "Modified config");
	/** When the config was modified but the write type and read type were not the same */
	public static final Translation CONFIG_MODIFICATION_SUCCEEDED_BUT_TYPES_DIFFERED = Translation.createGenericEnglishTranslation("bfapi.config.modification_succeeded_but_types_different", "Modified config");
	
	// TRANSLATIONS
	
	public static final Translation TRANSLATIONS_UPDATED = Translation.createGenericEnglishTranslation("bfapi.translation.updated", "Updated translations");
	public static final Translation TRANSLATIONS_UPDATED_FAIL = Translation.createGenericEnglishTranslation("bfapi.translation.updated_failed", "Failed to update translations");
	
	// GENERIC ERRORS
	
	public static final Translation PLAYER_ONLY = Translation.createGenericEnglishTranslation("bfapi.error.player_only", "Player only");
	public static final Translation CONSOLE_ONLY_COMMAND = Translation.createGenericEnglishTranslation("bfapi.error.console_only_command", "This command is console only");
	public static final Translation PLAYER_ONLY_COMMAND = Translation.createGenericEnglishTranslation("bfapi.error.player_only_command", "This command is player only");
	public static final Translation NEED_PERMISSIONS = Translation.createGenericEnglishTranslation("bfapi.error.required_permission", "A permission is needed to do this");
	/** When something is executed with improper arguments <i>(This includes if an argument is incorrect)</i>*/
	public static final Translation NEED_ARGUMENTS = Translation.createGenericEnglishTranslation("bfapi.error.required_arguments", "More arguments are needed to preform this");
	/** When a provider is needed but not found/doesn't exist */
	public static final Translation PROVIDER_NOT_FOUND = Translation.createGenericEnglishTranslation("bfapi.error.required_provider", "Failed due to a lack of a provider");
	
	private DefaultTranslations() { } // So no instance can be made of this class
	
}
