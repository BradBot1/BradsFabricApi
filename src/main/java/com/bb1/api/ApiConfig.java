package com.bb1.api;

import com.bb1.api.config.Config;
import com.bb1.api.config.Storable;

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
