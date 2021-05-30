package com.bb1.api.permissions.command;

import java.util.HashSet;
import java.util.Set;

import com.bb1.api.commands.Command;
import com.bb1.api.commands.tab.ITabable;
import com.bb1.api.commands.tab.TabableSubCommand;
import com.bb1.api.translations.DefaultTranslations;

import net.minecraft.server.command.ServerCommandSource;

public class PermissionCommand extends Command {

	public PermissionCommand() {
		super("permission");
	}
	
	@Override
	public Set<String> getAliases() {
		Set<String> set = new HashSet<String>();
		set.add("perm");
		return set;
	}

	@Override
	public int execute(ServerCommandSource source, String alias, String[] params) {
		source.sendFeedback(DefaultTranslations.NEED_ARGUMENTS, false);
		return 0;
	}
	
	@Override
	public ITabable[] getParams() {
		return new ITabable[] {new TabableSubCommand(new PermissionSetSubCommand(), new PermissionViewSubCommand())};
	}

}
