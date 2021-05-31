package com.bb1.api.permissions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import com.bb1.api.Loader;

import me.lucko.fabric.api.permissions.v0.PermissionCheckEvent;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public final class PermissionManager {
	
	private static final PermissionManager INSTANCE = new PermissionManager();
	
	public static PermissionManager get() { return INSTANCE; }
	
	private PermissionManager() { registerEvent(); }
	
	private Map<UUID, Set<String>> permissionMap = new HashMap<UUID, Set<String>>();
	
	public Set<String> getPermissions(UUID player) { return permissionMap.getOrDefault(player, new HashSet<String>()); }
	
	public void givePermission(UUID player, String perm) {
		Set<String> set = permissionMap.getOrDefault(perm, new HashSet<String>());
		set.add(perm);
		permissionMap.put(player, set);
	}
	
	public void takePermission(UUID player, String perm) {
		Set<String> set = permissionMap.getOrDefault(perm, new HashSet<String>());
		set.remove(perm);
		permissionMap.put(player, set);
	}
	
	public void togglePermission(UUID player, String perm) {
		Set<String> set = permissionMap.getOrDefault(perm, new HashSet<String>());
		if (set.contains(perm)) set.remove(perm); else set.add(perm);
		permissionMap.put(player, set);
	}
	
	public boolean hasPermission(UUID player, String perm) {
		return permissionMap.getOrDefault(player, new HashSet<String>()).contains(perm);
	}
	
	public void registerEvent() {
		if (Loader.CONFIG.loadPermissionModule) { // Only register if allowed to (the check is here aswell in-case another mod attempts to load it)
			PermissionCheckEvent.EVENT.register(new PermissionCheckEvent() {
				
				@Override
				public @NotNull TriState onPermissionCheck(@NotNull CommandSource source, @NotNull String permission) {
					if (source instanceof ServerCommandSource) return TriState.DEFAULT;
					ServerPlayerEntity player = Loader.getServerPlayerEntity((ServerCommandSource)source);
					return (player==null) ? TriState.DEFAULT : (hasPermission(player.getUuid(), permission) ? TriState.TRUE : TriState.DEFAULT);
				}
				
			});
		}
	}
	
}
