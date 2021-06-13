package com.bb1.api.providers;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bb1.api.commands.permissions.Permission;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;

public interface PermissionProvider extends Provider {
	
	public boolean hasPermission(PlayerEntity playerEntity, String permission);
	
	public void givePermission(PlayerEntity playerEntity, String permission);
	
	public void takePermission(PlayerEntity playerEntity, String permission);
	/**
	 * This is just for tab complete, so the server knows what permissions are available
	 */
	public void registerPermission(Permission permission);
	
	public Set<Permission> getPermissions();
	
	public Set<Permission> getPermissions(PlayerEntity playerEntity);
	/**
	 * Registers permissions for all commands
	 */
	public void registerPermissions(CommandDispatcher<ServerCommandSource> dispatcher);
	
	@Override
	public default Logger getProviderLogger() { return LogManager.getLogger("PermissionProvider | "+getProviderName()); }
	
}
