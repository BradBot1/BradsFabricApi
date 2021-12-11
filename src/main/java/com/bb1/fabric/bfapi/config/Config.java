package com.bb1.fabric.bfapi.config;

import static com.bb1.fabric.bfapi.Constants.ID;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.UUID;

import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.bb1.fabric.bfapi.Constants;
import com.bb1.fabric.bfapi.permissions.Permission;
import com.bb1.fabric.bfapi.permissions.PermissionLevel;
import com.bb1.fabric.bfapi.registery.BFAPIRegistry;
import com.bb1.fabric.bfapi.utils.ExceptionWrapper;
import com.bb1.fabric.bfapi.utils.ExceptionWrapper.ExceptionWrapperWithoutReturn;
import com.bb1.fabric.bfapi.utils.Inputs.DualInput;
import com.bb1.fabric.bfapi.utils.Inputs.TriInput;
import com.bb1.fabric.bfapi.utils.NbtUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;

public class Config {
	
	private static boolean LOADED = false;
	
	public static synchronized final void init() {
		if (LOADED) { return; }
		// api stuff
		new InlineConfigSerializer<Permission>(ID, Permission.class, (n)->{
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("node", n.node());
			jsonObject.addProperty("level", n.level().toString());
			return jsonObject;
		}, (js)->new Permission(js.getAsJsonObject().get("node").getAsString(), PermissionLevel.fromString(js.getAsJsonObject().get("level").getAsString())));
		new InlineConfigSerializer<PermissionLevel>(ID, PermissionLevel.class, (n)->new JsonPrimitive(n.toString()), (js)->PermissionLevel.fromString(js.getAsString()));
		// java
		new InlineConfigSerializer<String>("java", String.class, (n)->new JsonPrimitive(n), (js)->js.getAsString());
		new InlineConfigSerializer<Character>("java", Character.class, (n)->new JsonPrimitive(n), (js)->js.getAsString().toCharArray()[0]);
		new InlineConfigSerializer<Number>("java", Number.class, (n)->new JsonPrimitive(n), (js)->js.getAsNumber());
		new InlineConfigSerializer<Boolean>("java", Boolean.class, (n)->new JsonPrimitive(n), (js)->js.getAsBoolean());
		new InlineConfigSerializer<UUID>("java", UUID.class, (n)->new JsonPrimitive(n.toString()), (js)->UUID.fromString(js.getAsString()));
		// minecraft
		new InlineConfigSerializer<Text>("minecraft", Text.class, (n)->Text.Serializer.toJsonTree(n), (js)->Text.Serializer.fromJson(js));
		new InlineConfigSerializer<Identifier>("minecraft", Identifier.class, (n)->new JsonPrimitive(n.toString()), (js)->new Identifier(js.getAsString()));
		new InlineConfigSerializer<NbtElement>("minecraft", NbtElement.class, (n)->NbtUtils.serialize(n), (js)->NbtUtils.deserialize(js));
		new InlineConfigSerializer<ItemStack>("minecraft", ItemStack.class, (n)->NbtUtils.serialize(n.writeNbt(new NbtCompound())), (js)->ItemStack.fromNbt((NbtCompound)NbtUtils.deserialize(js)));
		// gson
		new InlineConfigSerializer<JsonObject>("gson", JsonObject.class, (n)->n, (js)->js.getAsJsonObject());
		new InlineConfigSerializer<JsonArray>("gson", JsonArray.class, (n)->n, (js)->js.getAsJsonArray());
		new InlineConfigSerializer<JsonPrimitive>("gson", JsonPrimitive.class, (n)->n, (js)->js.getAsJsonPrimitive());
		new InlineConfigSerializer<JsonElement>("gson", JsonElement.class, (n)->n, (js)->js);
		LOADED = true;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> @Nullable AbstractConfigSerializable<T> getSerializer(Class<T> wantedClazz, boolean allowSupers) {
		switch (wantedClazz.getName()) { // since we know these values we may aswell switch over them for a little bit of performance (it also allows for primitives lol)
			case "int":
			case "java.lang.Integer":
			case "short":
			case "java.lang.Short":
			case "long":
			case "java.lang.Long":
			case "double":
			case "java.lang.Double":
			case "float":
			case "java.lang.Float":
			case "byte":
			case "java.lang.Byte":
				return  (AbstractConfigSerializable<T>) BFAPIRegistry.CONFIG_SERIALIZER.get(new Identifier("java:number_serializer"));
			case "String":
				return  (AbstractConfigSerializable<T>) BFAPIRegistry.CONFIG_SERIALIZER.get(new Identifier("java:string_serializer"));
			case "char":
			case "java.lang.Character":
				return  (AbstractConfigSerializable<T>) BFAPIRegistry.CONFIG_SERIALIZER.get(new Identifier("java:character_serializer"));
			case "boolean":
			case "java.lang.Boolean":
				return  (AbstractConfigSerializable<T>) BFAPIRegistry.CONFIG_SERIALIZER.get(new Identifier("java:boolean_serializer"));
		}
		AbstractConfigSerializable<T> bestSerializer = null;
		for (Entry<RegistryKey<AbstractConfigSerializable<?>>, AbstractConfigSerializable<?>> entry : BFAPIRegistry.CONFIG_SERIALIZER.getEntries()) {
			AbstractConfigSerializable<?> ser = entry.getValue();
			final Class<?> clazz = ser.getSerializableClass();
			if (wantedClazz.equals(clazz)) {
				// Best possible serializer found
				bestSerializer = (AbstractConfigSerializable<T>) ser;
				break;
			} else if (allowSupers && clazz.isAssignableFrom(wantedClazz)) {
				if (bestSerializer==null) {
					bestSerializer = (AbstractConfigSerializable<T>) ser;
				} else {
					if (!clazz.isAssignableFrom(bestSerializer.getSerializableClass())) {
						// We have a higher level serializer
						bestSerializer = (AbstractConfigSerializable<T>) ser;
					}
				}
			}
		}
		return bestSerializer;
	}
	
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	public static final String CONFIG_DIRECTORY = FabricLoader.getInstance().getConfigDir().toFile().getAbsolutePath()+File.separatorChar;
	
	private static final Logger LOGGER = Constants.createSubLogger("Configs");
	
	static final ExceptionWrapper<Void, Method> SERIALIZER_GRABBER = (i) -> AbstractConfigSerializable.class.getDeclaredMethod("serialize", com.bb1.fabric.bfapi.utils.Field.class);
	
	static final ExceptionWrapper<Void, Method> DESERIALIZER_GRABBER = (i) -> AbstractConfigSerializable.class.getDeclaredMethod("deserialize", com.bb1.fabric.bfapi.utils.Field.class);
	
	static final ExceptionWrapper<TriInput<Method, AbstractConfigSerializable<?>, com.bb1.fabric.bfapi.utils.Field<?>>, JsonElement> SERIALIZER_INVOKER = (i) -> (JsonElement)i.get().invoke(i.getSecond(), i.getThird());
	
	static final ExceptionWrapper<TriInput<Method, AbstractConfigSerializable<?>, com.bb1.fabric.bfapi.utils.Field<JsonElement>>, Object> DESERIALIZER_INVOKER = (i) -> i.get().invoke(i.getSecond(), i.getThird());
	
	static final ExceptionWrapperWithoutReturn<TriInput<Field, Object, Object>> DESERIALIZER_SETTER = (i) -> i.get().set(i.getSecond(), i.getThird());
	
	protected final Identifier identifier;
	
	protected Config(@NotNull Identifier identifier) {
		this.identifier = identifier;
		init(); // just in case
	}
	
	public void save() {
		JsonObject jsonObject = new JsonObject();
		final Method method = ExceptionWrapper.executeWithReturn(null, SERIALIZER_GRABBER);
		method.setAccessible(true);
		for (Field field : getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(ConfigIgnore.class) || field.getDeclaringClass().isAnnotationPresent(ConfigIgnore.class) || Modifier.isStatic(field.getModifiers())|| Modifier.isFinal(field.getModifiers())) { continue; }
			final Object instance = ExceptionWrapper.executeWithReturn(DualInput.of(field, this), (i)->i.get().get(i.getSecond()));
			if (instance==null) {
				LOGGER.warn("Failed to save the field "+field.getName()+" of "+this.identifier.toString());
				continue;
			}
			final AbstractConfigSerializable<?> ser = getSerializer(instance.getClass(), true);
			if (ser==null) {
				LOGGER.warn("Failed to save the field "+field.getName()+" of "+this.identifier.toString()+"! There is not a registered serializer for this type");
				continue;
			}
			JsonObject current = jsonObject;
			if (field.isAnnotationPresent(ConfigSub.class)) {
				final ConfigSub sub = field.getAnnotation(ConfigSub.class);
				String[] path = sub.subOf().split("\\.");
				JsonObject prev = jsonObject;
				for (String str : path) {
					current = prev.has(str) ? prev.get(str).getAsJsonObject() : new JsonObject();
					prev.add(str, current);
					prev = current;
				}
			}
			if (field.isAnnotationPresent(ConfigComment.class)) {
				final ConfigComment comment = field.getAnnotation(ConfigComment.class);
				current.addProperty(comment.prefix()+field.getName(), comment.contents());
			}
			current.add(field.isAnnotationPresent(ConfigName.class) ? field.getAnnotation(ConfigName.class).name() : field.getName(), ExceptionWrapper.executeWithReturn(TriInput.of(method, ser, com.bb1.fabric.bfapi.utils.Field.of(instance)), SERIALIZER_INVOKER));
		}
		try {
			new File(CONFIG_DIRECTORY+this.identifier.getNamespace()+File.separatorChar).mkdirs();
			File file = new File(CONFIG_DIRECTORY+this.identifier.getNamespace()+File.separatorChar+this.identifier.getPath()+".json");
			if (!file.exists()) file.createNewFile();
			BufferedWriter b = new BufferedWriter(new PrintWriter(file));
			b.write(GSON.toJson(jsonObject));
			b.flush();
			b.close();
			LOGGER.info("Saved "+this.identifier.toString());
		} catch (IOException e) {
			LOGGER.warn("Failed to save "+this.identifier.toString());
		}
	}
	
	public void load() {
		File file = new File(CONFIG_DIRECTORY+this.identifier.getNamespace()+File.separatorChar+this.identifier.getPath()+".json");
		if (!file.exists()) return; // Can't load anything if there is file
		ArrayList<String> r = new ArrayList<String>();
		try {
			// Attempt to read the config's contents
			Scanner s = new Scanner(file);
			while (s.hasNext()) {
		    	r.add(s.nextLine());
			}
			s.close();
		} catch (IOException e) {
			LOGGER.warn("Failed to load config due to an IOException");
			return; // Not readable
		}
		JsonElement contents = JsonParser.parseString(String.join("", r));
		if (!contents.isJsonObject()) {
			LOGGER.warn("Failed to load config due to the contents not being a JsonObject");
			return;
		}
		JsonObject jsonObject = contents.getAsJsonObject();
		final Method method = ExceptionWrapper.executeWithReturn(null, DESERIALIZER_GRABBER);
		method.setAccessible(true);
		for (Field field : getClass().getFields()) {
			if (field.isAnnotationPresent(ConfigIgnore.class) || field.getDeclaringClass().isAnnotationPresent(ConfigIgnore.class) || Modifier.isStatic(field.getModifiers())|| Modifier.isFinal(field.getModifiers())) { continue; }
			JsonObject current = jsonObject;
			if (field.isAnnotationPresent(ConfigSub.class)) {
				final ConfigSub sub = field.getAnnotation(ConfigSub.class);
				String[] path = sub.subOf().split("\\.");
				JsonObject prev = jsonObject;
				for (String str : path) {
					current = prev.has(str) ? prev.get(str).getAsJsonObject() : new JsonObject();
					prev.add(str, current);
					prev = current;
				}
			}
			JsonElement instance = current.get(field.isAnnotationPresent(ConfigName.class) ? field.getAnnotation(ConfigName.class).name() : field.getName());
			if (instance==null) {
				LOGGER.warn("Failed to save the field "+field.getName()+" of "+this.identifier.toString());
				continue;
			}
			AbstractConfigSerializable<?> ser = getSerializer(field.getType(), true);
			if (ser==null) {
				LOGGER.warn("Failed to save the field "+field.getName()+" of "+this.identifier.toString()+"! There is not a registered serializer for this type");
				continue;
			}
			ExceptionWrapper.execute(TriInput.of(field, this, ExceptionWrapper.executeWithReturn(TriInput.of(method, ser, com.bb1.fabric.bfapi.utils.Field.of(instance)), DESERIALIZER_INVOKER)), DESERIALIZER_SETTER);
		}
	}
	
}
