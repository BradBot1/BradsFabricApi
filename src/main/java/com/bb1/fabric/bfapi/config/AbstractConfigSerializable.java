package com.bb1.fabric.bfapi.config;

import static com.bb1.fabric.bfapi.registery.BFAPIRegistry.CONFIG_SERIALIZER;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.bb1.fabric.bfapi.registery.IRegisterable;
import com.bb1.fabric.bfapi.utils.Field;
import com.bb1.fabric.bfapi.utils.NbtUtils;
import com.google.gson.JsonElement;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.command.argument.NbtElementArgumentType;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
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
	
	public LiteralArgumentBuilder<ServerCommandSource> addSetter(LiteralArgumentBuilder<ServerCommandSource> partialCommand, Object caller, java.lang.reflect.Field field, String fieldName) {
		
		// MAYBE: pull from InlineConfigSerializer#buildSetterFor()? Would remove repeated code but at a loss of performance
		
		field.setAccessible(true);
		partialCommand.then(CommandManager.argument("value", NbtElementArgumentType.nbtElement()).executes((s)->{
			try {
				field.set(caller, deserialize(Field.of(NbtUtils.serialize(s.getArgument("value", NbtElement.class)))));
				return 1;
			} catch (Throwable t) {
				s.getSource().sendError(new LiteralText("Field to set the value"));
				return 0;
			}
		}));
		return partialCommand;
	}
	
	@Override
	public void register(@Nullable Identifier name) {
		Registry.register(CONFIG_SERIALIZER, name==null?this.identifier:name, this);
	}
	
}
