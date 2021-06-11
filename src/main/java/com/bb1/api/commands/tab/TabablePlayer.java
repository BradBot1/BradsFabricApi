package com.bb1.api.commands.tab;

import java.util.ArrayList;
import java.util.List;

import com.bb1.api.Loader;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

/**
 * Copyright 2021 BradBot_1
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class TabablePlayer implements ITabable {
	
	public TabablePlayer() {
		
	}

	@Override
	public List<Text> getTabable(ServerCommandSource commandSource, String[] params) {
		List<Text> list = new ArrayList<>();
		for (ServerPlayerEntity serverPlayerEntity : Loader.getMinecraftServer().getPlayerManager().getPlayerList()) {
			list.add(serverPlayerEntity.getDisplayName());
		}
		return list;
	}

	@Override
	public String getTabableName() {
		return "player";
	}
	
}