package com.bb1.api.config.command;

import java.util.HashMap;
import java.util.Map.Entry;

import com.bb1.api.commands.SubCommand;
import com.bb1.api.commands.permissions.Permission;
import com.bb1.api.commands.tab.ITabable;
import com.bb1.api.permissions.DefaultPermissions;
import com.bb1.api.translations.DefaultTranslations;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ConfigCheckSubCommand extends SubCommand {

	public ConfigCheckSubCommand() {
		super("check");
	}

	@Override
	public int execute(ServerCommandSource source, String alias, String[] params) {
		if (params==null || params.length<1) {
			source.sendFeedback(DefaultTranslations.NEED_ARGUMENTS, false);
			return 0;
		}
		if (params.length>1) {
			source.sendFeedback(
					new LiteralText("[--").formatted(Formatting.GRAY)
						.append(new LiteralText(params[0]).formatted(Formatting.AQUA)
							.append(new LiteralText("--]").formatted(Formatting.GRAY)
								.append(new LiteralText("\n")
									.append(new LiteralText(params[1]).formatted(Formatting.DARK_AQUA)
										.append(new LiteralText(" - ").formatted(Formatting.GRAY)
											.append(new LiteralText(convert(TabableConfigOptionList.configMap.getOrDefault(TabableConfigOptionList.fixName(params[0]), new HashMap<String, JsonElement>()).getOrDefault(params[1], new JsonPrimitive("?")))).formatted(Formatting.DARK_AQUA)
											)
										)
									)
								)
							)
						)
					, false);
		} else {
			MutableText text = new LiteralText("[--").formatted(Formatting.GRAY).append(new LiteralText(params[0]).formatted(Formatting.AQUA).append(new LiteralText("--]").formatted(Formatting.GRAY).append(new LiteralText("\n"))));
			Text text2 = new LiteralText(" - ").formatted(Formatting.GRAY);
			Text text3 = new LiteralText("\n");
			for (Entry<String, JsonElement> entry : TabableConfigOptionList.configMap.getOrDefault(TabableConfigOptionList.fixName(params[0]), new HashMap<String, JsonElement>()).entrySet()) {
				text = text.append(new LiteralText(entry.getKey()).formatted(Formatting.DARK_AQUA)).append(text2).append(new LiteralText(convert(entry.getValue())).formatted(Formatting.DARK_AQUA)).append(text3);
			}
			source.sendFeedback(text, false);
		}
		return 1;
	}

	@Override
	public ITabable[] getParams() {
		return new ITabable[] { new TabableConfigList(), new TabableConfigOptionList() };
	}
	
	@Override
	public Permission getPermission() {
		return DefaultPermissions.CONFIG_VIEW;
	}
	
	protected static final Gson GSON = new Gson();
	
	protected static String convert(JsonElement jsonElement) {
		return (jsonElement.isJsonPrimitive()) ? jsonElement.getAsString() : GSON.toJson(jsonElement);
	}
	
}
