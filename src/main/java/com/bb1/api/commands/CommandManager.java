package com.bb1.api.commands;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.bb1.api.commands.tab.ITabable;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
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
				ITabable[] iTabables = command.getParams();
				if (iTabables!=null && iTabables.length>0) {
					LiteralArgumentBuilder<ServerCommandSource> current = lab;
					for (ITabable iTabable : iTabables) {
						current = current
							.then(net.minecraft.server.command.CommandManager.argument(iTabable.getTabableName(), StringArgumentType.word())
							.executes(context -> {
								final String given = context.getInput();
								String[] split = given.split(" ");
								if (!split[0].startsWith("/")) return 0;
								final String commandName = split[0].replaceFirst("/", "");
								return registerableCommand.execute(context.getSource(), commandName, given.replaceFirst("/"+commandName+" ", "").split(" "));
							})
							.suggests((s, a) -> {
								final String given = s.getInput();
								String[] split = given.split(" ");
								if (!split[0].startsWith("/")) return a.buildFuture();
								final String commandName = split[0].replaceFirst("/", "");
								List<Text> text = registerableCommand.tabComplete(s.getSource(), commandName, given.replaceFirst("/"+commandName+" ", "").split(" "));
								for (Text t : text) {
									a.suggest(t.asTruncatedString(32));
								}
								return a.buildFuture();
							})
						);
					}
				}
				lab.executes(context -> {
					final String given = context.getInput();
					String[] split = given.split(" ");
					if (!split[0].startsWith("/")) return 0;
					final String commandName = split[0].replaceFirst("/", "");
					System.err.println("a");
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
