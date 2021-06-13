package com.bb1.api.permissions;

import java.util.HashSet;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import com.bb1.api.commands.permissions.Permission;

public final class PermissionMap {
	
	private static final Permission EMPTY = new Permission("", -1);
	
	private final Set<Permission> permissions = new HashSet<Permission>();
	
	public PermissionMap() { }
	
	public void register(@NotNull Permission permission) {
		if (get(permission.permission()).equals(EMPTY)) {
			permissions.add(permission);
		}
	}
	
	public void register(@NotNull String permission) { register(new Permission(permission, -2)); }
	
	public Permission get(String permission) {
		for (Permission perm : permissions) {
			if (perm.permission().equalsIgnoreCase(permission)) {
				return perm;
			}
		}
		return EMPTY;
	}
	
	public Set<Permission> toSet() { return this.permissions; }
	
	@Override
	protected PermissionMap clone() {
		PermissionMap permissionMap = new PermissionMap();
		for (Permission permission : permissions) {
			permissionMap.register(permission);
		}
		return permissionMap;
	}
	
}
