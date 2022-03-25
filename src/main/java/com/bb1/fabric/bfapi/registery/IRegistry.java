package com.bb1.fabric.bfapi.registery;

import java.util.Collection;
import java.util.Map.Entry;

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
	
	public Collection<Entry<Identifier, T>> getEntries();
	/**
	 * A proxy to {@link #add(Identifier, Object)} to minimise changes needed to maintain dependencies
	 */
	public default @Nullable T register(@NotNull Identifier identifier, @NotNull T registree) { return this.add(identifier, registree); }
	
}
