package com.bb1.api.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Scanner;

import org.jetbrains.annotations.NotNull;

import com.bb1.api.Loader;
import com.bb1.api.events.Events;
import com.bb1.api.events.Events.ConfigChangeEvent;
import com.bb1.api.events.Events.ConfigChangeEvent.ConfigChangeType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

/**
 * Look at <i>Config.md</i> for instructions and more information
 */
public abstract class Config {
	
	protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	protected static final JsonParser PARSER = new JsonParser();
	
	public static final String CONFIG_DIRECTORY = Loader.getRootPath()+File.separator+"config"+File.separator;
	/** Names cannot contain extensions, for example exampleConfig.yml is not a valid config name */
	@NotNull private final String name;
	
	protected Config(String name) {
		this.name = name;
		if (throwAndHandleEvents()) { // We should handle events
			Events.CONFIG_EVENT.register((event) -> {
				if (!event.getConfig().equalsIgnoreCase(getConfigName()) || !event.getType().equals(ConfigChangeType.REFRESH)) return;
				load();
			});
			Events.AUTOSAVE_EVENT.register((event) -> {
				save();
			});
			Events.LOAD_EVENT.register((event) -> {
				load();
			});
			Events.UNLOAD_EVENT.register((event) -> {
				save();
			});
		}
	}
	/**
	 * This defines what the config should be saved as
	 */
	@NotNull
	public final String getConfigName() { return this.name.replaceAll("[\\.]\\w+", ""); }
	/**
	 * If the config should through {@link ConfigChangeEvent}'s
	 * 
	 * @apiNote will make it auto save on certain events
	 */
	public boolean throwAndHandleEvents() { return true; }
	/**
	 * Tells all config instances <i>(of this config)</i> to refresh
	 * 
	 * @apiNote Simply calls the refresh event
	 */
	public void refresh() {
		if (throwAndHandleEvents()) Events.CONFIG_EVENT.onEvent(new ConfigChangeEvent(this, ConfigChangeType.REFRESH));
	}
	
	public void load() {
		new File(CONFIG_DIRECTORY).mkdirs();
		File file = new File(CONFIG_DIRECTORY+getConfigName()+".json");
		if (!file.exists()) return; // Can't load anything if there is file
		ArrayList<String> r = new ArrayList<>();
		try {
			// Attempt to read the config's contents
			Scanner s = new Scanner(file);
			while (s.hasNext()) {
		    	r.add(s.nextLine());
			}
			s.close();
		} catch (IOException e) {
			System.err.println("Failed to load config due to an IOException");
			return; // Not readable
		}
		JsonElement contents = PARSER.parse(String.join("", r));
		if (!contents.isJsonObject()) {
			System.err.println("Failed to load config due to the contents not being a JsonObject");
			return;
		}
		JsonObject jsonObject = contents.getAsJsonObject();
		for (Field field : getClass().getFields()) { // Go through every field in the config
			if (field.isEnumConstant()) continue; // Do not work on constants or inaccessable values
			Storable storable = field.getAnnotation(Storable.class); // Get its storable instance
			if (storable!=null) { // If the instance is not null
				final String saveUnder = storable.key().equalsIgnoreCase("null") ? field.getName() : storable.key();
				JsonElement jsonElement = jsonObject.get(saveUnder);
				if (jsonElement==null || jsonElement.isJsonNull()) continue; // Not in config
				final Class<?> type = field.getType();
				try {
					if (type.equals(JsonElement.class)) {
						field.set(this, jsonElement);
					} else if (jsonElement.isJsonObject() && type.equals(JsonObject.class)) {
						field.set(this, jsonElement.getAsJsonObject());
					} else if (jsonElement.isJsonArray() && type.equals(JsonArray.class)) {
						field.set(this, jsonElement.getAsJsonArray());
					} else if(jsonElement.isJsonPrimitive() && type.equals(JsonPrimitive.class)) {
						field.set(this, jsonElement.getAsJsonPrimitive());
					} else {
						final JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
						if (type.equals(String.class)) {
							field.set(this, jsonPrimitive.getAsString());
						} else if (type.equals(int.class) || type.equals(Integer.class)) {
							field.set(this, jsonPrimitive.getAsInt());
						} else if (type.equals(byte.class) || type.equals(Byte.class)) {
							field.set(this, jsonPrimitive.getAsByte());
						} else if (type.equals(short.class) || type.equals(Short.class)) {
							field.set(this, jsonPrimitive.getAsShort());
						} else if (type.equals(long.class) || type.equals(Long.class)) {
							field.set(this, jsonPrimitive.getAsLong());
						} else if (type.equals(float.class) || type.equals(Float.class)) {
							field.set(this, jsonPrimitive.getAsFloat());
						} else if (type.equals(double.class) || type.equals(Double.class)) {
							field.set(this, jsonPrimitive.getAsDouble());
						} else if (type.equals(boolean.class) || type.equals(Boolean.class)) {
							field.set(this, jsonPrimitive.getAsBoolean());
						} else if (type.equals(char.class) || type.equals(Character.class)) {
							field.set(this, jsonPrimitive.getAsCharacter());
						}
					}
					if (throwAndHandleEvents()) Events.CONFIG_EVENT.onEvent(new ConfigChangeEvent(this, ConfigChangeType.LOAD));
				} catch (Exception e) {}
			}
		}
	}
	
	public void save() {
		JsonObject jsonObject = new JsonObject();
		for (Field field : getClass().getFields()) { // Go through every field in the config
			if (field.isEnumConstant()) continue; // Do not work on constants or inaccessable values
			Storable storable = field.getAnnotation(Storable.class); // Get its storable instance
			if (storable!=null) { // If the instance is not null
				final String saveUnder = storable.key().equalsIgnoreCase("null") ? field.getName() : storable.key();
				if (jsonObject.has(saveUnder)) throw new IllegalArgumentException("The field "+field.getName()+"("+saveUnder+") in the config "+getClass().getName()+"("+getConfigName()+".json) is trying to save under a key already used!");
				Object value;
				try {
					@SuppressWarnings("deprecation")
					boolean b = field.isAccessible();
					field.setAccessible(true);
					value = field.get(this);
					field.setAccessible(b);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					continue;
				}
				if (value==null) continue; // Can't save nothing
				JsonElement jsonElement;
				if (value instanceof JsonElement) {
					jsonElement = (JsonElement) value;
				} else if(value instanceof String) {
					jsonElement = new JsonPrimitive((String) value);
				} else if(value instanceof Character) {
					jsonElement = new JsonPrimitive((Character) value);
				} else if(value instanceof Number) {
					jsonElement = new JsonPrimitive((Number) value);
				} else if(value instanceof Boolean) {
					jsonElement = new JsonPrimitive((Boolean) value);
				} else {
					throw new IllegalArgumentException("The field "+field.getName()+"("+saveUnder+") in the config "+getClass().getName()+"("+getConfigName()+".json) is not savable!");
				}
				jsonObject.add(saveUnder, jsonElement);
			}
		}
		try {
			// First create the config directory
			new File(CONFIG_DIRECTORY).mkdirs();
			File file = new File(CONFIG_DIRECTORY+getConfigName()+".json");
			if (!file.exists()) file.createNewFile(); // Create file if it doesn't already exist
			file.createNewFile();
			BufferedWriter b = new BufferedWriter(new PrintWriter(file));
			b.write(GSON.toJson(jsonObject));
			b.flush();
			b.close();
			if (throwAndHandleEvents()) Events.CONFIG_EVENT.onEvent(new ConfigChangeEvent(this, ConfigChangeType.SAVE));
		} catch (IOException e) {
			// TODO: log error
		}
	}
	
}