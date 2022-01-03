package com.bb1.fabric.bfapi.config;

import org.jetbrains.annotations.Nullable;

import com.bb1.fabric.bfapi.utils.ExceptionWrapper;
import com.bb1.fabric.bfapi.utils.Field;
import com.bb1.fabric.bfapi.utils.Inputs.QuadInput;
import com.bb1.fabric.bfapi.utils.Inputs.TriInput;
import com.google.common.base.Function;
import com.google.gson.JsonElement;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.command.argument.NbtElementArgumentType;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

public final class InlineConfigSerializer<O> extends AbstractConfigSerializable<O> {
	
	private final Function<O, JsonElement> setter;
	
	private final Function<JsonElement, O> getter;
	
	public static final <T> Function<QuadInput<LiteralArgumentBuilder<ServerCommandSource>, Object, java.lang.reflect.Field, String>, LiteralArgumentBuilder<ServerCommandSource>> buildSetterFor(ArgumentType<T> argType, Class<T> classType, @Nullable Function<T, Object> beforeSet) {
		return (i)->{
			i.getThird().setAccessible(true);
			return i.get().then(CommandManager.argument("valueToSet", argType).executes((c)->{
				ExceptionWrapper.execute(TriInput.of(i.getThird(), i.getSecond(), beforeSet==null?c.getArgument("valueToSet", classType):beforeSet.apply(c.getArgument("valueToSet", classType))), Config.DESERIALIZER_SETTER);
				return 1;
			}));
		};
	}
	
	private final Function<QuadInput<LiteralArgumentBuilder<ServerCommandSource>, Object, java.lang.reflect.Field, String>, LiteralArgumentBuilder<ServerCommandSource>> command;

	public InlineConfigSerializer(String id, Class<O> clazz, Function<O, JsonElement> setter, Function<JsonElement, O> getter) {
		this(id, clazz, setter, getter, NbtElementArgumentType.nbtElement(), NbtElement.class);
	}

	public <T> InlineConfigSerializer(String id, Class<O> clazz, Function<O, JsonElement> setter, Function<JsonElement, O> getter, ArgumentType<T> argType, Class<T> classType) {
		this(id, clazz, setter, getter, argType, classType, null);
	}
	
	public <T> InlineConfigSerializer(String id, Class<O> clazz, Function<O, JsonElement> setter, Function<JsonElement, O> getter, ArgumentType<T> argType, Class<T> classType, Function<T, Object> beforeSet) {
		this(id, clazz, setter, getter, buildSetterFor(argType, classType, beforeSet));
	}

	public InlineConfigSerializer(String id, Class<O> clazz, Function<O, JsonElement> setter, Function<JsonElement, O> getter, @Nullable Function<QuadInput<LiteralArgumentBuilder<ServerCommandSource>, Object, java.lang.reflect.Field, String>, LiteralArgumentBuilder<ServerCommandSource>> command) {
		super(new Identifier(id, clazz.getSimpleName().toLowerCase()+"_serializer"), clazz, true);
		this.setter = setter;
		this.getter = getter;
		this.command = command;
	}

	@Override
	public JsonElement serialize(Field<O> object) {
		return this.setter.apply(object.getObject());
	}

	@Override
	public O deserialize(Field<JsonElement> object) {
		return this.getter.apply(object.getObject());
	}

	@Override
	public LiteralArgumentBuilder<ServerCommandSource> addSetter(LiteralArgumentBuilder<ServerCommandSource> partialCommand, Object caller, java.lang.reflect.Field field, String fieldName) {
		return this.command==null ? super.addSetter(partialCommand, caller, field, fieldName) : this.command.apply(QuadInput.of(partialCommand, caller, field, fieldName));
	}

}
