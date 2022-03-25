package com.bb1.fabric.bfapi.text.parser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public interface TextParserLookup {
	/**
	 * Called to look up what the replacement should be
	 * 
	 * @param recipient Provided if and only if the text is to be sent to an entity
	 * @param key The {@link TextParserKey} detected, provided as one parser may serve multiple keys
	 * @param occurrence The numeric occurrence of this match
	 * @return The replacement {@link Text}
	 */
	public Text lookUp(@Nullable ServerCommandSource recipient, @NotNull TextParserKey key, int occurrence);
	
}
