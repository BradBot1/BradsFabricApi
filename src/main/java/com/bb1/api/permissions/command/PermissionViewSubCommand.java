package com.bb1.api.permissions.command;

import org.jetbrains.annotations.Nullable;

import com.bb1.api.Loader;
import com.bb1.api.commands.SubCommand;
import com.bb1.api.commands.tab.ITabable;
import com.bb1.api.commands.tab.TabablePlayer;
import com.bb1.api.commands.tab.TabableStrings;
import com.bb1.api.permissions.DefaultPermissions;
import com.bb1.api.permissions.PermissionManager;
import com.bb1.api.translations.DefaultTranslations;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class PermissionViewSubCommand extends SubCommand {

	public PermissionViewSubCommand() {
		super("view");
	}

	@Override
	public int execute(ServerCommandSource source, String alias, String[] params) {
		if (params.length<2) {
			source.sendFeedback(DefaultTranslations.NEED_ARGUMENTS, false);
			return 0;
		}
		ServerPlayerEntity player = Loader.getMinecraftServer().getPlayerManager().getPlayer(params[0]);
		if (player==null) {
			source.sendFeedback(DefaultTranslations.NEED_ARGUMENTS, false);
			return 0;
		}
		source.sendFeedback(PermissionManager.get().hasPermission(player.getUuid(), params[1]) ? DefaultTranslations.PERMISSION_HAS : DefaultTranslations.PERMISSION_DOESNT_HAVE, false);
		return 1;
	}

	@Override
	public @Nullable ITabable[] getParams() {
		return new ITabable[] {new TabablePlayer(), new TabableStrings("permission", DefaultPermissions.PERMISSIONS)};
	}
	
	@Override
	public @Nullable String getPermission() {
		return DefaultPermissions.PERMISSIONS_VIEW;
	}

}
