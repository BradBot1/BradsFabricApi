package com.bb1.fabric.bfapi.registery;

import org.jetbrains.annotations.Nullable;

import net.minecraft.util.Identifier;

@FunctionalInterface
public interface IRegisterable {
	
	public void register(@Nullable Identifier name);
	
	public default void register() { register(null); }
	
}