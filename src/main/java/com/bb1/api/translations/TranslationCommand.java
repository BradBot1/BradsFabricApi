package com.bb1.api.translations;

import com.bb1.api.commands.Command;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;

public final class TranslationCommand extends Command {

	@Override
	public String getName() {
		return "translate";
	}

	@Override
	public String getDescription() {
		return "A command to control translated text";
	}

	@Override
	public int execute(ServerCommandSource source, String alias, String[] params) {
		if (params==null || params.length<1) {
			source.sendFeedback(DefaultTranslations.NEED_ARGUMENTS, false);
			return 1;
		}
		source.sendFeedback(new TranslatableText(params[0]), false);
		return 1;
	}
	
}
