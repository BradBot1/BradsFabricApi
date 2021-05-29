package com.bb1.api.commands;

import java.util.Set;

import com.bb1.api.commands.tab.ITabable;

/**
 * 
 * @author BradBot_1
 * 
 */
public abstract class SubCommand implements CommandHandler {
	
	public SubCommand() {}
	
	public abstract String getCommandName();
	
	public abstract String getCommandUsage();
	
	public Set<String> getCommandAliases() { return null; }
	
	public String getPermission() { return null; }
	
	public ITabable[] getParams() { return null; }
	
}
