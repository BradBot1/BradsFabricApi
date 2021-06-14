package com.bb1.api.config.command;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

import com.bb1.api.commands.tab.ITabable;
import com.bb1.api.config.Config;
import com.bb1.api.events.Events;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

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
public class TabableConfigOptionList implements ITabable {

	protected static final JsonParser PARSER = new JsonParser();
	public static Map<String, Map<String, JsonElement>> configMap = new HashMap<String, Map<String, JsonElement>>();
	private static boolean V = false;
	
	private void loadConfigs() {
		if (!V) {
			V = true;
			Events.CONFIG_EVENT.register((e) -> { // This just makes our index change when they save or refresh
				switch (e.getType()) {
				case REFRESH:
				case SAVE:
					File config = new File(Config.CONFIG_DIRECTORY+e.getConfig()+".json");
					if (config==null || !config.exists() || !config.isFile()) return;
					try {
						ArrayList<String> r = new ArrayList<String>();
						Scanner s = new Scanner(config);
						while (s.hasNext()) {
							r.add(s.nextLine());
						}
						s.close();
						JsonObject contents = PARSER.parse(String.join("", r)).getAsJsonObject();
						Map<String, JsonElement> set = new HashMap<String, JsonElement>();
						for (Entry<String, JsonElement> entry : contents.entrySet()) {
							set.put(entry.getKey(), entry.getValue());
						}
						configMap.put(fixName(config.getName()), set);
					} catch (Throwable t) { }
					break;
				default:
					break;
				}
			});
		}
		if (!configMap.isEmpty()) return; // Already loaded
		File directory = new File(Config.CONFIG_DIRECTORY);
		if (directory.exists()) {
			File[] configs = directory.listFiles();
			if (configs==null || configs.length<1) return;
			for (File config : configs) {
				if (config==null || !config.exists() || !config.isFile()) continue;
				try {
					ArrayList<String> r = new ArrayList<>();
					Scanner s = new Scanner(config);
					while (s.hasNext()) {
				    	r.add(s.nextLine());
					}
					s.close();
					JsonObject contents = PARSER.parse(String.join("", r)).getAsJsonObject();
					Map<String, JsonElement> set = new HashMap<String, JsonElement>();
					for (Entry<String, JsonElement> entry : contents.entrySet()) {
						set.put(entry.getKey(), entry.getValue());
					}
					configMap.put(fixName(config.getName()), set);
				} catch (Throwable e) { }
			}
		}
	}
	
	public TabableConfigOptionList() {
		loadConfigs();
	}

	@Override
	public String getTabableName() {
		return "config";
	}

	@Override
	public List<Text> getTabable(ServerCommandSource commandSource, String[] params) {
		List<Text> list = new ArrayList<Text>();
		if (params==null || params.length<1) return list;
		Set<String> set = null;
		String name = "";
		if (params.length>1) {
			for (int i = 0; set==null && i <= params.length; i++) { // Starts at 0 because elsewise it don't work properly
				try {
					name = fixName(params[params.length-i]); 
					set = configMap.get(name).keySet();
				} catch (Throwable e) { }
			}
			if (set==null) set = new HashSet<String>();
		} else {
			name = fixName(params[params.length-1]);
			set = configMap.getOrDefault(name, new HashMap<String, JsonElement>()).keySet();
		}
		for (String s : set) {
			list.add(new LiteralText(s));
		}
		return list;
	}
	
	@Nullable
	public static String fixName(@Nullable String name) {
		return (name==null) ? null : name.toLowerCase().replaceFirst("[\\.]\\w+$", "");
	}

}
