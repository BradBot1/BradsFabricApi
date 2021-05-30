package com.bb1.api.commands;

import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.bb1.api.commands.tab.ITabable;

public abstract class SubCommand implements CommandHandler {
	
	protected final String name;
	
	public SubCommand(@NotNull String name) { this.name = name; }
	
	public final String getName() { return this.name; }
	
	public String getCommandUsage() { return getName(); }
	
	@Nullable
	public Set<String> getAliases() { return null; }
	
	@Nullable
	public String getPermission() { return null; }
	
	@Nullable
	public ITabable[] getParams() { return null; }
	
}
