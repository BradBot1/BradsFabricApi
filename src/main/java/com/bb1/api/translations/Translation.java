package com.bb1.api.translations;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

import net.minecraft.text.TranslatableText;

public record Translation(@NotNull String translation_key, @NotNull Map<String, String> translationMap) {
	
	public TranslatableText translatableText() { return new TranslatableText(translation_key()); }
	
}