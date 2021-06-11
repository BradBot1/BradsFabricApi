package com.bb1.api.commands.tab;

import java.util.ArrayList;
import java.util.List;

import com.bb1.api.commands.SubCommand;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
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
public final class TabableSubCommand implements ITabable {
	
	private SubCommand[] subCommands;
	
	public TabableSubCommand(SubCommand... subCommands) {
		this.subCommands = subCommands;
	}
	
	public SubCommand getSubCommand(String name) {
		for (SubCommand subCommand : subCommands) {
			if (subCommand.getName().equalsIgnoreCase(name)) {
				return subCommand;
			} else {
				if (subCommand.getAliases()==null || subCommand.getAliases().isEmpty()) continue;
				for (String s : subCommand.getAliases()) {
					if (s.equalsIgnoreCase(name)) {
						return subCommand;
					}
				}
			}
		}
		return null;
	}
	
	public SubCommand[] getSubCommands() {
		return this.subCommands;
	}
	
	@Override
	public List<Text> getTabable(ServerCommandSource commandSource, String[] params) {
		List<Text> list = new ArrayList<>();
		for (SubCommand subCommand : subCommands) {
			list.add(new LiteralText(subCommand.getName()));
			if (subCommand.getAliases()!=null) {
				for (String s : subCommand.getAliases()) {
					list.add(new LiteralText(s));
				}
			}
		}
		return list;
	}

	@Override
	public String getTabableName() {
		return "subcommand";
	}

}