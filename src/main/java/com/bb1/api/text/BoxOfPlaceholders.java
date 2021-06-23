package com.bb1.api.text;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.bb1.api.Loader;
import com.bb1.api.adapters.AbstractAdapter;

import eu.pb4.placeholders.PlaceholderAPI;
import eu.pb4.placeholders.TextParser;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class BoxOfPlaceholders extends AbstractAdapter implements TextProvider {

	public BoxOfPlaceholders() { super("placeholder-api"); }

	@Override
	protected void load() { TextManager.getInstance().registerProvider(this); }
	
	@Override
	public Text parse(@NotNull Text text, @Nullable ServerPlayerEntity player) {
		return TextParser.parse(placeHolder(text, player).asTruncatedString(256));
	}
	
	private Text placeHolder(@NotNull Text text, @Nullable ServerPlayerEntity player) {
		if (player==null) return PlaceholderAPI.parseText(text, Loader.getMinecraftServer());
		return PlaceholderAPI.parseText(text, player);
	}

}
