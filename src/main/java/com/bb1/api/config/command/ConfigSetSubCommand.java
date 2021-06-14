package com.bb1.api.config.command;

import java.io.BufferedWriter;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import com.bb1.api.commands.SubCommand;
import com.bb1.api.commands.permissions.Permission;
import com.bb1.api.commands.tab.ITabable;
import com.bb1.api.commands.tab.TabableString;
import com.bb1.api.config.Config;
import com.bb1.api.events.Events;
import com.bb1.api.events.Events.ConfigChangeEvent;
import com.bb1.api.events.Events.ConfigChangeEvent.ConfigChangeType;
import com.bb1.api.permissions.DefaultPermissions;
import com.bb1.api.translations.DefaultTranslations;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
public class ConfigSetSubCommand extends SubCommand {

	protected static final JsonParser PARSER = new JsonParser();
	protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	public ConfigSetSubCommand() {
		super("set");
	}

	@Override
	public int execute(ServerCommandSource source, String alias, String[] params) {
		if (params==null || params.length<3) {
			source.sendFeedback(DefaultTranslations.NEED_ARGUMENTS.translatableText(), false);
			return 0;
		}
		final String configName = TabableConfigOptionList.fixName(params[0]);
		final String optionName = params[1];
		String newValue = params[2];
		if (params.length>3) {
			for (int i = 3; i < params.length; i++) {
				newValue = (newValue+" "+params[i]);
			}
		}
		boolean v = false;
		File config = new File(Config.CONFIG_DIRECTORY+configName+".json");
		if (config==null || !config.exists() || !config.isFile()) {
			source.sendFeedback(DefaultTranslations.CONFIG_NOT_FOUND.translatableText(), false);
			return 0;
		}
		JsonObject contents;
		try {
			ArrayList<String> r = new ArrayList<String>();
			Scanner s = new Scanner(config);
			while (s.hasNext()) {
				r.add(s.nextLine());
			}
			s.close();
			contents = PARSER.parse(String.join("", r)).getAsJsonObject();
		} catch (Throwable t) {
			source.sendFeedback(DefaultTranslations.CONFIG_PARSE_FAILED.translatableText(), false);
			return 0;
		}
		JsonElement option = contents.get(optionName);
		if (option.isJsonPrimitive()) {
			contents.addProperty(optionName, newValue);
		} else {// We have to parse it and then set it
			try {
				JsonElement jsonElement = PARSER.parse(newValue);
				if ((jsonElement.isJsonArray() && option.isJsonArray()) || (jsonElement.isJsonObject() && option.isJsonObject())) {
					contents.add(optionName, jsonElement);
				} else {
					contents.add(optionName, jsonElement);
					v = true;
				}
			} catch (Throwable t) {
				source.sendFeedback(DefaultTranslations.NEED_ARGUMENTS.translatableText(), false);
				return 0;
			}
		}
		try {
			BufferedWriter b = new BufferedWriter(new PrintWriter(config));
			b.write(GSON.toJson(contents));
			b.flush();
			b.close();
		} catch (Throwable t) {
			source.sendFeedback(DefaultTranslations.CONFIG_WRITE_FAILED.translatableText(), false);
			return 0;
		}
		if (v) {
			source.sendFeedback(DefaultTranslations.CONFIG_MODIFICATION_SUCCEEDED_BUT_TYPES_DIFFERED.translatableText(), false);
		} else {
			source.sendFeedback(DefaultTranslations.CONFIG_MODIFICATION_SUCCEEDED.translatableText(), false);
		}
		Events.CONFIG_EVENT.onEvent(new ConfigChangeEvent(configName, ConfigChangeType.REFRESH));
		return 1;
	}

	@Override
	public ITabable[] getParams() {
		return new ITabable[] { new TabableConfigList(), new TabableConfigOptionList(), new TabableString("value") };
	}
	
	@Override
	public Permission getPermission() {
		return DefaultPermissions.CONFIG_MODIFY;
	}

}
