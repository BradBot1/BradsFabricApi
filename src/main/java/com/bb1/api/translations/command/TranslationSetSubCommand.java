package com.bb1.api.translations.command;

import org.jetbrains.annotations.Nullable;

import com.bb1.api.commands.SubCommand;
import com.bb1.api.commands.tab.ITabable;
import com.bb1.api.commands.tab.TabableString;
import com.bb1.api.permissions.DefaultPermissions;
import com.bb1.api.translations.DefaultTranslations;
import com.bb1.api.translations.TranslationManager;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

public class TranslationSetSubCommand extends SubCommand {

	public TranslationSetSubCommand() {
		super("set");
	}

	@Override
	public int execute(ServerCommandSource source, String alias, String[] params) {
		if (params.length<3) {
			source.sendFeedback(DefaultTranslations.NEED_ARGUMENTS, false);
			return 0;
		}
		TranslationManager translationManager = TranslationManager.get();
		final String old = translationManager.translate(params[0], params[1]);
		String n3w = params[2];
		for(int i = 3; i < params.length; i++) {
			n3w += (" "+params[i]);
		}
		n3w = n3w.substring(0, n3w.length());
		translationManager.set(params[0], params[1], n3w);
		source.sendFeedback(new LiteralText("Set \""+old+"\" to \""+n3w+"\" in the lang "+params[1]+" under the key "+params[0]), true);
		return 1;
	}
	
	@Override
	public @Nullable String getPermission() {
		return DefaultPermissions.TRANSLATION_MODIFY;
	}

	@Override
	public ITabable[] getParams() {
		return new ITabable[] {new TabableTranslationKey(), new TabableTranslationLang(), new TabableString("new_translation")};
	}

}
