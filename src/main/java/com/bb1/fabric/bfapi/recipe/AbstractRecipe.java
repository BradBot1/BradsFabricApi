package com.bb1.fabric.bfapi.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.bb1.fabric.bfapi.GameObjects;
import com.bb1.fabric.bfapi.config.AbstractConfigSerializable;
import com.bb1.fabric.bfapi.config.Config;
import com.bb1.fabric.bfapi.permissions.PermissionUtils;
import com.bb1.fabric.bfapi.registery.IRegisterable;
import com.bb1.fabric.bfapi.utils.Field;
import com.bb1.fabric.bfapi.utils.Inputs.QuintInput;
import com.bb1.fabric.bfapi.utils.ItemStackUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * 
 * Copyright 2022 BradBot_1
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * Used to handle custom recipe stuff
 * 
 * @author BradBot_1
 */
public abstract class AbstractRecipe implements IRegisterable {
	
	private static boolean _init = false;
	
	public static final void init() {
		if (_init) { return; }
		_init = true;
		addRequirementBuilder("xp", (n)->{
			final int amount = n.getAsInt();
			return new IRecipeRequirement() {

				@Override
				public boolean canCraft(Field<Entity> crafter) {
					return crafter.getObject() instanceof PlayerEntity pe ? pe.experienceLevel >= amount : false;
				}

				@Override
				public JsonObject addToObject(JsonObject object) {
					object.addProperty("xp", amount);
					return object;
				}
				
			};
		});
		addResultBuilder("xp", (n)-> {
			final int amount = n.getAsInt();
			return new IRecipeResult() {

				@Override
				public void onCraft(Field<Entity> crafter) {
					if (crafter.getObject() instanceof PlayerEntity pe) {
						pe.addExperienceLevels(amount);
					}
				}

				@Override
				public JsonObject addToObject(JsonObject object) {
					object.addProperty("xp", amount);
					return object;
				}
				
			};
		});
		addResultBuilder("run", (n)-> {
			final JsonObject obj = n.getAsJsonObject();
			// here we build the JSON object's data into an array
			final String[][] commands = new String[][] { new String[obj.get("server").getAsJsonArray().size()], new String[obj.get("player").getAsJsonArray().size()] };
			int index = 0;
			for (JsonElement elem : obj.get("server").getAsJsonArray()) {
				if (elem.isJsonPrimitive() && elem.getAsJsonPrimitive().isString()) {
					commands[0][index++] = elem.getAsString();
				}
			}
			index = 0; // reset pointer
			for (JsonElement elem : obj.get("player").getAsJsonArray()) {
				if (elem.isJsonPrimitive() && elem.getAsJsonPrimitive().isString()) {
					commands[1][index++] = elem.getAsString();
				}
			}
			return new IRecipeResult() {

				@Override
				public void onCraft(Field<Entity> crafter) {
					for (String string : commands[0]) {
						GameObjects.getCommandManager().execute(GameObjects.getMinecraftServer().getCommandSource(), string);
					}
					if (crafter.getObject() instanceof PlayerEntity pe) {
						for (String string : commands[1]) {
							GameObjects.getCommandManager().execute(pe.getCommandSource(), string);
						}
					}
				}

				@Override
				public JsonObject addToObject(JsonObject object) {
					JsonObject main = new JsonObject();
					JsonArray server = new JsonArray();
					for (String string : commands[0]) {
						server.add(string);
					}
					main.add("server", server);
					JsonArray player = new JsonArray();
					for (String string : commands[1]) {
						player.add(string);
					}
					main.add("player", player);
					object.add("run", main);
					return object;
				}
				
			};
		});
		addRequirementBuilder("advancements", (n)->{
			// we can't cache advancements here as they may not be loaded, so we will cache their identifiers instead
			final Identifier[] identifiers = new Identifier[n.getAsJsonArray().size()];
			int index = 0;
			for (JsonElement elem : n.getAsJsonArray()) {
				if (elem.isJsonPrimitive() && elem.getAsJsonPrimitive().isString()) {
					identifiers[index++] = new Identifier(elem.getAsString());
				}
			}
			return new IRecipeRequirement() {

				@Override
				public boolean canCraft(Field<Entity> crafter) {
					if (!(crafter.getObject() instanceof ServerPlayerEntity)) { return false; }
					final ServerPlayerEntity spe = crafter.get();
					final PlayerAdvancementTracker tracker = spe.getAdvancementTracker();
					final ServerAdvancementLoader manager = GameObjects.getMinecraftServer().getAdvancementLoader();
					for (Identifier identifier : identifiers) {
						if (!tracker.getProgress(manager.get(identifier)).isDone()) {
							return false;
						}
					}
					return true;
				}

				@Override
				public JsonObject addToObject(JsonObject object) {
					JsonArray advancements = new JsonArray();
					for (Identifier identifier : identifiers) {
						advancements.add(identifier.toString());
					}
					object.add("advancements", advancements);
					return object;
				}
				
			};
		});
		addResultBuilder("advancements", (n)-> {
			// we can't cache advancements here as they may not be loaded, so we will cache their identifiers instead
			final Identifier[] identifiers = new Identifier[n.getAsJsonArray().size()];
			int index = 0;
			for (JsonElement elem : n.getAsJsonArray()) {
				if (elem.isJsonPrimitive() && elem.getAsJsonPrimitive().isString()) {
					identifiers[index++] = new Identifier(elem.getAsString());
				}
			}
			return new IRecipeResult() {

				@Override
				public void onCraft(Field<Entity> crafter) {
					if (!(crafter.getObject() instanceof ServerPlayerEntity)) { return; }
					final ServerPlayerEntity spe = crafter.get();
					final PlayerAdvancementTracker tracker = spe.getAdvancementTracker();
					final ServerAdvancementLoader manager = GameObjects.getMinecraftServer().getAdvancementLoader();
					for (Identifier identifier : identifiers) {
						AdvancementProgress progress = tracker.getProgress(manager.get(identifier));
						for (String string : progress.getUnobtainedCriteria()) {
							progress.obtain(string);
						}
					}
				}

				@Override
				public JsonObject addToObject(JsonObject object) {
					JsonArray advancements = new JsonArray();
					for (Identifier identifier : identifiers) {
						advancements.add(identifier.toString());
					}
					object.add("advancements", advancements);
					return object;
				}
				
			};
		});
		addRequirementBuilder("permissions", (n)->{
			final String[] permissions = new String[n.getAsJsonArray().size()];
			int index = 0;
			for (JsonElement elem : n.getAsJsonArray()) {
				if (elem.isJsonPrimitive() && elem.getAsJsonPrimitive().isString()) {
					permissions[index++] = elem.getAsString();
				}
			}
			return new IRecipeRequirement() {

				@Override
				public boolean canCraft(Field<Entity> crafter) {
					for (String string : permissions) {
						if (!PermissionUtils.hasPermission(crafter, string)) {
							return false;
						}
					}
					return true;
				}

				@Override
				public JsonObject addToObject(JsonObject object) {
					JsonArray perms = new JsonArray();
					for (String string : permissions) {
						perms.add(string);
					}
					object.add("permissions", perms);
					return object;
				}
				
			};
		});
		addResultBuilder("permissions", (n)-> {
			final String[] permissions = new String[n.getAsJsonArray().size()];
			int index = 0;
			for (JsonElement elem : n.getAsJsonArray()) {
				if (elem.isJsonPrimitive() && elem.getAsJsonPrimitive().isString()) {
					permissions[index++] = elem.getAsString();
				}
			}
			return new IRecipeResult() {

				@Override
				public void onCraft(Field<Entity> crafter) {
					for (String string : permissions) {
						PermissionUtils.givePermission(crafter, string, true);
					}
				}

				@Override
				public JsonObject addToObject(JsonObject object) {
					JsonArray perms = new JsonArray();
					for (String string : permissions) {
						perms.add(string);
					}
					object.add("permissions", perms);
					return object;
				}
				
			};
		});
		addResultBuilder("items", (n)-> {
			final ItemStack[] items = new ItemStack[n.getAsJsonArray().size()];
			final AbstractConfigSerializable<ItemStack> ser = Config.getSerializer(ItemStack.class, false);
			int index = 0;
			for (JsonElement elem : n.getAsJsonArray()) {
				if (elem.isJsonObject()) {
					items[index++] = ser.deserialize(Field.of(elem));
				}
			}
			return new IRecipeResult() {

				@Override
				public void onCraft(Field<Entity> crafter) {
					for (ItemStack is : items) {
						if (crafter.get() instanceof PlayerEntity pe) {
							pe.getInventory().insertStack(ItemStackUtils.clone(is));
						}
					}
				}

				@Override
				public JsonObject addToObject(JsonObject object) {
					JsonArray perms = new JsonArray();
					for (ItemStack itemStack : items) {
						perms.add(ser.serialize(Field.of(itemStack)));
					}
					object.add("items", perms);
					return object;
				}
				
			};
		});
		addResultBuilder("messages", (n)-> {
			final Text[] texts = new Text[n.getAsJsonArray().size()];
			final AbstractConfigSerializable<Text> ser = Config.getSerializer(Text.class, true);
			int index = 0;
			for (JsonElement elem : n.getAsJsonArray()) {
				texts[index++] = ser.deserialize(Field.of(elem));
			}
			return new IRecipeResult() {

				@Override
				public void onCraft(Field<Entity> crafter) {
					for (Text text : texts) {
						crafter.getObject().getCommandSource().sendFeedback(text, false);
					}
				}

				@Override
				public JsonObject addToObject(JsonObject object) {
					JsonArray perms = new JsonArray();
					for (Text text : texts) {
						perms.add(ser.serialize(Field.of(text)));
					}
					object.add("messages", perms);
					return object;
				}
				
			};
		});
		addRecipeBuilder(ShapelessCraftingRecipe.TYPE, (i)->ShapelessCraftingRecipe.deserialize(i));
		addRecipeBuilder(ShapedCraftingRecipe.TYPE, (i)->ShapedCraftingRecipe.deserialize(i));
	}
	
	private static final Map<String, Function<JsonElement, IRecipeRequirement>> REQUIREMENT_BUILDER = new HashMap<String, Function<JsonElement, IRecipeRequirement>>();
	
	public static final void addRequirementBuilder(String key, Function<JsonElement, IRecipeRequirement> value) {
		REQUIREMENT_BUILDER.put(key, value);
	}
	
	public static final IRecipeRequirement buildRequirement(String key, JsonElement value) {
		return REQUIREMENT_BUILDER.get(key).apply(value);
	}
	
	private static final Map<String, Function<JsonElement, IRecipeResult>> RESULT_BUILDER = new HashMap<String, Function<JsonElement, IRecipeResult>>();
	
	public static final void addResultBuilder(String key, Function<JsonElement, IRecipeResult> value) {
		RESULT_BUILDER.put(key, value);
	}
	
	public static final IRecipeResult buildResult(String key, JsonElement value) {
		return RESULT_BUILDER.get(key).apply(value);
	}
	
	private static final Map<Identifier, Function<QuintInput<@Nullable String, ItemStack, IRecipeRequirement[], IRecipeResult[], JsonObject>, AbstractRecipe>> RECIPE_BUILDER = new HashMap<Identifier, Function<QuintInput<@Nullable String, ItemStack, IRecipeRequirement[], IRecipeResult[], JsonObject>, AbstractRecipe>>();
	
	public static final void addRecipeBuilder(Identifier key, Function<QuintInput<@Nullable String, ItemStack, IRecipeRequirement[], IRecipeResult[], JsonObject>, AbstractRecipe> value) {
		RECIPE_BUILDER.put(key, value);
	}
	
	public static final AbstractRecipe buildRecipe(Identifier key, QuintInput<@Nullable String, ItemStack, IRecipeRequirement[], IRecipeResult[], JsonObject> value) {
		return RECIPE_BUILDER.get(key).apply(value);
	}
	
	private final @NotNull ItemStack resultIcon;
	
	private final @Nullable String group;
	
	protected final List<IRecipeResult> results = new ArrayList<IRecipeResult>();
	
	protected final List<IRecipeRequirement> requirements = new ArrayList<IRecipeRequirement>();
	
	protected AbstractRecipe(@Nullable ItemStack resultIcon, @Nullable String group) {
		this.resultIcon = resultIcon==null||resultIcon.isEmpty() ? Items.BARRIER.getDefaultStack() : resultIcon;
		this.group = group==null ? Registry.ITEM.getId(resultIcon.getItem()).toUnderscoreSeparatedString() : group;
	}
	
	public void addResult(@NotNull IRecipeResult result, @Nullable IRecipeResult... results) {
		this.results.add(result);
		if (results==null) { return; }
		for (IRecipeResult result2 : results) {
			this.results.add(result2);
		}
	}
	
	public void addRequirement(@NotNull IRecipeRequirement result, @Nullable IRecipeRequirement... results) {
		this.requirements.add(result);
		if (results==null) { return; }
		for (IRecipeRequirement result2 : results) {
			this.requirements.add(result2);
		}
	}
	
	public boolean canCraft(Entity entity) {
		final Field<Entity> field = Field.of(entity);
		for (IRecipeRequirement req : this.requirements) {
			if (!req.canCraft(field)) { return false; }
		}
		return true;
	}
	
	public void onCrafted(Entity entity) {
		final Field<Entity> field = Field.of(entity);
		for (IRecipeResult result : this.results) {
			result.onCraft(field);
		}
	}
	
	public final List<IRecipeResult> getResults() {
		return this.results;
	}
	
	public final List<IRecipeRequirement> getRequirements() {
		return this.requirements;
	}
	
	public @NotNull ItemStack getResultIcon() {
		return this.resultIcon;
	}
	
	public final @Nullable String getGroup() {
		return this.group;
	}
	
	public abstract @NotNull Identifier getType();
	/**
	 * The given {@link JsonObject} will already contain:
	 * 	the type (grabbed via {@link #getType()}),
	 *  the group if it is not null (grabbed via {@link #getGroup()}),
	 *  the icon (grabbed via {@link #getResultIcon()}),
	 *  the custom results (grabbed via {@link #getResults()}),
	 *  the custom requirements (grabbed via {@link #getRequirements()})
	 */
	public abstract @NotNull JsonObject serialize(@NotNull JsonObject jsonObject);
	
	public abstract @NotNull IWrappedRecipe buildWrappedRecipe(@NotNull Identifier identifier);
	
}
