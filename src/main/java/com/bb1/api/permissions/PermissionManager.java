package com.bb1.api.permissions;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import com.bb1.api.managers.AbstractInputtableManager;

public final class PermissionManager extends AbstractInputtableManager<PermissionProvider, Permission> {
	
	private static final PermissionManager INSTANCE = new PermissionManager();
	
	public static PermissionManager getInstance() { return INSTANCE; }
	
	private PermissionManager() { }
	
	public boolean hasPermission(@NotNull UUID uuid, @NotNull String permission) {
		for (PermissionProvider provider : getProviders()) {
			Boolean b = provider.hasPermission(uuid, permission);
			if (b==null) continue;
			return b;
		}
		return false;
	}
	
	@Override
	protected void onRegister(PermissionProvider provider) {
		for (Permission permission : getInput()) {
			provider.register(permission);
		}
	}
	
	@Override
	protected void onInput(Permission input) {
		for (PermissionProvider provider : getProviders()) {
			provider.register(input);
		}
	}
	
}
