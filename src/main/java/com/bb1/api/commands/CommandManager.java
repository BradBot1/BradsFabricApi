package com.bb1.api.commands;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import com.bb1.api.managers.AbstractManager;

import net.minecraft.server.network.ServerPlayerEntity;

public final class CommandManager extends AbstractManager<CommandProvider> {
	
	private static final CommandManager INSTANCE = new CommandManager();
	
	public static CommandManager getInstance() { return INSTANCE; }
	
	private CommandManager() { }
	
	private Map<ServerPlayerEntity, Set<String>> blockedMap = new HashMap<ServerPlayerEntity, Set<String>>();
	
	private Set<String> blockedSet = new HashSet<String>();
	
	public void disableCommand(@NotNull String commandName) {
		blockedSet.add(commandName);
		for (CommandProvider provider : getProviders()) {
			provider.disableCommand(commandName);
		}
	}
	
	public void enableCommand(@NotNull String commandName) {
		blockedSet.remove(commandName);
		for (CommandProvider provider : getProviders()) {
			provider.enableCommand(commandName);
		}
	}
	
	public void disableCommand(@NotNull ServerPlayerEntity player, @NotNull String commandName) {
		Set<String> set = blockedMap.getOrDefault(player, new HashSet<String>());
		set.add(commandName);
		blockedMap.put(player, set);
		for (CommandProvider provider : getProviders()) {
			provider.disableCommand(player, commandName);
		}
	}
	
	public void enableCommand(@NotNull ServerPlayerEntity player, @NotNull String commandName) {
		Set<String> set = blockedMap.getOrDefault(player, new HashSet<String>());
		set.remove(commandName);
		blockedMap.put(player, set);
		for (CommandProvider provider : getProviders()) {
			provider.enableCommand(player, commandName);
		}
	}
	
	@Override
	protected void onRegister(CommandProvider provider) {
		for (String s : blockedSet) {
			provider.disableCommand(s);
		}
		for (Entry<ServerPlayerEntity, Set<String>> entry : blockedMap.entrySet()) {
			for (String s : entry.getValue()) {
				provider.disableCommand(entry.getKey(), s);
			}
		}
	}
	
}
