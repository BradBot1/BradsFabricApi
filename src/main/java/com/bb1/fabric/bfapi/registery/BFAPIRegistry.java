package com.bb1.fabric.bfapi.registery;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.bb1.fabric.bfapi.Constants;
import com.bb1.fabric.bfapi.GameObjects;
import com.bb1.fabric.bfapi.config.AbstractConfigSerializable;
import com.bb1.fabric.bfapi.events.Event;
import com.bb1.fabric.bfapi.nbt.mark.INbtMarkListener;
import com.bb1.fabric.bfapi.permissions.Permission;
import com.bb1.fabric.bfapi.permissions.database.IPermissionDatabase;
import com.bb1.fabric.bfapi.recipe.AbstractRecipe;
import com.bb1.fabric.bfapi.text.parser.TextParserLookup;

import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;

public final class BFAPIRegistry {
	
	public static final SimpleRegistry<INbtMarkListener> MARK_LISTENER = new SimpleRegistry<INbtMarkListener>(new Identifier(Constants.ID, "mark_listeners"));
	public static final SimpleRegistry<Event<?>> EVENTS = new SimpleRegistry<Event<?>>(new Identifier(Constants.ID, "events"));
	public static final SimpleUniqueRegistry<AbstractConfigSerializable<?>> CONFIG_SERIALIZER = new SimpleUniqueRegistry<AbstractConfigSerializable<?>>(new Identifier(Constants.ID, "config_serializers"));
	public static final SimpleRegistry<Permission> PERMISSIONS = new SimpleRegistry<Permission>(new Identifier(Constants.ID, "permissions"));
	public static final SimpleRegistry<IPermissionDatabase> PERMISSION_DATABASES = new SimpleRegistry<IPermissionDatabase>(new Identifier(Constants.ID, "permission_databases"));
	private static final Queue<Recipe<?>> RECIPES_TO_BE_ADDED = new LinkedList<>();	
	@SuppressWarnings("resource")
	public static final SimpleValidatedRegistry<AbstractRecipe> RECIPES = new SimpleValidatedRegistry<AbstractRecipe>(new Identifier(Constants.ID, "recipes"), (i, i2)->{
		Recipe<?> recipe = (Recipe<?>) (Object) i.buildWrappedRecipe(i2);
		if (GameObjects.getMinecraftServer()==null) {
			if (RECIPES_TO_BE_ADDED.isEmpty()) {
				GameObjects.GameEvents.SERVER_START.addHandler((mc)->{
					List<Recipe<?>> recipes = new ArrayList<Recipe<?>>(); // MAYBE: work out a good default length?
					for (Map<Identifier, Recipe<?>> map : GameObjects.getMinecraftServer().getRecipeManager().recipes.values()) {
						recipes.addAll(map.values());
					}
					Recipe<?> r = RECIPES_TO_BE_ADDED.poll();
					while (r!=null) {
						recipes.add(r);
						r = RECIPES_TO_BE_ADDED.poll();
					}
					GameObjects.getMinecraftServer().getRecipeManager().setRecipes(recipes);
				});
			}
			RECIPES_TO_BE_ADDED.add(recipe);
			return true;
		}
		List<Recipe<?>> recipes = new ArrayList<Recipe<?>>(); // MAYBE: work out a good default length?
		for (Map<Identifier, Recipe<?>> map : GameObjects.getMinecraftServer().getRecipeManager().recipes.values()) {
			recipes.addAll(map.values());
		}
		recipes.add(recipe);
		GameObjects.getMinecraftServer().getRecipeManager().setRecipes(recipes);
		return true;
	});
	
	public static final SimpleRegistry<TextParserLookup> TEXT_PARSER_LOOKUPS = new SimpleRegistry<TextParserLookup>(new Identifier(Constants.ID, "text_parser_lookups"));
	
}
