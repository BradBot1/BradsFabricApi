package com.bb1.api.config.command;

import com.bb1.api.commands.Command;
import com.bb1.api.commands.tab.ITabable;
import com.bb1.api.commands.tab.TabableSubCommand;
import com.bb1.api.translations.DefaultTranslations;

import net.minecraft.server.command.ServerCommandSource;

public class ConfigCommand extends Command {

	public ConfigCommand() {
		super("config");
	}

	@Override
	public String getDescription() {
		return "A command to modify configs";
	}

	@Override
	public int execute(ServerCommandSource source, String alias, String[] params) {
		source.sendFeedback(DefaultTranslations.NEED_ARGUMENTS, false);
		return 1;
	}
	
	@Override
	public ITabable[] getParams() {
		return new ITabable[] { new TabableSubCommand( new ConfigCheckSubCommand(), new ConfigSetSubCommand() ) };
	}

}
