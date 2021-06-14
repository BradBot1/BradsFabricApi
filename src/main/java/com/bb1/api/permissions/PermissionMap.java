package com.bb1.api.permissions;

import java.util.HashSet;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

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
