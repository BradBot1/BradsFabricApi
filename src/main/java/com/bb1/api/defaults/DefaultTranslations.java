package com.bb1.api.defaults;

import com.bb1.api.translations.Translation;

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
	
	public static final Translation GENERIC_ERROR = Translation.builder("bfapi.generic_error").addEnglish("An error occured").build();
	public static final Translation PLAYER_ONLY = Translation.builder("bfapi.player_only").addEnglish("This can only be preformed by players").build();
	public static final Translation OP_ONLY = Translation.builder("bfapi.op_only").addEnglish("This can only be preformed by operators").build();
	public static final Translation CONSOLE_ONLY = Translation.builder("bfapi.console_only").addEnglish("This can only be preformed by console").build();
	public static final Translation NEED_PERMISSIONS = Translation.builder("bfapi.permission_required").addEnglish("You are missing the required permission to do this").build();
	public static final Translation INVALID_INPUT = Translation.builder("bfapi.input_invalid").addEnglish("The input provided is invaild or unusable").build();
	
}