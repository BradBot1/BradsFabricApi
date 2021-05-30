package com.bb1.api.permissions.command;

import org.jetbrains.annotations.Nullable;

import com.bb1.api.Loader;
import com.bb1.api.commands.SubCommand;
import com.bb1.api.commands.tab.ITabable;
import com.bb1.api.commands.tab.TabableBoolean;
import com.bb1.api.commands.tab.TabablePlayer;
import com.bb1.api.commands.tab.TabableStrings;
import com.bb1.api.permissions.DefaultPermissions;
import com.bb1.api.permissions.PermissionManager;
import com.bb1.api.translations.DefaultTranslations;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class PermissionSetSubCommand extends SubCommand {

	public PermissionSetSubCommand() {
		super("set");
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
		try {
			if (Boolean.parseBoolean(params[2])) {
				PermissionManager.get().givePermission(player.getUuid(), params[1]);
				source.sendFeedback(DefaultTranslations.PERMISSION_GIVEN, true);
			} else {
				PermissionManager.get().takePermission(player.getUuid(), params[1]);
				source.sendFeedback(DefaultTranslations.PERMISSION_TAKEN, true);
			}
		} catch (Throwable e) {
			boolean v = PermissionManager.get().hasPermission(player.getUuid(), params[1]);
			PermissionManager.get().togglePermission(player.getUuid(), params[1]);
			if (v) source.sendFeedback(DefaultTranslations.PERMISSION_TAKEN, true); else source.sendFeedback(DefaultTranslations.PERMISSION_GIVEN, true);
		}
		return 1;
	}
	
	@Override
	public @Nullable ITabable[] getParams() {
		return new ITabable[] {new TabablePlayer(), new TabableStrings("permission", DefaultPermissions.PERMISSIONS), new TabableBoolean()};
	}
	
	@Override
	public @Nullable String getPermission() {
		return DefaultPermissions.PERMISSIONS_MODIFY;
	}

}
