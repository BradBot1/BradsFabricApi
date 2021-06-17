package com.bb1.api.commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.bb1.api.Loader;
import com.bb1.api.commands.tab.ITabable;
import com.bb1.api.events.Events;
import com.bb1.api.providers.CommandProvider;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

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
public final class CommandManager implements CommandProvider {
	
	private static final CommandManager COMMAND_MANAGER = new CommandManager();
	
	public static CommandManager get() {
		return COMMAND_MANAGER;
	}
	
	private Set<RegisterableCommand> commands = new HashSet<RegisterableCommand>();
	
	private boolean debug = Loader.CONFIG.debugMode;
	
	private CommandManager() {
		if (!Loader.CONFIG.loadCommandProvider) return;
		Events.LOAD_EVENT.register((event)->{
			for (Command command : callInfoEventAndGet(Command.class)) {
				registerCommand(command);
			}
		});
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			for (RegisterableCommand registerableCommand : commands) {
				final Command command = registerableCommand.getInner();
				LiteralArgumentBuilder<ServerCommandSource> lab = LiteralArgumentBuilder.literal(command.getName());
				lab.then(addOn(command.getParams(), registerableCommand));
				lab.executes(context -> {
					final String given = context.getInput();
					String[] split = given.split(" ");
					if (!split[0].startsWith("/")) return 0;
					final String commandName = split[0].replaceFirst("/", "");
					return registerableCommand.execute(context.getSource(), commandName, given.replaceFirst("/"+commandName+" ", "").split(" "));
				});
				dispatcher.register(lab);
				if (command.getAliases()!=null && command.getAliases().size()>0) {
					for (String s : command.getAliases()) {
						LiteralArgumentBuilder<ServerCommandSource> lab2 = LiteralArgumentBuilder.literal(s);
						lab2.then(addOn(command.getParams(), registerableCommand));
						lab2.executes(context -> {
							final String given = context.getInput();
							String[] split = given.split(" ");
							if (!split[0].startsWith("/")) return 0;
							final String commandName = split[0].replaceFirst("/", "");
							return registerableCommand.execute(context.getSource(), commandName, given.replaceFirst("/"+commandName+" ", "").split(" "));
						});
					}
				}
			}
		});
	}
	
	protected RequiredArgumentBuilder<ServerCommandSource, String> addOn(@Nullable ITabable[] tabs, RegisterableCommand registerableCommand) {
		return loop(create((tabs==null || tabs.length<1 || tabs[0]==null) ? "Argument0" : tabs[0].getTabableName(), registerableCommand), 1, 8, tabs, registerableCommand);
	}
	
	protected RequiredArgumentBuilder<ServerCommandSource, String> loop(RequiredArgumentBuilder<ServerCommandSource, String> origin, int current, int maxSize, @Nullable ITabable[] tabs, RegisterableCommand registerableCommand) {
		// System.err.println(""+current+"/"+maxSize);
		if (current>=maxSize) return origin.then(create((tabs==null || tabs.length<(current+1) || tabs[current]==null) ? "Argument"+Integer.toString(current) : tabs[current].getTabableName(), registerableCommand));
		return origin.then(loop(create((tabs==null || tabs.length<(current+1) || tabs[current]==null) ? "Argument"+Integer.toString(current) : tabs[current].getTabableName(), registerableCommand), current+1, maxSize, tabs, registerableCommand));
	}
	
	protected RequiredArgumentBuilder<ServerCommandSource, String> create(String name, RegisterableCommand registerableCommand) {
		return net.minecraft.server.command.CommandManager.argument(name, StringArgumentType.word())
				.executes(context -> {
					final String given = context.getInput();
					String[] split = given.split(" ");
					if (!split[0].startsWith("/")) return 0;
					final String commandName = split[0].replaceFirst("/", "");
					return registerableCommand.execute(context.getSource(), commandName, given.replaceFirst("/"+commandName+" ", "").split(" "));
				})
				.suggests((s, a) -> {
					try {
						final String given = s.getInput();
						String[] split = given.split(" ");
						if (!split[0].startsWith("/")) return a.buildFuture();
						final String commandName = split[0].replaceFirst("/", "");
						String string = given.replaceFirst("/"+commandName+" ", "");
						String[] split2 = (string.endsWith(" ")) ? Arrays.asList(string.split(" ")).toArray(new String[string.split(" ").length+1]) : string.split(" ");
						List<Text> text = registerableCommand.tabComplete(s.getSource(), commandName, split2);
						if (text.size()<1) text.add(new LiteralText("?"));
						for (Text t : text) {
							a.suggest(t.asTruncatedString(64));
						}
					} catch (Throwable e) {
						a.suggest("?");
					}
					return a.buildFuture();
				});
	}
	
	public void registerCommand(@NotNull Command command) {
		this.commands.add(new RegisterableCommand(command));
		if (debug) getProviderLogger().info("Registered the command "+command.name);
	}
	
	@Nullable
	public Command getCommandByName(@NotNull String name) {
		RegisterableCommand registerableCommand = getRegisterableByName(name);
		if (registerableCommand!=null) return registerableCommand.getInner();
		if (debug) getProviderLogger().error("Failed to find the registered command \""+name+"\", it must be an external command");
		return null;
	}
	
	@Nullable
	public RegisterableCommand getRegisterableByName(@NotNull String name) {
		for (RegisterableCommand registerableCommand : this.commands) {
			final Command command = registerableCommand.getInner();
			if (command.getName().equalsIgnoreCase(name)) {
				return registerableCommand;
			}
			if (command.getAliases()!=null && command.getAliases().size()>0) {
				for (String string : command.getAliases()) {
					if (string.equalsIgnoreCase(name)) {
						return registerableCommand;
					}
				}
			}
		}
		return null;
	}

	@Override
	public @NotNull String getProviderName() { return "CommandManager"; }
	
	private Map<UUID, Set<String>> blockedMap = new HashMap<UUID, Set<String>>();
	
	@Override
	public void disableCommand(PlayerEntity player, String commandName) {
		Set<String> set = blockedMap.getOrDefault(player.getUuid(), new HashSet<String>());
		set.add(commandName);
		blockedMap.put(player.getUuid(), set);
		if (player instanceof ServerPlayerEntity server) { Loader.getCommandManager().sendCommandTree(server); }
	}

	@Override
	public void enableCommand(PlayerEntity player, String commandName) {
		Set<String> set = blockedMap.getOrDefault(player.getUuid(), new HashSet<String>());
		set.remove(commandName);
		blockedMap.put(player.getUuid(), set);
		if (player instanceof ServerPlayerEntity server) { Loader.getCommandManager().sendCommandTree(server); }
	}

	@Override
	public boolean isCommandEnabled(PlayerEntity player, String commandName) { return !blockedMap.getOrDefault(player.getUuid(), new HashSet<String>()).contains(commandName); }
	
}
