package com.bb1.fabric.bfapi.registery;

import org.jetbrains.annotations.NotNull;

import net.minecraft.util.Identifier;

public interface IValidatedRegistry<T> extends IRegistry<T> {
	
	public boolean validate(@NotNull Identifier identifier, @NotNull T value);
	
}
