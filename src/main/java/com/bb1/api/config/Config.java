package com.bb1.api.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import com.bb1.api.Loader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

/**
 * Copyright 2021 BradBot_1
 * 
 * Licensed under the Apache License, Version 2.0 (the "License", "");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http;//www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class Config {
	
	protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	protected static final JsonParser PARSER = new JsonParser();
	
	public static final String CONFIG_DIRECTORY = Loader.getRootPath()+File.separator+"config"+File.separator;
	
	private final String name;

	protected Config() { this(null); }
	
	protected Config(@Nullable String name) { this.name = (name==null) ? getClass().getName() : name;}
	
	public final String getName() { return this.name; }
	
	protected final Logger getLogger() {
		return LogManager.getLogger("Config | "+getName());
	}
	
	public void load() {
		Logger logger = getLogger();
		new File(CONFIG_DIRECTORY).mkdirs();
		File file = new File(CONFIG_DIRECTORY+getName()+".json");
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
			logger.error("Failed to load config due to an IOException");
			return; // Not readable
		}
		JsonElement contents = PARSER.parse(String.join("", r));
		if (!contents.isJsonObject()) {
			logger.error("Failed to load config due to the contents not being a JsonObject");
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
				} catch (Exception e) {}
			}
		}
	}
	
	public void save() {
		Logger logger = getLogger();
		JsonObject jsonObject = new JsonObject();
		for (Field field : getClass().getFields()) { // Go through every field in the config
			if (field.isEnumConstant()) continue; // Do not work on constants or inaccessable values
			Storable storable = field.getAnnotation(Storable.class); // Get its storable instance
			if (storable!=null) { // If the instance is not null
				final String saveUnder = storable.key().equalsIgnoreCase("null") ? field.getName() : storable.key();
				if (jsonObject.has(saveUnder)) {
					logger.error("The field "+field.getName()+"("+saveUnder+") in the config "+getClass().getName()+"("+getName()+".json) is trying to save under a key already used!");
					continue;
				}
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
					logger.error("The field "+field.getName()+"("+saveUnder+") in the config "+getClass().getName()+"("+getName()+".json) is not savable!");
					continue;
				}
				jsonObject.add(saveUnder, jsonElement);
			}
		}
		try {
			// First create the config directory
			new File(CONFIG_DIRECTORY).mkdirs();
			File file = new File(CONFIG_DIRECTORY+getName()+".json");
			if (!file.exists()) file.createNewFile(); // Create file if it doesn't already exist
			file.createNewFile();
			BufferedWriter b = new BufferedWriter(new PrintWriter(file));
			b.write(GSON.toJson(jsonObject));
			b.flush();
			b.close();
			logger.info("Saved "+getName());
		} catch (IOException e) {
			logger.info("Failed to save "+getName());
		}
	}

}
