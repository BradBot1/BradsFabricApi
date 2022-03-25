package com.bb1.fabric.bfapi.permissions;

import static com.bb1.fabric.bfapi.registery.BFAPIRegistry.PERMISSIONS;

import org.jetbrains.annotations.Nullable;

import com.bb1.fabric.bfapi.registery.IRegisterable;

import net.minecraft.util.Identifier;

public record Permission(String node, PermissionLevel level) implements IRegisterable {

	@Override
	public void register(@Nullable Identifier name) {
		PERMISSIONS.add(name, this);
	}
	
}
