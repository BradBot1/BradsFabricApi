package com.bb1.api.translations.command;

import org.jetbrains.annotations.Nullable;

import com.bb1.api.commands.SubCommand;
import com.bb1.api.permissions.DefaultPermissions;
import com.bb1.api.translations.DefaultTranslations;
import com.bb1.api.translations.TranslationManager;

import net.minecraft.server.command.ServerCommandSource;

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
