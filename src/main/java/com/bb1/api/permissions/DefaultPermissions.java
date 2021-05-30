package com.bb1.api.permissions;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public final class DefaultPermissions {
	
	public static final String PERMISSIONS_VIEW = "bradsfabricapi.permissions.get";
	public static final String PERMISSIONS_MODIFY = "bradsfabricapi.permissions.set";
	
	public static final String TRANSLATION_MODIFY = "bradsfabricapi.translations.set";
	public static final String TRANSLATION_VIEW = "bradsfabricapi.translations.get";
	/**
	 * A set of all permissions in this class
	 */
	public static final Set<String> PERMISSIONS = new HashSet<String>();
	
	private DefaultPermissions() {}
	
	public static final void load() {
		for (Field field : DefaultPermissions.class.getDeclaredFields()) {
			try {
				PERMISSIONS.add((String) field.get(null));
			} catch (IllegalArgumentException | IllegalAccessException e) {}
		}
	}
	
}
