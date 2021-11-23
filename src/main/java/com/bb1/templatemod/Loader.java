package com.bb1.templatemod;

import net.fabricmc.api.ModInitializer;

import static com.bb1.templatemod.Constants.LOGGER;
import static com.bb1.templatemod.Constants.NAME;
import static com.bb1.templatemod.Constants.VERSION;

import org.jetbrains.annotations.Nullable;

public class Loader implements ModInitializer {
	
	private static Loader _INSTANCE;
	/** @return the instance of Loader or null if the mod has yet to init */
	public static final @Nullable Loader getInstance() { return _INSTANCE; }

	@Override
	public void onInitialize() {
		_INSTANCE = this;
		LOGGER.info("Isn't "+NAME+" "+VERSION+" funky?");
	}
}
