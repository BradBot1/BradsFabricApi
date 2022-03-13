package com.bb1.fabric.bfapi.registery;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.util.Identifier;

public interface IRegistry<T> {
	
	public @NotNull Identifier getIdentifer();
	
	public @Nullable T add(@NotNull Identifier identifier, @NotNull T registree);
	
	public @Nullable T get(@NotNull Identifier identifier);
	
	public @Nullable T remove(@NotNull Identifier identifier);
	
	public boolean contains(@NotNull Identifier identifier);
	
	default void onRegister(@NotNull Identifier identifier, @NotNull T registree) { }
	
}
