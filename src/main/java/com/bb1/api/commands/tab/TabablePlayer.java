package com.bb1.api.commands.tab;

import java.util.ArrayList;
import java.util.List;

import com.bb1.api.Loader;
import com.bb1.utils.TextUtils;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class TabablePlayer implements ITabable {
	
	public List<Text> getPlayerNames() {
		List<Text> list = new ArrayList<>();
		for (ServerPlayerEntity serverPlayerEntity : Loader.getMinecraftServer().getPlayerManager().getPlayerList()) {
			list.add(serverPlayerEntity.getDisplayName());
		}
		return list;
	}
	
	@Override
	public List<Text> getTabable(ServerCommandSource commandSource, String[] params) {
		return TextUtils.sortAlphabetically(TextUtils.copyPartialMatches(params[params.length-1], getPlayerNames()));
	}

	@Override
	public String getTabableName() {
		return "player";
	}
	
}