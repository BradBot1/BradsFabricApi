package com.bb1.api.commands;

import net.minecraft.server.command.ServerCommandSource;

@FunctionalInterface
public interface CommandHandler {
	
	public int execute(ServerCommandSource source, String alias, String[] params);
	
}
