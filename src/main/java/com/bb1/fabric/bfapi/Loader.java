package com.bb1.fabric.bfapi;

import static com.bb1.fabric.bfapi.Constants.ID;
import static com.bb1.fabric.bfapi.Constants.LOGGER;
import static com.bb1.fabric.bfapi.Constants.NAME;
import static com.bb1.fabric.bfapi.Constants.VERSION;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.bb1.fabric.bfapi.config.Config;
import com.bb1.fabric.bfapi.events.Event;
import com.bb1.fabric.bfapi.permissions.PermissionConfig;
import com.bb1.fabric.bfapi.permissions.database.SimplePermissionDatabase;
import com.bb1.fabric.bfapi.timings.IScheduler;
import com.bb1.fabric.bfapi.timings.ThreadedScheduler;
import com.bb1.fabric.bfapi.utils.Inputs.Input;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class Loader implements ModInitializer {
	
	private static Loader _INSTANCE;
	/** @return the instance of Loader or null if the mod has yet to init */
	public static final @Nullable Loader getInstance() { return _INSTANCE; }
	
	private static final IScheduler SCHEDULER = new ThreadedScheduler();
	
	public static @NotNull IScheduler getScheduler() { return SCHEDULER; }
	
	public static final Event<Input<Loader>> LOADER_LOADED = new Event<Input<Loader>>(new Identifier(ID, "loaded"));

	@Override
	public void onInitialize() {
		_INSTANCE = this;
		LOGGER.info("Isn't "+NAME+" "+VERSION+" funky?");
		Config.init(this);
		GameObjects.GameEvents.SERVER_START.addHandler((i)->{
			loadPermissions();
		});
		LOADER_LOADED.emit(Input.of(this));
	}
	
	private void loadPermissions() {
		PermissionConfig conf = new PermissionConfig();
		conf.load();
		conf.save();
		if (conf.enabled) {
			new SimplePermissionDatabase(new Identifier(ID, conf.dbName)).register();
		}
	}
	
}
