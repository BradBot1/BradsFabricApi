package com.bb1.fabric.bfapi.gamerules;

import static com.bb1.fabric.bfapi.registery.BFAPIRegistry.GAMERULES;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import com.bb1.fabric.bfapi.registery.IRegisterable;
import com.mojang.brigadier.arguments.ArgumentType;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameRules.Category;

public abstract class AbstractGamerule<T> implements IRegisterable {
	
	protected final String owner;
	
	protected final String name;
	
	protected final Category category;
	
	protected T value;
	
	protected AbstractGamerule(@NotNull String owningModId, @NotNull String name, @NotNull T defaultValue, Category category) {
		this(owningModId, name, defaultValue, category, true);
	}
	
	protected AbstractGamerule(@NotNull String owningModId, @NotNull String name, @NotNull T defaultValue, Category category, boolean autoRegister) {
		this.owner = owningModId;
		this.name = name;
		this.value = defaultValue;
		this.category = category;
		if (autoRegister) { register(getIdentifier()); }
	}
	
	public final String getName() { return this.name; }
	
	public final String getOwner() { return this.owner; }
	
	public final Identifier getIdentifier() { return new Identifier(owner, name.replaceAll("[^a-z0-9]", "")); }
	
	public final void setValue(T value) { this.value = value; }
	
	public final T getValue() { return this.value; }
	
	public final Category getCategory() { return this.category; }
	
	public abstract String serialize();
	
	public abstract void deserialize(String serializedData);
	
	public int toInt() { return Objects.hashCode(value); }
	
	public abstract T parse(String string);
	
	public abstract ArgumentType<?> getArgument();
	
	@Override
	public void register(Identifier identifier) {
		Registry.register(GAMERULES, identifier, this);
	}
	
}
