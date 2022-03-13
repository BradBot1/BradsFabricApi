package com.bb1.fabric.bfapi.registery;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.util.Identifier;

public class SimpleUniqueRegistry<T> extends SimpleRegistry<T> implements IValidatedRegistry<T> {
	
	public SimpleUniqueRegistry(Identifier identifier) {
		super(identifier);
	}
	
	@Override
	public @Nullable T add(@NotNull Identifier identifier, @NotNull T registree) {
		if (!validate(identifier, registree)) return null;
		return super.add(identifier, registree);
	}

	@Override
	public boolean validate(@NotNull Identifier identifier, @NotNull T value) {
		return this.contains(identifier);
	}

}
