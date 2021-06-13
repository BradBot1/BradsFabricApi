package com.bb1.api.permissions;

import com.bb1.api.commands.permissions.Permission;

public final class DefaultPermissions {
	
	public static final Permission CONFIG_VIEW = new Permission("bradsfabricapi.config.get", 2);
	public static final Permission CONFIG_MODIFY = new Permission("bradsfabricapi.config.set", 4);
	
	public static final Permission TRANSLATION_MODIFY = new Permission("bradsfabricapi.translations.set", 4);
	public static final Permission TRANSLATION_VIEW = new Permission("bradsfabricapi.translations.get", 2);
	
	private DefaultPermissions() { }
	
}
