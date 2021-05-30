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

public class TabableTranslationLang implements ITabable {

	@Override
	public String getTabableName() {
		return "translation_lang";
	}

	@Override
	public List<Text> getTabable(ServerCommandSource commandSource, String[] params) {
		JsonObject jsonObject = TranslationManager.get().convertLangToJson();
		List<Text> list = new ArrayList<Text>();
		for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
			list.add(new LiteralText(entry.getKey()));
		}
		return list;
	}

}
