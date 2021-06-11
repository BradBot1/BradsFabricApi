package com.bb1.api;

import com.bb1.api.config.Config;
import com.bb1.api.config.Storable;

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
/**
 * The config for the main api
 */
public class ApiConfig extends Config {

	public ApiConfig() {
		super("bradsfabricapi");
	}
	
	// Toggling modules
	
	/**
	 * If the server has a permission manager then the default one can be disabled
	 */
	@Storable public boolean loadPermissionModule = true;
	
	// Toggling commands
	
	/**
	 * I am not sure why you would want to disable this, but if they want to they can
	 */
	@Storable public boolean loadTranslationCommand = true;
	/**
	 * If {@link #loadPermissionModule} is off this auto disables
	 */
	@Storable public boolean loadPermissionCommand = loadPermissionModule;
	/**
	 * If you do not wish for configs to be modifyable via a command you can disable this
	 */
	@Storable public boolean loadConfigCommand = true;
}
