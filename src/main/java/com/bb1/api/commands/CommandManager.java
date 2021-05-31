package com.bb1.api.commands;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.bb1.api.commands.tab.ITabable;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public final class CommandManager {
	
	private static final CommandManager COMMAND_MANAGER = new CommandManager();
	
	public static CommandManager get() {
		return COMMAND_MANAGER;
	}
	
	private Set<RegisterableCommand> commands = new HashSet<RegisterableCommand>();
	
	private CommandManager() {
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
						dispatcher.register(LiteralArgumentBuilder.literal(s));
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
	}
	
	@Nullable
	public Command getCommandByName(@NotNull String name) {
		for (RegisterableCommand registerableCommand : this.commands) {
			final Command command = registerableCommand.getInner();
			if (command.getName().equalsIgnoreCase(name)) {
				return command;
			}
			if (command.getAliases()!=null && command.getAliases().size()>0) {
				for (String string : command.getAliases()) {
					if (string.equalsIgnoreCase(name)) {
						return command;
					}
				}
			}
		}
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
	
}
