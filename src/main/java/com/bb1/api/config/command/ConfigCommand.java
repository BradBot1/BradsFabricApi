package com.bb1.api.config.command;

import com.bb1.api.commands.Command;
import com.bb1.api.commands.tab.ITabable;
import com.bb1.api.commands.tab.TabableSubCommand;
import com.bb1.api.translations.DefaultTranslations;

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
		source.sendFeedback(DefaultTranslations.NEED_ARGUMENTS.translatableText(), false);
		return 1;
	}
	
	@Override
	public ITabable[] getParams() {
		return new ITabable[] { new TabableSubCommand( new ConfigCheckSubCommand(), new ConfigSetSubCommand() ) };
	}

}
