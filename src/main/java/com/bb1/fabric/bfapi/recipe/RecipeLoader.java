package com.bb1.fabric.bfapi.recipe;

import static com.bb1.fabric.bfapi.Constants.GSON;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.UUID;

import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import com.bb1.fabric.bfapi.Constants;
import com.bb1.fabric.bfapi.config.AbstractConfigSerializable;
import com.bb1.fabric.bfapi.config.Config;
import com.bb1.fabric.bfapi.utils.Field;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

public final class RecipeLoader {
	
	public static final String RECIPE_DIRECTORY = FabricLoader.getInstance().getGameDir().toFile().getAbsolutePath()+File.separatorChar+"recipes"+File.separatorChar;
	
	private static final Map<Identifier, AbstractRecipe> DEFAULT_RECIPES = new HashMap<Identifier, AbstractRecipe>();
	
	private static final Logger LOGGER = Constants.createSubLogger("RecipeLoader");
	
	public static final void addDefaultRecipe(@NotNull Identifier identifier, @NotNull AbstractRecipe recipe) {
		DEFAULT_RECIPES.put(identifier, recipe);
	}
	
	public static final List<AbstractRecipe> loadRecipes() {
		final List<AbstractRecipe> recipes = new LinkedList<AbstractRecipe>();
		final AbstractConfigSerializable<AbstractRecipe> ser = Config.getSerializer(AbstractRecipe.class, true);
		File dir = new File(RECIPE_DIRECTORY);
		dir.mkdirs();
		ArrayList<String> r = new ArrayList<String>();
		for (File file : dir.listFiles((f)->f.getAbsolutePath().endsWith(".json"))) {
			r.clear();
			try {
				Scanner s = new Scanner(file);
				while (s.hasNext()) {
			    	r.add(s.nextLine());
				}
				s.close();
			} catch (IOException e) {
				LOGGER.warn("Failed to read the recipe in "+file.getName());
				continue;
			}
			JsonElement jse = JsonParser.parseString(String.join("", r));
			if (jse instanceof JsonObject js) {
				Identifier id = new Identifier(js.get("id").getAsString());
				AbstractRecipe recipe = ser.deserialize(Field.of(js.get("recipe")));
				recipe.register(id);
				recipes.add(recipe);
				DEFAULT_RECIPES.remove(id); // remove default as its loaded
			}
		}
		for (Entry<Identifier, AbstractRecipe> recipe : DEFAULT_RECIPES.entrySet()) {
			recipe.getValue().register(recipe.getKey());
			saveRecipe(recipe.getKey(), recipe.getValue(), recipe.getKey().toUnderscoreSeparatedString());
			recipes.add(recipe.getValue());
		}
		LOGGER.info("Loaded " + recipes.size() + " recipes");
		return recipes;
	}
	
	public static final void saveRecipe(Identifier id, AbstractRecipe recipe) { saveRecipe(id, recipe, UUID.randomUUID().toString()); }
	
	public static final void saveRecipe(Identifier id, AbstractRecipe recipe, String fileName) {
		new File(RECIPE_DIRECTORY).mkdirs();
		File file = new File(RECIPE_DIRECTORY + fileName + ".json");
		file.delete();
		try {
			file.createNewFile();
			BufferedWriter b = new BufferedWriter(new PrintWriter(file));
			JsonObject js = new JsonObject();
			js.addProperty("id", id.toString());
			js.add("recipe", Config.getSerializer(AbstractRecipe.class, true).serialize(Field.of(recipe)));
			b.write(GSON.toJson(js));
			b.flush();
			b.close();
		} catch (Exception e) { }
	}
	
}
