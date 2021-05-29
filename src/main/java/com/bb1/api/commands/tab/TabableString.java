package com.bb1.api.commands.tab;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class TabableString implements ITabable {
	
	private List<Text> string = new ArrayList<Text>();
	
	public TabableString(String string) {
		this.string.add(new LiteralText(string));
	}
	
	@Override
	public List<Text> getTabable(ServerCommandSource commandSource, String[] params) {
		return string;
	}
	
	@Override
	public String getTabableName() {
		return string.get(0).asTruncatedString(32);
	}
	
}