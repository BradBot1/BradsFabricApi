package com.bb1.api.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import com.bb1.api.providers.Provider;

import net.minecraft.server.network.ServerPlayerEntity;

public interface CommandProvider extends Provider {
	
	public void disableCommand(@NotNull String commandName);
	
	public void enableCommand(@NotNull String commandName);
	
	public void disableCommand(@NotNull ServerPlayerEntity player, @NotNull String commandName);
	
	public void enableCommand(@NotNull ServerPlayerEntity player, @NotNull String commandName);
	
	@Override
	default Logger getProviderLogger() { return LogManager.getLogger("CommandProvider | "+getProviderName()); }
	
}
