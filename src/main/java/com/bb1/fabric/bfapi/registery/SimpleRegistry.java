package com.bb1.fabric.bfapi.registery;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.util.Identifier;

public class SimpleRegistry<T> implements IRegistry<T> {
	
	protected final Map<Identifier, T> map = new ConcurrentHashMap<Identifier, T>();
	
	protected final Identifier identifier;
	
	public SimpleRegistry(@NotNull Identifier identifier) {
		this.identifier = identifier;
	}
	
	@Override
	public @Nullable T add(@NotNull Identifier identifier, T registree) {
		map.put(identifier, registree);
		this.onRegister(identifier, registree);
		return registree;
	}

	@Override
	public @Nullable T get(@NotNull Identifier identifier) {
		return this.map.get(identifier);
	}

	@Override
	public @Nullable T remove(@NotNull Identifier identifier) {
		return this.map.remove(identifier);
	}

	@Override
	public boolean contains(@NotNull Identifier identifier) {
		return this.map.containsKey(identifier);
	}
	
	@Override
	public @NotNull Identifier getIdentifer() {
		return this.identifier;
	}
	
}
