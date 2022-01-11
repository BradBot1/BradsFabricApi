package com.bb1.fabric.bfapi;

import static com.bb1.fabric.bfapi.Constants.ID;
import static com.bb1.fabric.bfapi.Constants.LOGGER;
import static com.bb1.fabric.bfapi.Constants.NAME;
import static com.bb1.fabric.bfapi.Constants.VERSION;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.bb1.fabric.bfapi.config.Config;
import com.bb1.fabric.bfapi.events.Event;
import com.bb1.fabric.bfapi.nbt.mark.INbtMarkListenerHandler;
import com.bb1.fabric.bfapi.permissions.PermissionConfig;
import com.bb1.fabric.bfapi.permissions.database.SimplePermissionDatabase;
import com.bb1.fabric.bfapi.timings.IScheduler;
import com.bb1.fabric.bfapi.timings.ThreadedScheduler;
import com.bb1.fabric.bfapi.utils.Container;
import com.bb1.fabric.bfapi.utils.Field;
import com.bb1.fabric.bfapi.utils.Inputs.Input;
import com.bb1.fabric.bfapi.utils.Inputs.QuintInput;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Loader implements ModInitializer {
	
	private static Loader _INSTANCE;
	/** @return the instance of Loader or null if the mod has yet to init */
	public static final @Nullable Loader getInstance() { return _INSTANCE; }
	
	private static final IScheduler SCHEDULER = new ThreadedScheduler();
	
	public static @NotNull IScheduler getScheduler() { return SCHEDULER; }
	
	public static final Event<Input<Loader>> LOADER_LOADED = new Event<Input<Loader>>(new Identifier(ID, "loaded"));
	/**
	 * Called when an itemstack is used that has the mark
	 */
	public static final Event<QuintInput<ItemStack, World, BlockPos, Field<Entity>, Container<Boolean>>> MARK_ITEM_USED = new Event<QuintInput<ItemStack, World, BlockPos, Field<Entity>, Container<Boolean>>>(new Identifier(ID, "marked_item_used"));
	/**
	 * Called when an entity with the mark is interacted with
	 */
	public static final Event<QuintInput<Field<Entity>, World, Field<Entity>, ItemStack, Container<Boolean>>> MARK_ENTITY_HIT = new Event<QuintInput<Field<Entity>, World, Field<Entity>, ItemStack, Container<Boolean>>>(new Identifier(ID, "marked_entity_hit"));
	/**
	 * Called when an itemstack is used that has the mark
	 */
	public static final Event<QuintInput<ItemStack, World, BlockPos, Field<Entity>, Container<Boolean>>> MARK_ARMOUR_USED = new Event<QuintInput<ItemStack, World, BlockPos, Field<Entity>, Container<Boolean>>>(new Identifier(ID, "marked_armour_used"));
	
	@Override
	public void onInitialize() {
		_INSTANCE = this;
		LOGGER.info("Isn't "+NAME+" "+VERSION+" funky?");
		Config.init();
		loadPermissions();
		setupMarkHandling();
		LOADER_LOADED.emit(Input.of(this));
	}
	
	private void setupMarkHandling() {
		new INbtMarkListenerHandler().register();
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
