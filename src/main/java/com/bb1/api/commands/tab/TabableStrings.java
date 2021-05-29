package com.bb1.api.commands.tab;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class TabableStrings implements ITabable {
	
	private final String name;
	private List<Text> string = new ArrayList<>();
	
	public TabableStrings(@NotNull String name, @NotNull String... strings) {
		this.name = name;
		for (String s : strings) 
			this.string.add(new LiteralText(s));
	}
	
	@Override
	public List<Text> getTabable(ServerCommandSource commandSource, String[] params) {
		return string;
	}
	
	@Override
	public String getTabableName() {
		return name;
	}
	
}