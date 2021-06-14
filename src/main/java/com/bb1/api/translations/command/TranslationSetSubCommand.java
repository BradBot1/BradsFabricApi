package com.bb1.api.translations.command;

import org.jetbrains.annotations.Nullable;

import com.bb1.api.Loader;
import com.bb1.api.commands.SubCommand;
import com.bb1.api.commands.permissions.Permission;
import com.bb1.api.commands.tab.ITabable;
import com.bb1.api.commands.tab.TabableString;
import com.bb1.api.permissions.DefaultPermissions;
import com.bb1.api.providers.TranslationProvider;
import com.bb1.api.translations.DefaultTranslations;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

/**
 * Copyright 2021 BradBot_1
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class TranslationSetSubCommand extends SubCommand {

	public TranslationSetSubCommand() {
		super("set");
	}

	@Override
	public int execute(ServerCommandSource source, String alias, String[] params) {
		if (params.length<3) {
			source.sendFeedback(DefaultTranslations.NEED_ARGUMENTS.translatableText(), false);
			return 0;
		}
		TranslationProvider translationProvider = Loader.getProvider(TranslationProvider.class);
		if (translationProvider==null) {
			source.sendFeedback(DefaultTranslations.PROVIDER_NOT_FOUND.translatableText(), false);
			return 0;
		}
		final String old = translationProvider.getTranslation(params[0], params[1]);
		String n3w = params[2];
		for(int i = 3; i < params.length; i++) {
			n3w += (" "+params[i]);
		}
		n3w = n3w.substring(0, n3w.length());
		translationProvider.setTranslation(params[0], params[1], n3w);
		source.sendFeedback(new LiteralText("Set \""+old+"\" to \""+n3w+"\" in the lang "+params[1]+" under the key "+params[0]), true);
		return 1;
	}
	
	@Override
	public @Nullable Permission getPermission() {
		return DefaultPermissions.TRANSLATION_MODIFY;
	}

	@Override
	public ITabable[] getParams() {
		return new ITabable[] {new TabableTranslationKey(), new TabableTranslationLang(), new TabableString("new_translation")};
	}

}
