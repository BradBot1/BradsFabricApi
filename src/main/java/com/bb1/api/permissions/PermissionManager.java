package com.bb1.api.permissions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import com.bb1.api.providers.PermissionProvider;

import net.minecraft.entity.player.PlayerEntity;

public final class PermissionManager implements PermissionProvider {
	
	private static final PermissionManager INSTANCE = new PermissionManager();
	
	public static PermissionManager get() { return INSTANCE; }
	
	private PermissionManager() { }
	
	private final Map<UUID, Set<String>> permissionMap = new HashMap<UUID, Set<String>>();
	
	private final Set<String> permissions = new HashSet<String>();
	
	public Set<String> getPermissions(PlayerEntity player) { return permissionMap.getOrDefault(player.getUuid(), new HashSet<String>()); }
	
	@Override
	public void registerPermission(String permission) { this.permissions.add(permission); }
	
	@Override
	public Set<String> getPermissions() { return this.permissions; }
	
	public void givePermission(PlayerEntity player, String perm) {
		Set<String> set = permissionMap.getOrDefault(perm, new HashSet<String>());
		set.add(perm);
		permissionMap.put(player.getUuid(), set);
	}
	
	public void takePermission(PlayerEntity player, String perm) {
		Set<String> set = permissionMap.getOrDefault(perm, new HashSet<String>());
		set.remove(perm);
		permissionMap.put(player.getUuid(), set);
	}
	
	public boolean hasPermission(PlayerEntity player, String perm) {
		return permissionMap.getOrDefault(player.getUuid(), new HashSet<String>()).contains(perm);
	}
	
	@Override
	public @NotNull String getProviderName() {
		return "PermissionManager";
	}
	
}
