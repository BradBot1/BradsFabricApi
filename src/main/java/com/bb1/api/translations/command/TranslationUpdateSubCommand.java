package com.bb1.api.translations.command;

import org.jetbrains.annotations.Nullable;

import com.bb1.api.commands.SubCommand;
import com.bb1.api.permissions.DefaultPermissions;
import com.bb1.api.translations.DefaultTranslations;
import com.bb1.api.translations.TranslationManager;

import net.minecraft.server.command.ServerCommandSource;

public class TranslationUpdateSubCommand extends SubCommand {

	public TranslationUpdateSubCommand() {
		super("update");
	}

	@Override
	public int execute(ServerCommandSource source, String alias, String[] params) {
		TranslationManager.get().pushAllTranslations(true);
		source.sendFeedback(DefaultTranslations.TRANSLATIONS_UPDATED, true);
		return 1;
	}
	
	@Override
	public @Nullable String getPermission() {
		return DefaultPermissions.TRANSLATION_MODIFY;
	}

}
