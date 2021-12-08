package com.bb1.fabric.bfapi.config;

import com.bb1.fabric.bfapi.utils.Field;
import com.google.common.base.Function;
import com.google.gson.JsonElement;

import net.minecraft.util.Identifier;

public final class InlineConfigSerializer<O> extends AbstractConfigSerializable<O> {
	
	private final Function<O, JsonElement> setter;
	
	private final Function<JsonElement, O> getter;

	public InlineConfigSerializer(String id, Class<O> clazz, Function<O, JsonElement> setter, Function<JsonElement, O> getter) {
		super(new Identifier(id, clazz.getSimpleName().toLowerCase()+"_serializer"), clazz, true);
		this.setter = setter;
		this.getter = getter;
	}

	@Override
	public JsonElement serialize(Field<O> object) {
		return this.setter.apply(object.getObject());
	}

	@Override
	public O deserialize(Field<JsonElement> object) {
		return this.getter.apply(object.getObject());
	}

}
