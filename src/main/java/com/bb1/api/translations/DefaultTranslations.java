package com.bb1.api.translations;

import java.lang.reflect.Field;

import com.bb1.api.Loader;

import net.minecraft.text.TranslatableText;

public final class DefaultTranslations {
	
	// PERMISSIONS
	
	public static final TranslatableText PERMISSION_HAS = Loader.getTranslatableText("permission.has");
	public static final TranslatableText PERMISSION_DOESNT_HAVE = Loader.getTranslatableText("permission.hasnt");
	public static final TranslatableText PERMISSION_TAKEN = Loader.getTranslatableText("permission.taken");
	public static final TranslatableText PERMISSION_GIVEN = Loader.getTranslatableText("permission.given");
	
	// ERRORS
	
	public static final TranslatableText PLAYER_ONLY = Loader.getTranslatableText("error.player_only");
	public static final TranslatableText CONSOLE_ONLY_COMMAND = Loader.getTranslatableText("error.console_only_command");
	public static final TranslatableText PLAYER_ONLY_COMMAND = Loader.getTranslatableText("error.player_only_command");
	public static final TranslatableText NEED_PERMISSIONS = Loader.getTranslatableText("error.required_permission");
	/** When something is executed with improper arguments <i>(This includes if an argument is incorrect)</i>*/
	public static final TranslatableText NEED_ARGUMENTS = Loader.getTranslatableText("error.required_arguments");
	
	private DefaultTranslations() {} // So no instance can be made of this class
	
	public static final void register() {
		TranslationManager translationManager = TranslationManager.get();
		for (Field field : DefaultTranslations.class.getDeclaredFields()) {
			try {
				TranslatableText text = (TranslatableText) field.get(null);
				translationManager.set(text.getKey(), TranslationManager.DEFAULT_LANG, text.getKey());
			} catch (Throwable e) {}
		}
	}
	
}
