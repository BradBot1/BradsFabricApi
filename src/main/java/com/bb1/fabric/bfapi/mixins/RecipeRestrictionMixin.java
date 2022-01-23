package com.bb1.fabric.bfapi.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.bb1.fabric.bfapi.recipe.IWrappedRecipe;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeUnlocker;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

@Mixin({ CraftingResultInventory.class }) // RecipeUnlocker
public abstract class RecipeRestrictionMixin implements RecipeUnlocker { // i have to use this hacky method because mixins cannot work on interfaces ;/
	
	@Shadow public abstract Recipe<?> getLastRecipe();
	
	@Override
	public boolean shouldCraftRecipe(World world, ServerPlayerEntity player, Recipe<?> recipe) {
		return (recipe instanceof IWrappedRecipe wr && !wr.getInner().canCraft(player)) ? false : RecipeUnlocker.super.shouldCraftRecipe(world, player, recipe);
	}
	
	@Override
	public void unlockLastRecipe(PlayerEntity player) {
		Recipe<?> recipe = getLastRecipe();
		if (recipe!=null && recipe instanceof IWrappedRecipe wr) { wr.getInner().onCrafted(player); }
		RecipeUnlocker.super.unlockLastRecipe(player);
	}
	
}
