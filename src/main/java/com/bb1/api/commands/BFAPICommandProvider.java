package com.bb1.api.commands;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import com.bb1.api.Loader;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class BFAPICommandProvider implements CommandProvider {
	
	public BFAPICommandProvider() {
		CommandManager.getInstance().registerProvider(this);
		overrideCommands();
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
						if (player==null) return old.test(t);
						return canSee(player, node.getName());
					}
					
				});
			}
		} catch (Throwable t) {
			return;
		}
	}
	
	public boolean canSee(@NotNull ServerPlayerEntity player, @NotNull String commandName) {
		return !blockedSet.contains(commandName) && !blockedMap.getOrDefault(player, new HashSet<String>()).contains(commandName);
	}
	
	@Override
	public String getProviderName() { return "BFAPICommandProvider"; }

	private Map<ServerPlayerEntity, Set<String>> blockedMap = new HashMap<ServerPlayerEntity, Set<String>>();
	
	private Set<String> blockedSet = new HashSet<String>();
	
	@Override
	public void disableCommand(@NotNull String commandName) { blockedSet.add(commandName); }

	@Override
	public void enableCommand(@NotNull String commandName) { blockedSet.remove(commandName); }

	@Override
	public void disableCommand(@NotNull ServerPlayerEntity player, @NotNull String commandName) {
		Set<String> set = blockedMap.getOrDefault(player, new HashSet<String>());
		set.add(commandName);
		blockedMap.put(player, set);
	}

	@Override
	public void enableCommand(@NotNull ServerPlayerEntity player, @NotNull String commandName) {
		Set<String> set = blockedMap.getOrDefault(player, new HashSet<String>());
		set.remove(commandName);
		blockedMap.put(player, set);
	}

}
