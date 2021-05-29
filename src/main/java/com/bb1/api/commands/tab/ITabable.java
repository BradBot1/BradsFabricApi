package com.bb1.api.commands.tab;

import java.util.List;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public interface ITabable {
	
	public String getTabableName();
	
	public List<Text> getTabable(ServerCommandSource commandSource, String[] params);
	
}
