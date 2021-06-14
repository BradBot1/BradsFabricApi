package com.bb1.api.translations;

import java.util.HashMap;

/**
 * Copyright 2021 BradBot_1
 * 
 * Licensed under the Apache License, Version 2.0 (the "License", new HashMap<String, String>());
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
	
	public static final Translation CONFIG_NOT_FOUND = new Translation("bfapi.config.not_found", new HashMap<String, String>());
	public static final Translation CONFIG_PARSE_FAILED = new Translation("bfapi.config.parse_failed", new HashMap<String, String>());
	public static final Translation CONFIG_WRITE_FAILED = new Translation("bfapi.config.write_failed", new HashMap<String, String>());
	public static final Translation CONFIG_MODIFICATION_SUCCEEDED = new Translation("bfapi.config.modification_succeeded", new HashMap<String, String>());
	/** When the config was modified but the write type and read type were not the same */
	public static final Translation CONFIG_MODIFICATION_SUCCEEDED_BUT_TYPES_DIFFERED = new Translation("bfapi.config.modification_succeeded_but_types_different", new HashMap<String, String>());
	
	// TRANSLATIONS
	
	public static final Translation TRANSLATIONS_UPDATED = new Translation("bfapi.translation.updated", new HashMap<String, String>());
	public static final Translation TRANSLATIONS_UPDATED_FAIL = new Translation("bfapi.translation.updated_failed", new HashMap<String, String>());
	
	// GENERIC ERRORS
	
	public static final Translation PLAYER_ONLY = new Translation("bfapi.error.player_only", new HashMap<String, String>());
	public static final Translation CONSOLE_ONLY_COMMAND = new Translation("bfapi.error.console_only_command", new HashMap<String, String>());
	public static final Translation PLAYER_ONLY_COMMAND = new Translation("bfapi.error.player_only_command", new HashMap<String, String>());
	public static final Translation NEED_PERMISSIONS = new Translation("bfapi.error.required_permission", new HashMap<String, String>());
	/** When something is executed with improper arguments <i>(This includes if an argument is incorrect)</i>*/
	public static final Translation NEED_ARGUMENTS = new Translation("bfapi.error.required_arguments", new HashMap<String, String>());
	/** When a provider is needed but not found/doesnt exist */
	public static final Translation PROVIDER_NOT_FOUND = new Translation("bfapi.error.required_provider", new HashMap<String, String>());
	
	private DefaultTranslations() { } // So no instance can be made of this class
	
}
