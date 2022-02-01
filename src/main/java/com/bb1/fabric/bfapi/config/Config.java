package com.bb1.fabric.bfapi.config;

import static com.bb1.fabric.bfapi.Constants.GSON;
import static com.bb1.fabric.bfapi.Constants.ID;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.UUID;

import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.bb1.fabric.bfapi.Constants;
import com.bb1.fabric.bfapi.GameObjects;
import com.bb1.fabric.bfapi.permissions.Permission;
import com.bb1.fabric.bfapi.permissions.PermissionLevel;
import com.bb1.fabric.bfapi.recipe.AbstractRecipe;
import com.bb1.fabric.bfapi.recipe.IRecipeRequirement;
import com.bb1.fabric.bfapi.recipe.IRecipeResult;
import com.bb1.fabric.bfapi.registery.BFAPIRegistry;
import com.bb1.fabric.bfapi.utils.ExceptionWrapper;
import com.bb1.fabric.bfapi.utils.ExceptionWrapper.ExceptionWrapperWithoutReturn;
import com.bb1.fabric.bfapi.utils.Inputs.DualInput;
import com.bb1.fabric.bfapi.utils.Inputs.Input;
import com.bb1.fabric.bfapi.utils.Inputs.QuintInput;
import com.bb1.fabric.bfapi.utils.Inputs.TriInput;
import com.bb1.fabric.bfapi.utils.NbtUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.command.argument.ItemStackArgument;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.command.argument.TextArgumentType;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;

public class Config {
	
	private static boolean LOADED = false;
	
	@SuppressWarnings("deprecation")
	public static synchronized final void init() {
		if (LOADED) { return; }
		// api stuff
		new InlineConfigSerializer<Permission>(ID, Permission.class, (n)->{
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("node", n.node());
			jsonObject.addProperty("level", n.level().toString());
			return jsonObject;
		}, (js)->new Permission(js.getAsJsonObject().get("node").getAsString(), PermissionLevel.fromString(js.getAsJsonObject().get("level").getAsString())));
		new InlineConfigSerializer<PermissionLevel>(ID, PermissionLevel.class, (n)->new JsonPrimitive(n.toString()), (js)->PermissionLevel.fromString(js.getAsString()), (i)->{
			LiteralArgumentBuilder<ServerCommandSource> cmd = i.get();
			for (final PermissionLevel lvl : PermissionLevel.values()) {
				cmd.then(CommandManager.literal(lvl.toString()).executes(c->{
					ExceptionWrapper.execute(TriInput.of(i.getThird(), i.getSecond(), lvl), Config.DESERIALIZER_SETTER);
					return 1;
				}));
			}
			return cmd;
		});
		new InlineConfigSerializer<AbstractRecipe>(ID, AbstractRecipe.class, (n)->{
			final JsonObject js = new JsonObject();
			js.addProperty("type", n.getType().toString());
			if (n.getGroup()!=null) { js.addProperty("group", n.getGroup()); }
			final AbstractConfigSerializable<ItemStack> isS = getSerializer(ItemStack.class, false);
			js.add("icon", isS.serialize(com.bb1.fabric.bfapi.utils.Field.of(n.getResultIcon())));
			JsonObject requirements = new JsonObject();
			for (IRecipeRequirement req : n.getRequirements()) {
				requirements = req.addToObject(requirements);
			}
			js.add("requirements", requirements);
			JsonObject results = new JsonObject();
			for (IRecipeResult result : n.getResults()) {
				results = result.addToObject(results);
			}
			js.add("results", results);
			return n.serialize(js);
		}, (js2)->{
			final JsonObject js = js2.getAsJsonObject();
			final Identifier type = new Identifier(js.get("type").getAsString());
			final @Nullable String group = js.get("group").getAsString();
			final AbstractConfigSerializable<ItemStack> isS = getSerializer(ItemStack.class, false);
			final ItemStack icon = isS.deserialize(com.bb1.fabric.bfapi.utils.Field.of(js.get("icon")));
			JsonObject obj = js.get("requirements").getAsJsonObject();
			final IRecipeRequirement[] requirements = new IRecipeRequirement[obj.entrySet().size()];
			int index = 0;
			for (Entry<String, JsonElement> elem : obj.entrySet()) {
				requirements[index++] = AbstractRecipe.buildRequirement(elem.getKey(), elem.getValue());
			}
			obj = js.get("results").getAsJsonObject();
			final IRecipeResult[] results = new IRecipeResult[obj.entrySet().size()];
			index = 0;
			for (Entry<String, JsonElement> elem : obj.entrySet()) {
				results[index++] = AbstractRecipe.buildResult(elem.getKey(), elem.getValue());
			}
			return AbstractRecipe.buildRecipe(type, QuintInput.of(group, icon, requirements, results, js));
		}); // IMP: add command support stuff
		// java
		new InlineConfigSerializer<String>("java", String.class, (n)->new JsonPrimitive(n), (js)->js.getAsString(), StringArgumentType.greedyString(), String.class);
		new InlineConfigSerializer<Character>("java", Character.class, (n)->new JsonPrimitive(n), (js)->js.getAsString().toCharArray()[0], StringArgumentType.greedyString(), String.class, (s)->s.charAt(0));
		new InlineConfigSerializer<Number>("java", Number.class, (n)->new JsonPrimitive(n), (js)->js.getAsNumber(), DoubleArgumentType.doubleArg(), Double.class);
		new InlineConfigSerializer<Boolean>("java", Boolean.class, (n)->new JsonPrimitive(n), (js)->js.getAsBoolean(), (i)->{
			return i.get().then(CommandManager.literal("true").executes(c->{
				ExceptionWrapper.execute(TriInput.of(i.getThird(), i.getSecond(), true), Config.DESERIALIZER_SETTER);
				return 1;
			})).then(CommandManager.literal("false").executes(c->{
				ExceptionWrapper.execute(TriInput.of(i.getThird(), i.getSecond(), false), Config.DESERIALIZER_SETTER);
				return 1;
			}));
		});
		new InlineConfigSerializer<UUID>("java", UUID.class, (n)->new JsonPrimitive(n.toString()), (js)->UUID.fromString(js.getAsString()), UuidArgumentType.uuid(), UUID.class);
		new InlineConfigSerializer<Record>("java", Record.class, (n)->{
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("comment-record_type", "DO NOT CHANGE THIS! This is used to identify what record to reflect upon, changing it or removing it will result in a crash/error");
			jsonObject.addProperty("-record_type", n.getClass().getName()); // used to identify what 
			final Method method = ExceptionWrapper.executeWithReturn(null, SERIALIZER_GRABBER);
			method.setAccessible(true);
			for (Field field : n.getClass().getFields()) {
				if (field.isAnnotationPresent(ConfigIgnore.class) || field.getDeclaringClass().isAnnotationPresent(ConfigIgnore.class) || Modifier.isStatic(field.getModifiers())|| Modifier.isFinal(field.getModifiers())) { continue; }
				final Object instance = ExceptionWrapper.executeWithReturn(DualInput.of(field, n), (i)->i.get().get(i.getSecond()));
				if (instance==null) { continue; }
				final AbstractConfigSerializable<?> ser = getSerializer(field.getType(), true);
				if (ser==null) { continue; }
				JsonObject current = jsonObject;
				if (field.isAnnotationPresent(ConfigSub.class)) {
					final ConfigSub sub = field.getAnnotation(ConfigSub.class);
					String[] path = sub.value().equals("") ? sub.subOf().split("\\.") : sub.value().split("\\.");
					JsonObject prev = jsonObject;
					for (String str : path) {
						current = prev.has(str) ? prev.get(str).getAsJsonObject() : new JsonObject();
						prev.add(str, current);
						prev = current;
					}
				}
				if (field.isAnnotationPresent(ConfigComment.class)) {
					final ConfigComment comment = field.getAnnotation(ConfigComment.class);
					current.addProperty(comment.prefix()+field.getName(), comment.value().equals("")?comment.contents():comment.value());
				}
				String name = field.getName();
				if (field.isAnnotationPresent(ConfigName.class)) {
					ConfigName nme = field.getAnnotation(ConfigName.class);
					name = nme.value().equals("") ? nme.name() : nme.value();
				}
				current.add(name, ExceptionWrapper.executeWithReturn(TriInput.of(method, ser, com.bb1.fabric.bfapi.utils.Field.of(instance)), SERIALIZER_INVOKER));
			}
			return jsonObject;
		}, (js)->{
			final JsonObject jsonObject = js.getAsJsonObject();
			final Class<?> clazz = ExceptionWrapper.executeWithReturn(Input.of(jsonObject), (jsObjIn)->Class.forName(jsObjIn.get().get("-record_type").getAsString()));
			List<Class<?>> params = new ArrayList<Class<?>>();
			for (Field field : clazz.getFields()) { params.add(field.getDeclaringClass()); }
			return (Record) ExceptionWrapper.executeWithReturn(TriInput.of(clazz, params, jsonObject), (i) -> {
				Constructor<?> constructor = i.get().getConstructor(i.getSecond().toArray(new Class<?>[i.getSecond().size()]));
				constructor.setAccessible(true);
				final Method method = ExceptionWrapper.executeWithReturn(null, DESERIALIZER_GRABBER);
				method.setAccessible(true);
				Object[] objs = new Object[i.getSecond().size()];
				int index = 0;
				for (Field field : i.get().getFields()) {
					if (field.isAnnotationPresent(ConfigIgnore.class) || field.getDeclaringClass().isAnnotationPresent(ConfigIgnore.class) || Modifier.isStatic(field.getModifiers())|| Modifier.isFinal(field.getModifiers())) { continue; }
					JsonObject current = jsonObject;
					if (field.isAnnotationPresent(ConfigSub.class)) {
						final ConfigSub sub = field.getAnnotation(ConfigSub.class);
						String[] path = sub.value().equals("") ? sub.subOf().split("\\.") : sub.value().split("\\.");
						JsonObject prev = jsonObject;
						for (String str : path) {
							current = prev.has(str) ? prev.get(str).getAsJsonObject() : new JsonObject();
							prev.add(str, current);
							prev = current;
						}
					}
					String name = field.getName();
					if (field.isAnnotationPresent(ConfigName.class)) {
						ConfigName nme = field.getAnnotation(ConfigName.class);
						name = nme.value().equals("") ? nme.name() : nme.value();
					}
					JsonElement instance = current.get(name);
					if (instance==null) {
						LOGGER.fatal("Unable to instantiate record as a field could not be found!");
						throw new NullPointerException("Instance cannot be null!");
					}
					AbstractConfigSerializable<?> ser = getSerializer(field.getType(), true);
					if (ser==null) {
						LOGGER.fatal("Unable to instantiate record as a field's serializer could not be found!");
						throw new NullPointerException("Serializer cannot be null!");
					}
					objs[index++] = ser.deserialize(com.bb1.fabric.bfapi.utils.Field.of(instance));
				}
				return constructor.newInstance(objs);
			});
		});
		// minecraft
		new InlineConfigSerializer<Text>("minecraft", Text.class, (n)->Text.Serializer.toJsonTree(n), (js)->Text.Serializer.fromJson(js), TextArgumentType.text(), Text.class);
		new InlineConfigSerializer<Identifier>("minecraft", Identifier.class, (n)->new JsonPrimitive(n.toString()), (js)->new Identifier(js.getAsString()), IdentifierArgumentType.identifier(), Identifier.class);
		new InlineConfigSerializer<NbtElement>("minecraft", NbtElement.class, (n)->NbtUtils.serialize(n), (js)->NbtUtils.deserialize(js));
		new InlineConfigSerializer<ItemStack>("minecraft", ItemStack.class, (n)->NbtUtils.serialize(n.writeNbt(new NbtCompound())), (js)->ItemStack.fromNbt((NbtCompound)NbtUtils.deserialize(js)), ItemStackArgumentType.itemStack(), ItemStackArgument.class, (isa)->ExceptionWrapper.executeWithReturn((v)->isa.createStack(1, true)));
		// gson
		new InlineConfigSerializer<JsonObject>("gson", JsonObject.class, (n)->n, (js)->js.getAsJsonObject());
		new InlineConfigSerializer<JsonArray>("gson", JsonArray.class, (n)->n, (js)->js.getAsJsonArray());
		new InlineConfigSerializer<JsonPrimitive>("gson", JsonPrimitive.class, (n)->n, (js)->js.getAsJsonPrimitive());
		new InlineConfigSerializer<JsonElement>("gson", JsonElement.class, (n)->n, (js)->js);
		LOADED = true;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> @Nullable AbstractConfigSerializable<T> getSerializer(Class<T> wantedClazz, boolean allowSupers) {
		if (wantedClazz.isArray()) {
			final AbstractConfigSerializable<?> serializer = getSerializer(wantedClazz.getComponentType(), allowSupers);
			if (serializer==null) { return null; }
			return new AbstractConfigSerializable<T>(new Identifier("null", "array_serializer"), wantedClazz, false) {

				@Override
				public JsonElement serialize(com.bb1.fabric.bfapi.utils.Field<T> object) {
					final Method method = ExceptionWrapper.executeWithReturn(null, SERIALIZER_GRABBER);
					method.setAccessible(true);
					JsonArray array = new JsonArray();
					for (Object obj : (Object[]) object.getObject()) {
						array.add(ExceptionWrapper.executeWithReturn(TriInput.of(method, serializer, com.bb1.fabric.bfapi.utils.Field.of(obj)), SERIALIZER_INVOKER));
					}
					return array;
				}

				@Override
				public T deserialize(com.bb1.fabric.bfapi.utils.Field<JsonElement> object) {
					final Method method = ExceptionWrapper.executeWithReturn(null, DESERIALIZER_GRABBER);
					method.setAccessible(true);
					JsonArray jsonArray = object.get();
					T array = (T) Array.newInstance(wantedClazz.getComponentType(), jsonArray.size());
					int index = 0;
					for (JsonElement obj : jsonArray) {
						Array.set(array, index++, ExceptionWrapper.executeWithReturn(TriInput.of(method, serializer, com.bb1.fabric.bfapi.utils.Field.of(obj)), DESERIALIZER_INVOKER));
					}
					return array;
				}
				
				@Override
				public void register(@Nullable Identifier name) { }
				
			};
		}
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
		if (bestSerializer==null) {
			LOGGER.warn("There is no provided serializer for "+wantedClazz.getName());
		}
		return bestSerializer;
	}
	
	public static final String CONFIG_DIRECTORY = FabricLoader.getInstance().getConfigDir().toFile().getAbsolutePath()+File.separatorChar;
	
	private static final Logger LOGGER = Constants.createSubLogger("Configs");
	
	static final ExceptionWrapper<Void, Method> SERIALIZER_GRABBER = (i) -> AbstractConfigSerializable.class.getDeclaredMethod("serialize", com.bb1.fabric.bfapi.utils.Field.class);
	
	static final ExceptionWrapper<Void, Method> DESERIALIZER_GRABBER = (i) -> AbstractConfigSerializable.class.getDeclaredMethod("deserialize", com.bb1.fabric.bfapi.utils.Field.class);
	
	static final ExceptionWrapper<TriInput<Method, AbstractConfigSerializable<?>, com.bb1.fabric.bfapi.utils.Field<?>>, JsonElement> SERIALIZER_INVOKER = (i) -> (JsonElement)i.get().invoke(i.getSecond(), i.getThird());
	
	static final ExceptionWrapper<TriInput<Method, AbstractConfigSerializable<?>, com.bb1.fabric.bfapi.utils.Field<JsonElement>>, Object> DESERIALIZER_INVOKER = (i) -> i.get().invoke(i.getSecond(), i.getThird());
	
	static final ExceptionWrapperWithoutReturn<TriInput<Field, Object, Object>> DESERIALIZER_SETTER = (i) -> i.get().set(i.getSecond(), i.getThird());
	
	protected final Identifier identifier;
	
	protected Config(@NotNull Identifier identifier) {
		this(identifier, true);
	}
	
	protected Config(@NotNull Identifier identifier, boolean buildCommand) {
		this.identifier = identifier;
		init(); // just in case
		if (buildCommand) {
			final CommandManager cmdManager = GameObjects.getCommandManager();
			if (cmdManager!=null) {
				buildCommand(cmdManager.getDispatcher());
				return;
			}
			GameObjects.GameEvents.COMMAND_REGISTRATION.addHandler((e)->{
				buildCommand(e.get());
			});
		}
	}
	
	@SuppressWarnings("deprecation")
	private final void buildCommand(@NotNull CommandDispatcher<ServerCommandSource> dispatcher) {
		CommandNode<ServerCommandSource> cmd = dispatcher.getRoot().getChild("config");
		if (cmd==null) {
			cmd = dispatcher.register(CommandManager.literal("config").requires((s)->s.getEntity()==null||s.hasPermissionLevel(4)));
		}
		CommandNode<ServerCommandSource> subCmd = cmd.getChild(this.identifier.getNamespace());
		if (subCmd==null) {
			subCmd = CommandManager.literal(this.identifier.getNamespace()).build();
			cmd.addChild(subCmd);
		}
		CommandNode<ServerCommandSource> subSubCmd = CommandManager.literal(this.identifier.getPath()).build();
		for (Field field : getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(ConfigIgnore.class) || field.getDeclaringClass().isAnnotationPresent(ConfigIgnore.class) || Modifier.isStatic(field.getModifiers())|| Modifier.isFinal(field.getModifiers())) { continue; }
			String name = field.getName();
			if (field.isAnnotationPresent(ConfigName.class)) {
				ConfigName nme = field.getAnnotation(ConfigName.class);
				name = nme.value().equals("") ? nme.name() : nme.value();
			}
			final String fieldName = name;
			final LiteralArgumentBuilder<ServerCommandSource> innerCmd = CommandManager.literal(fieldName);
			innerCmd.then(CommandManager.literal("get")
					.executes((cs)->{
						final Method method = ExceptionWrapper.executeWithReturn(null, SERIALIZER_GRABBER);
						method.setAccessible(true);
						cs.getSource().sendFeedback(new LiteralText(fieldName + " is set to " + ExceptionWrapper.executeWithReturn(TriInput.of(method, getSerializer(field.getType(), true), com.bb1.fabric.bfapi.utils.Field.of(ExceptionWrapper.executeWithReturn(DualInput.of(field, this), (i)->i.get().get(i.getSecond())))), SERIALIZER_INVOKER)), true);
						return 1;
					})
				);
			var setter = CommandManager.literal("set");
			getSerializer(field.getType(), true).addSetter(setter, this, field, fieldName);
			innerCmd.then(setter);
			subSubCmd.addChild(innerCmd.build());
		}
		subCmd.addChild(subSubCmd);
	}
	
	@SuppressWarnings("deprecation")
	public void save() {
		JsonObject jsonObject = new JsonObject();
		final Method method = ExceptionWrapper.executeWithReturn(null, SERIALIZER_GRABBER);
		method.setAccessible(true);
		for (Field field : getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(ConfigIgnore.class) || field.getDeclaringClass().isAnnotationPresent(ConfigIgnore.class) || Modifier.isStatic(field.getModifiers())|| Modifier.isFinal(field.getModifiers())) { continue; }
			final Object instance = ExceptionWrapper.executeWithReturn(DualInput.of(field, this), (i)->i.get().get(i.getSecond()));
			if (instance==null) {
				if (!field.isAnnotationPresent(ConfigNullable.class)) { // if marked nullable they obviously know it can be null/not set
					LOGGER.warn("Failed to save the field "+field.getName()+" of "+this.identifier.toString());
				}
				continue;
			}
			final AbstractConfigSerializable<?> ser = getSerializer(field.getType(), true);
			if (ser==null) {
				LOGGER.warn("Failed to save the field "+field.getName()+" of "+this.identifier.toString()+"! There is not a registered serializer for this type");
				continue;
			}
			JsonObject current = jsonObject;
			if (field.isAnnotationPresent(ConfigSub.class)) {
				final ConfigSub sub = field.getAnnotation(ConfigSub.class);
				String[] path = sub.value().equals("") ? sub.subOf().split("\\.") : sub.value().split("\\.");
				JsonObject prev = jsonObject;
				for (String str : path) {
					current = prev.has(str) ? prev.get(str).getAsJsonObject() : new JsonObject();
					prev.add(str, current);
					prev = current;
				}
			}
			if (field.isAnnotationPresent(ConfigComment.class)) {
				final ConfigComment comment = field.getAnnotation(ConfigComment.class);
				current.addProperty(comment.prefix()+field.getName(), comment.value().equals("")?comment.contents():comment.value());
			}
			String name = field.getName();
			if (field.isAnnotationPresent(ConfigName.class)) {
				ConfigName nme = field.getAnnotation(ConfigName.class);
				name = nme.value().equals("") ? nme.name() : nme.value();
			}
			current.add(name, ExceptionWrapper.executeWithReturn(TriInput.of(method, ser, com.bb1.fabric.bfapi.utils.Field.of(instance)), SERIALIZER_INVOKER));
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
	
	@SuppressWarnings("deprecation")
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
				String[] path = sub.value().equals("") ? sub.subOf().split("\\.") : sub.value().split("\\.");
				JsonObject prev = jsonObject;
				for (String str : path) {
					current = prev.has(str) ? prev.get(str).getAsJsonObject() : new JsonObject();
					prev.add(str, current);
					prev = current;
				}
			}
			String name = field.getName();
			if (field.isAnnotationPresent(ConfigName.class)) {
				ConfigName nme = field.getAnnotation(ConfigName.class);
				name = nme.value().equals("") ? nme.name() : nme.value();
			}
			JsonElement instance = current.get(name);
			if (instance==null) {
				if (!field.isAnnotationPresent(ConfigNullable.class)) {
					LOGGER.warn("Failed to load the field "+field.getName()+" of "+this.identifier.toString());
				}
				continue;
			}
			AbstractConfigSerializable<?> ser = getSerializer(field.getType(), true);
			if (ser==null) {
				LOGGER.warn("Failed to load the field "+field.getName()+" of "+this.identifier.toString()+"! There is not a registered serializer for this type");
				continue;
			}
			ExceptionWrapper.execute(TriInput.of(field, this, ExceptionWrapper.executeWithReturn(TriInput.of(method, ser, com.bb1.fabric.bfapi.utils.Field.of(instance)), DESERIALIZER_INVOKER)), DESERIALIZER_SETTER);
		}
	}
	
}
