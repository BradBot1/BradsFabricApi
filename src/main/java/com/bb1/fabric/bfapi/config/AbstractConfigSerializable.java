package com.bb1.fabric.bfapi.config;

import static com.bb1.fabric.bfapi.registery.BFAPIRegistry.CONFIG_SERIALIZER;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.bb1.fabric.bfapi.registery.IRegisterable;
import com.bb1.fabric.bfapi.utils.Field;
import com.google.gson.JsonElement;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public abstract class AbstractConfigSerializable<O> implements IRegisterable {
	
	protected final Identifier identifier;
	protected final Class<O> clazz;
	
	protected AbstractConfigSerializable(@NotNull Identifier identifier, Class<O> clazz, boolean autoRegister) {
		this.identifier = identifier;
		this.clazz = clazz;
		if (autoRegister) { register(null); }
	}
	
	public final Class<O> getSerializableClass() { return this.clazz; }
	
	public final Identifier getIdentifier() { return this.identifier; }
	
	public abstract JsonElement serialize(Field<O> object);
	
	public abstract O deserialize(Field<JsonElement> object);
	
	@Override
	public void register(@Nullable Identifier name) {
		Registry.register(CONFIG_SERIALIZER, name==null?this.identifier:name, this);
	}
	
}
