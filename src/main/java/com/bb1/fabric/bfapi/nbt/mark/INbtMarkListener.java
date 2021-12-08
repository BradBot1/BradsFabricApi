package com.bb1.fabric.bfapi.nbt.mark;

import static com.bb1.fabric.bfapi.registery.BFAPIRegistry.MARK_LISTENER;

import org.jetbrains.annotations.NotNull;

import com.bb1.fabric.bfapi.registery.IRegisterable;
import com.bb1.fabric.bfapi.utils.Field;

import net.minecraft.entity.Entity;
import net.minecraft.util.registry.Registry;

public interface INbtMarkListener extends IRegisterable {
	
	public @NotNull String getMark();
	
	public boolean onUse(@NotNull Field<Entity> user, @NotNull Field<Markable> markedObject);
	
	public default void register(String name) {
		Registry.register(MARK_LISTENER, name, this);
	}
	
}
