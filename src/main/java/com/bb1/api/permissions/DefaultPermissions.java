package com.bb1.api.permissions;

import com.bb1.api.commands.permissions.Permission;

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
public final class DefaultPermissions {
	
	public static final Permission CONFIG_VIEW = new Permission("bfapi.config.get", 2);
	public static final Permission CONFIG_MODIFY = new Permission("bfapi.config.set", 4);
	
	public static final Permission TRANSLATION_MODIFY = new Permission("bfapi.translations.set", 4);
	public static final Permission TRANSLATION_VIEW = new Permission("bfapi.translations.get", 2);
	
	private DefaultPermissions() { }
	
}
