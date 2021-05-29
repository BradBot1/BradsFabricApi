package com.bb1.api.translations;

import com.bb1.api.Loader;

import net.minecraft.text.TranslatableText;

public final class DefaultTranslations {
	
	// ERRORS
	
	public static final TranslatableText PLAYER_ONLY = Loader.getTranslatableText("error.player_only");
	public static final TranslatableText CONSOLE_ONLY_COMMAND = Loader.getTranslatableText("error.console_only_command");
	public static final TranslatableText PLAYER_ONLY_COMMAND = Loader.getTranslatableText("error.player_only_command");
	public static final TranslatableText NEED_PERMISSIONS = Loader.getTranslatableText("error.required_permission");
	/** When something is executed with improper arguments */
	public static final TranslatableText NEED_ARGUMENTS = Loader.getTranslatableText("error.required_arguments");
	
	private DefaultTranslations() {} // So no instance can be made of this class
}
