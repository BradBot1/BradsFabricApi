package com.bb1.api.permissions;

import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import com.bb1.api.adapters.AbstractAdapter;
/**
 * The repo is not public atm, when it is i will add support :)
 */
public class PatboxPermissionsAPI extends AbstractAdapter implements PermissionProvider {

	public PatboxPermissionsAPI() {
		super("permissions-api");
	}
	
	@Override
	public void load() {
		PermissionManager.getInstance().registerProvider(this);
	}

	@Override
	public void register(Permission permission) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void givePermission(@NotNull UUID uuid, @NotNull String permission) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void takePermission(@NotNull UUID uuid, @NotNull String permission) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean hasPermission(@NotNull UUID uuid, @NotNull String permission) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Boolean> getPermissions(@NotNull UUID uuid) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
