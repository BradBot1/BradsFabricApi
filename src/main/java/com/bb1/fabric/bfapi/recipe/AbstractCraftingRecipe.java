package com.bb1.fabric.bfapi.recipe;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class AbstractCraftingRecipe extends AbstractRecipe {
	
	protected AbstractCraftingRecipe(@Nullable ItemStack resultIcon, @Nullable String group) {
		super(resultIcon, group);
	}

	public abstract @NotNull Item[] getIngredients();
	
}
