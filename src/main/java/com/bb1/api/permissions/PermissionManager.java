package com.bb1.api.permissions;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import com.bb1.api.managers.AbstractInputtableManager;

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
