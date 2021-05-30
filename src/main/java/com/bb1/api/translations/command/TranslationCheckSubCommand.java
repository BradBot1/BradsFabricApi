package com.bb1.api.translations.command;

import org.jetbrains.annotations.Nullable;

import com.bb1.api.commands.SubCommand;
import com.bb1.api.commands.tab.ITabable;
import com.bb1.api.permissions.DefaultPermissions;
import com.bb1.api.translations.DefaultTranslations;
import com.bb1.api.translations.TranslationManager;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

public class TranslationCheckSubCommand extends SubCommand {

	public TranslationCheckSubCommand() {
		super("check");
	}

	@Override
	public int execute(ServerCommandSource source, String alias, String[] params) {
		if (params.length<2) {
			source.sendFeedback(DefaultTranslations.NEED_ARGUMENTS, false);
			return 0;
		}
		source.sendFeedback(new LiteralText(TranslationManager.get().translate(params[0], params[1])), false);
		return 1;
	}
	
	@Override
	public @Nullable String getPermission() {
		return DefaultPermissions.TRANSLATION_VIEW;
	}

	@Override
	public ITabable[] getParams() {
		return new ITabable[] {new TabableTranslationKey(), new TabableTranslationLang()};
	}

}
