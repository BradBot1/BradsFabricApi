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
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

public class ShapedCraftingRecipe extends AbstractCraftingRecipe {
	
	public static final Identifier TYPE = new Identifier("minecraft", "shaped_crafting");
	
	protected @NotNull Item[] ingredients;
	
	protected final int[] size;

	public ShapedCraftingRecipe(@Nullable ItemStack resultIcon, @Nullable String group, int[] size, @NotNull Item ingredient, Item... ingredients) {
		super(resultIcon, group);
		this.size = size;
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
	
	protected ShapedCraftingRecipe(@Nullable ItemStack resultIcon, @Nullable String group, int[] size, Item... ingredients) {
		super(resultIcon, group);
		this.size = size;
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
			items.add((is==null)?Registry.ITEM.getId(Items.AIR).toString():Registry.ITEM.getId(is).toString());
		}
		jsonObject.add("ingredients", items);
		jsonObject.addProperty("size", this.size[0] + "x" + this.size[1]);
		return jsonObject;
	}
	
	public static ShapedCraftingRecipe deserialize(QuintInput<@Nullable String, ItemStack, IRecipeRequirement[], IRecipeResult[], JsonObject> i) {
		final JsonArray array = i.getFith().get("ingredients").getAsJsonArray();
		final Item[] ingredients = new Item[array.size()];
		int index = 0;
		for (JsonElement elem : array) {
			if (elem.isJsonPrimitive() && elem.getAsJsonPrimitive().isString()) {
				final Item is = Registry.ITEM.get(new Identifier(elem.getAsString()));
				ingredients[index++] = is.asItem().equals(Items.AIR)?null:is;
			}
		}
		String[] split = i.getFith().get("size").getAsString().split("x");
		ShapedCraftingRecipe recipe = new ShapedCraftingRecipe(i.getSecond(), i.get(), new int[] { Integer.parseInt(split[0]), Integer.parseInt(split[1]) }, ingredients);
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
		Registry.register(RECIPES, name, this);
	}

	@Override
	public @NotNull IWrappedRecipe buildWrappedRecipe(@NotNull Identifier identifier) {
		Ingredient[] ingredients = new Ingredient[this.ingredients.length];
		int index = 0;
		for (Item is : this.ingredients) {
			if (is==null) { continue; }
			ingredients[index++] = Ingredient.ofItems(is);
		}
		return new WrappedShapedRecipe(identifier, getGroup(), this.size, getResultIcon(), DefaultedList.copyOf(null, ingredients), this);
	}
	
	@Override
	public @NotNull Identifier getType() {
		return TYPE;
	}
	
	static final class WrappedShapedRecipe extends ShapedRecipe implements IWrappedRecipe {
		
		private final AbstractRecipe inner;

		public WrappedShapedRecipe(Identifier id, String group, int[] size, ItemStack output, DefaultedList<Ingredient> input, AbstractRecipe inner) {
			super(id, group, size[0], size[1], input, output); // width, height
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
