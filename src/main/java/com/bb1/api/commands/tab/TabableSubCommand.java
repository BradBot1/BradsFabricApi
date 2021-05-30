package com.bb1.api.commands.tab;

import java.util.ArrayList;
import java.util.List;

import com.bb1.api.commands.SubCommand;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

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