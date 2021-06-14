package com.bb1.api.permissions;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import com.bb1.api.Loader;
import com.bb1.api.commands.Command;
import com.bb1.api.commands.SubCommand;
import com.bb1.api.commands.permissions.Permission;
import com.bb1.api.commands.tab.ITabable;
import com.bb1.api.commands.tab.TabableSubCommand;
import com.bb1.api.events.Events;
import com.bb1.api.events.Events.ProviderInformationEvent;
import com.bb1.api.providers.CommandProvider;
import com.bb1.api.providers.PermissionProvider;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;

import me.lucko.fabric.api.permissions.v0.PermissionCheckEvent;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.fabric.api.util.TriState;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

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
public final class PermissionManager implements PermissionProvider {
	
	private static final PermissionManager INSTANCE = new PermissionManager();
	
	public static PermissionManager get() { return INSTANCE; }
	
	private final FabricLoader loader = FabricLoader.getInstance();
	
	private boolean debug = Loader.CONFIG.debugMode;
	
	private PermissionManager() {
		if (!Loader.CONFIG.loadPermissionProvider) return;
		Events.RELOAD_EVENT.register(event->overrideCommands());
		Events.LOAD_EVENT.register((event)->Events.PROVIDER_INFO_EVENT.onEvent(new ProviderInformationEvent(this)));
		if (loader.isModLoaded("fabric-permissions-api-v0")) {
			PermissionCheckEvent.EVENT.register(new PermissionCheckEvent() {
				
				@Override
				public @NotNull TriState onPermissionCheck(@NotNull CommandSource source, @NotNull String permission) {
					try {
						return checkForPerm(((ServerCommandSource)source).getPlayer(), permission) ? TriState.TRUE : TriState.DEFAULT;
					} catch (Throwable e) {
						return TriState.DEFAULT;
					}
				}
				
			});
		}
		Events.PLAYER_NBT_READ_EVENT.register((event)->{
			NbtElement permissionElement = event.getNBT(getProviderName());
			if (permissionElement==null || !(permissionElement instanceof NbtCompound)) {
				if (debug) getProviderLogger().error("Failed to load the player "+event.getPlayerEntity().getName().asTruncatedString(32)+"'s permissions");
				return;
			}
			NbtCompound permissionTag = (NbtCompound) permissionElement;
			for (String permissionName : permissionTag.getKeys()) {
				givePermission(event.getPlayerEntity(), permissionName);
			}
		});
		Events.PLAYER_NBT_WRITE_EVENT.register((event)->{
			NbtCompound permissionTag = new NbtCompound();
			for (Permission perm : permissionMap.getOrDefault(event.getPlayerEntity().getUuid(), new HashSet<Permission>())) {
				permissionTag.putInt(perm.permission(), perm.opLevel());
			}
			event.addNBT(getProviderName(), permissionTag);
		});
	}
	
	private final Map<UUID, Set<Permission>> permissionMap = new HashMap<UUID, Set<Permission>>();
	
	private final PermissionMap permissions = new PermissionMap();
	
	public Set<Permission> getPermissions(PlayerEntity player) { return permissionMap.getOrDefault(player.getUuid(), new HashSet<Permission>()); }
	
	@Override
	public void registerPermission(Permission permission) {
		permissions.register(permission);
		if (debug) getProviderLogger().info("Registered the permission "+permission.permission()+" with the level "+permission.opLevel());
	}
	
	@Override
	public Set<Permission> getPermissions() { return this.permissions.toSet(); }
	
	public void givePermission(PlayerEntity player, String perm) {
		Set<Permission> set = permissionMap.getOrDefault(perm, new HashSet<Permission>());
		set.add(permissions.get(perm));
		permissionMap.put(player.getUuid(), set);
		if (debug) getProviderLogger().info("Gave the player "+player.getName().asTruncatedString(32)+" the permission "+perm);
	}
	
	public void takePermission(PlayerEntity player, String perm) {
		Set<Permission> set = permissionMap.getOrDefault(perm, new HashSet<Permission>());
		set.remove(permissions.get(perm));
		permissionMap.put(player.getUuid(), set);
		if (debug) getProviderLogger().info("Took the permission "+perm+" from the player "+player.getName().asTruncatedString(32));
	}
	
	public boolean hasPermission(PlayerEntity player, String perm) {
		if (loader.isModLoaded("fabric-permissions-api-v0")) return Permissions.check(player, perm);
		return checkForPerm(player, perm);
	}
	
	private boolean checkForPerm(PlayerEntity player, String perm) {
		Permission permission = permissions.get(perm);
		if (permission.opLevel()==-1) return false;
		if (permission.opLevel()==0) return true;
		if (permission.opLevel()>0 && player.hasPermissionLevel(permission.opLevel())) return true;
		return permissionMap.getOrDefault(player.getUuid(), new HashSet<Permission>()).contains(permissions.get(perm));
	}
	
	@Override
	public void load() {
		overrideCommands();
	}
	
	@Override
	public @NotNull String getProviderName() {
		return "PermissionManager";
	}
	
	@Override
	public void registerPermissions(CommandDispatcher<ServerCommandSource> d) {
		if (d==null) return;
		final CommandProvider provider = Loader.getProvider(CommandProvider.class);
		for (CommandNode<ServerCommandSource> node : d.getRoot().getChildren()) {
			try {
				Command cmd = provider.getCommandByName(node.getName());
				Permission perm = cmd.getPermission();
				perm.permission(); // Null check
				registerPermission(perm);
				// Attempt to register sub command permissions too
				ITabable[] tabs = cmd.getParams();
				if (tabs!=null && tabs.length>0) {
					for (ITabable tab : tabs) {
						if (tab==null) continue;
						if (tab instanceof TabableSubCommand) {
							SubCommand[] subs = ((TabableSubCommand)tab).getSubCommands();
							if (subs==null || subs.length<1) break;
							for (SubCommand sub : subs) {
								if (sub==null) continue;
								Permission subPerm = sub.getPermission();
								if (subPerm!=null) {
									registerPermission(subPerm);
								}
							}
							break;
						}
					}
				} 
			} catch (Throwable e) {
				registerPermission(new Permission("command."+node.getName(), -3));
			}
		}
	}
	
	private void overrideCommands() {
		MinecraftServer server = Loader.getMinecraftServer();
		if (server==null) return;
		CommandDispatcher<ServerCommandSource> commandDispatcher = server.getCommandManager().getDispatcher();
		try {
			Field field = CommandNode.class.getDeclaredField("requirement");
			field.setAccessible(true);
			for (CommandNode<ServerCommandSource> node : commandDispatcher.getRoot().getChildren()) {
				final Predicate<ServerCommandSource> old = node.getRequirement();
				field.set(node, new Predicate<ServerCommandSource>() {

					@Override
					public boolean test(ServerCommandSource t) {
						ServerPlayerEntity player = Loader.getServerPlayerEntity(t);
						Permission permission = permissions.get(getPerm(node));
						if (permission.opLevel()==-3 || player==null) return old.test(t);
						boolean b = hasPermission(player, getPerm(node));
						if (debug && !b) getProviderLogger().info("Hid the command "+node.getName()+" from the player "+player.getName().asTruncatedString(32)+" due to the missing permission "+permission.permission());
						return b;
						//return hasPermission(player, getPerm(node));
					}
					
				});
			}
		} catch (Throwable t) {
			return;
		}
	}
	
	private String getPerm(CommandNode<?> node) {
		try {
			return Loader.getProvider(CommandProvider.class).getCommandByName(node.getName()).getPermission().permission();
		} catch (Throwable t) {
			return "command."+node.getName();
		}
	}
	
}
