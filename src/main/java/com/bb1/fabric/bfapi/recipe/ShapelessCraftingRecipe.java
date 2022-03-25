package com.bb1.fabric.bfapi.recipe;

import static com.bb1.fabric.bfapi.registery.BFAPIRegistry.RECIPES;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.bb1.fabric.bfapi.utils.Inputs.QuintInput;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

public class ShapelessCraftingRecipe extends AbstractCraftingRecipe {
	
	public static final Identifier TYPE = new Identifier("minecraft", "shapeless_crafting");
	
	protected @NotNull Item[] ingredients;

	public ShapelessCraftingRecipe(@Nullable ItemStack resultIcon, @Nullable String group, @NotNull Item ingredient, Item... ingredients) {
		super(resultIcon, group);
		if (ingredients==null || ingredients.length==0) {
			this.ingredients = new Item[] { ingredient };
		} else {
			this.ingredients = new Item[ingredients.length+1];
			this.ingredients[0] = ingredient;
			int counter = 1;
			for (Item is : ingredients) {
				this.ingredients[counter++] = is;
			}
		}
	}
	
	protected ShapelessCraftingRecipe(@Nullable ItemStack resultIcon, @Nullable String group, Item... ingredients) {
		super(resultIcon, group);
		this.ingredients = ingredients;
	}
	
	@Override
	public @NotNull Item[] getIngredients() {
		return this.ingredients;
	}
	
	@Override
	public JsonObject serialize(JsonObject jsonObject) {
		JsonArray items = new JsonArray();
		for (Item is : getIngredients()) {
			items.add(Registry.ITEM.getId(is).toString());
		}
		jsonObject.add("ingredients", items);
		return jsonObject;
	}
	
	public static ShapelessCraftingRecipe deserialize(QuintInput<@Nullable String, ItemStack, IRecipeRequirement[], IRecipeResult[], JsonObject> i) {
		final JsonArray array = i.getFith().get("ingredients").getAsJsonArray();
		final Item[] ingredients = new Item[array.size()];
		int index = 0;
		for (JsonElement elem : array) {
			if (elem.isJsonPrimitive() && elem.getAsJsonPrimitive().isString()) {
				ingredients[index++] = Registry.ITEM.get(new Identifier(elem.getAsString()));
			}
		}
		ShapelessCraftingRecipe recipe = new ShapelessCraftingRecipe(i.getSecond(), i.get(), ingredients);
		for (IRecipeResult res : i.getForth()) {
			recipe.addResult(res);
		}
		for (IRecipeRequirement res : i.getThird()) {
			recipe.addRequirement(res);
		}
		return recipe;
	}

	@Override
	public void register(Identifier name) {
		RECIPES.add(name, this);
	}
	
	@Override
	public IWrappedRecipe buildWrappedRecipe(Identifier name) {
		Ingredient[] ingredients = new Ingredient[this.ingredients.length];
		int index = 0;
		for (Item is : this.ingredients) {
			ingredients[index++] = Ingredient.ofItems(is);
		}
		return new WrappedShapelessRecipe(name, getGroup(), getResultIcon(), DefaultedList.copyOf(null, ingredients), this);
	}

	@Override
	public @NotNull Identifier getType() {
		return TYPE;
	}
	
	static final class WrappedShapelessRecipe extends ShapelessRecipe implements IWrappedRecipe {
		
		private final AbstractRecipe inner;

		public WrappedShapelessRecipe(Identifier id, String group, ItemStack output, DefaultedList<Ingredient> input, AbstractRecipe inner) {
			super(id, group, output, input);
			this.inner = inner;
		}

		@Override
		public AbstractRecipe getInner() {
			return this.inner;
		}
		
		@Override
		public ItemStack getOutput() {
			return this.inner.getResultIcon();
		}
		
	}
	
}
