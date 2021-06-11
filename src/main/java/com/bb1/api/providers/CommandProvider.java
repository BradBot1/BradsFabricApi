package com.bb1.api.providers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.bb1.api.commands.Command;

public interface CommandProvider extends Provider {
	
	public void registerCommand(@NotNull Command command);
	
	@Nullable
	public Command getCommandByName(@NotNull String name);
	
}
