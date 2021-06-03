package com.bb1.api.commands.tab;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public interface ITabable {
	
	@NotNull
	public String getTabableName();
	
	@NotNull
	public List<Text> getTabable(ServerCommandSource commandSource, String[] params);
	
}
