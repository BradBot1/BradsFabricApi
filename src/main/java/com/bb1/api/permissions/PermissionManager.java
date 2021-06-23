package com.bb1.api.permissions;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import com.bb1.api.managers.AbstractManager;

public final class PermissionManager extends AbstractManager<PermissionProvider> {
	
	private static final PermissionManager INSTANCE = new PermissionManager();
	
	public static PermissionManager getInstance() { return INSTANCE; }
	
	private PermissionManager() { }
	
	private Set<Permission> permissions = new HashSet<Permission>();
	
	public boolean hasPermission(@NotNull UUID uuid, @NotNull String permission) {
		for (PermissionProvider provider : getProviders()) {
			Boolean b = provider.hasPermission(uuid, permission);
			if (b==null) continue;
			return b;
		}
		return false;
	}
	
	public void registerPermission(@NotNull Permission permission) {
		permissions.add(permission);
		for (PermissionProvider provider : getProviders()) {
			provider.register(permission);
		}
	}
	
	@Override
	public void registerProvider(PermissionProvider provider) {
		for (Permission permission : permissions) {
			provider.register(permission);
		}
		super.registerProvider(provider);
	}
	
}
