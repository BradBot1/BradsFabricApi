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
	
	public SubCommand getCustomSubCommandFromName(String name) {
		for (SubCommand subCommand : subCommands) {
			if (subCommand.getCommandName().equalsIgnoreCase(name)) {
				return subCommand;
			} else {
				if (subCommand.getCommandAliases()==null || subCommand.getCommandAliases().isEmpty()) continue;
				for (String s : subCommand.getCommandAliases()) {
					if (s.equalsIgnoreCase(name)) {
						return subCommand;
					}
				}
			}
		}
		return null;
	}
	
	@Override
	public List<Text> getTabable(ServerCommandSource commandSource, String[] params) {
		List<Text> list = new ArrayList<>();
		for (SubCommand subCommand : subCommands) {
			list.add(new LiteralText(subCommand.getCommandName()));
			if (subCommand.getCommandAliases()!=null) {
				for (String s : subCommand.getCommandAliases()) {
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