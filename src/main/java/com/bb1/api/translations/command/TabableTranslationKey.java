package com.bb1.api.translations.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.bb1.api.commands.tab.ITabable;
import com.bb1.api.translations.TranslationManager;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
public class TabableTranslationKey implements ITabable {

	@Override
	public String getTabableName() {
		return "translation_key";
	}

	@Override
	public List<Text> getTabable(ServerCommandSource commandSource, String[] params) {
		JsonObject jsonObject = TranslationManager.get().convertLangToJson();
		List<Text> list = new ArrayList<Text>();
		for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
			for (Entry<String, JsonElement> entry2 : entry.getValue().getAsJsonObject().entrySet()) {
				list.add(new LiteralText(entry2.getKey()));
			}
		}
		return list;
	}

}
