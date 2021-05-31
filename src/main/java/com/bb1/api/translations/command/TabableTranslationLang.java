package com.bb1.api.translations.command;

import java.util.List;

import com.bb1.api.commands.tab.ITabable;
import com.bb1.api.translations.TranslationManager;
import com.bb1.utils.CollectionUtils;
import com.bb1.utils.TextUtils;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class TabableTranslationLang implements ITabable {

	@Override
	public String getTabableName() {
		return "translation_lang";
	}

	@Override
	public List<Text> getTabable(ServerCommandSource commandSource, String[] params) {
		return CollectionUtils.convert(TextUtils.convert(TranslationManager.get().getLangs()));
	}

}
