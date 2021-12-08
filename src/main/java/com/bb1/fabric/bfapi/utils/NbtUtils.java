package com.bb1.fabric.bfapi.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.minecraft.nbt.AbstractNbtList;
import net.minecraft.nbt.AbstractNbtNumber;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtFloat;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtLong;
import net.minecraft.nbt.NbtNull;
import net.minecraft.nbt.NbtShort;
import net.minecraft.nbt.NbtString;

public final class NbtUtils {
	
	public static @NotNull NbtList putCollection(@Nullable NbtList nbt, @NotNull Collection<String> collection) {
		if (nbt==null) { return putCollection(new NbtList(), collection); }
		collection.forEach((s)->nbt.add(NbtString.of(s)));
		return nbt;
	}
	
	public static @NotNull Collection<String> getCollection(@NotNull NbtList nbt, @Nullable Collection<String> collection) {
		if (collection==null) { return getCollection(nbt, new ArrayList<String>()); }
		nbt.forEach((elem)->collection.add(elem.asString()));
		return collection;
	}
	
	@SuppressWarnings("unchecked")
	public static @NotNull JsonElement serialize(@Nullable NbtElement nbtElement) {
		if (nbtElement==null) return JsonNull.INSTANCE;
		byte type = nbtElement.getType();
		JsonObject jsonObject;
		switch (type) {
		case NbtElement.NULL_TYPE: return JsonNull.INSTANCE;
		case NbtElement.COMPOUND_TYPE:
			jsonObject = new JsonObject();
			NbtCompound nbtCompound = ((NbtCompound)nbtElement);
			for (String key : nbtCompound.getKeys()) {
				jsonObject.add(key, serialize(nbtCompound.get(key)));
			}
			return jsonObject;
		case NbtElement.STRING_TYPE: return new JsonPrimitive(((NbtString)nbtElement).asString());
		case NbtElement.NUMBER_TYPE: 
		case NbtElement.INT_TYPE: 
		case NbtElement.LONG_TYPE: 
		case NbtElement.FLOAT_TYPE: 
		case NbtElement.SHORT_TYPE: 
		case NbtElement.DOUBLE_TYPE: 
		case NbtElement.BYTE_TYPE: 
			return new JsonPrimitive(((AbstractNbtNumber)nbtElement).numberValue());
		case NbtElement.BYTE_ARRAY_TYPE: 
		case NbtElement.INT_ARRAY_TYPE: 
		case NbtElement.LONG_ARRAY_TYPE: 
		case NbtElement.LIST_TYPE: 
			JsonArray jsonArray = new JsonArray();
			AbstractNbtList<? extends NbtElement> list = (AbstractNbtList<? extends NbtElement>) nbtElement;
			for (NbtElement nbtElement2 : list) {
				jsonArray.add(serialize(nbtElement2));
			}
			return jsonArray;
		default:
			throw new IllegalArgumentException("Unexpected type: " + type);
		}
	}
	
	public static @NotNull NbtElement deserialize(@Nullable JsonElement jsonElement) {
		if (jsonElement==null) return NbtNull.INSTANCE;
		if (jsonElement.isJsonArray()) {
			NbtList nbtList = new NbtList();
			for (JsonElement jsonElement2 : jsonElement.getAsJsonArray()) {
				nbtList.add(deserialize(jsonElement2));
			}
			return nbtList;
		} else if (jsonElement.isJsonObject()) {
			NbtCompound nbtCompound = new NbtCompound();
			for (Entry<String, JsonElement> entry : jsonElement.getAsJsonObject().entrySet()) {
				nbtCompound.put(entry.getKey(), deserialize(entry.getValue()));
			}
			return nbtCompound;
		} else if (jsonElement.isJsonPrimitive()) {
			JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
			if (jsonPrimitive.isString()) {
				return NbtString.of(jsonPrimitive.getAsString());
			} else if (jsonPrimitive.isBoolean()) {
				return NbtByte.of(jsonPrimitive.getAsBoolean());
			} else if (jsonPrimitive.isNumber()) {
				Number number = jsonPrimitive.getAsNumber();
				if (number instanceof Integer) {
					return NbtInt.of(number.intValue());
				} else if (number instanceof Float) {
					return NbtFloat.of(number.floatValue());
				} else if (number instanceof Short) {
					return NbtShort.of(number.shortValue());
				} else if (number instanceof Long) {
					return NbtLong.of(number.longValue());
				} else if (number instanceof Double) {
					return NbtDouble.of(number.doubleValue());
				} else if (number instanceof Byte) {
					return NbtByte.of(number.byteValue());
				}
			}
		}
		return NbtNull.INSTANCE;
	}
	
}
