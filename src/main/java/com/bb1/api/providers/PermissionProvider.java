package com.bb1.api.providers;

import java.util.Set;

import net.minecraft.entity.player.PlayerEntity;

public interface PermissionProvider extends Provider {
	
	public boolean hasPermission(PlayerEntity playerEntity, String permission);
	
	public void givePermission(PlayerEntity playerEntity, String permission);
	
	public void takePermission(PlayerEntity playerEntity, String permission);
	/**
	 * This is just for tab complete, so the server knows what permissions are available
	 */
	public void registerPermission(String permission);
	
	public Set<String> getPermissions();
	
	public Set<String> getPermissions(PlayerEntity playerEntity);
	
}
