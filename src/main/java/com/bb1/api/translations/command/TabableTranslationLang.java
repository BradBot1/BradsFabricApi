package com.bb1.api.translations.command;

import java.util.ArrayList;
import java.util.List;

import com.bb1.api.commands.tab.ITabable;
import com.bb1.api.translations.TranslationManager;

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
		List<Text> text = new ArrayList<Text>();
		for (String s : TranslationManager.get().getLangs()) {
			text.add(new LiteralText(s));
		}
		return text;
	}

}
