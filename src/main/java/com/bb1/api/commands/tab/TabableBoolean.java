package com.bb1.api.commands.tab;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class TabableBoolean implements ITabable {
	
	private List<Text> string = new ArrayList<Text>();
	
	public TabableBoolean() {
		this.string.add(new LiteralText("true"));
		this.string.add(new LiteralText("false"));
	}
	
	@Override
	public List<Text> getTabable(ServerCommandSource commandSource, String[] params) {
		return string;
	}
	
	@Override
	public String getTabableName() {
		return "boolean";
	}
	
}