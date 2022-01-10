package com.bb1.fabric.bfapi.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.bb1.fabric.bfapi.GameObjects;
import com.bb1.fabric.bfapi.Loader;
import com.bb1.fabric.bfapi.utils.Container;
import com.bb1.fabric.bfapi.utils.Field;
import com.bb1.fabric.bfapi.utils.Inputs.QuintInput;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

@Mixin(ItemStack.class)
public class ItemStackUseMixin {
	
	@Inject(method = "finishUsing", at = @At("HEAD"))
	public void callEventAfterUse(World world, LivingEntity user, CallbackInfoReturnable<ItemStack> callback) {
		if (user instanceof PlayerEntity) { return; }
		GameObjects.GameEvents.ITEM_USE.emit(QuintInput.of((ItemStack)(Object)this, world, null, user, new Container<Boolean>(false)));
	}
	
	@Inject(method = "use", at = @At("HEAD"), cancellable = true)
	public void onItemUsedCalledEvent(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> callback) {
		final ItemStack is = (ItemStack)(Object)this;
		Container<Boolean> container = new Container<Boolean>(false);
		GameObjects.GameEvents.ITEM_USE.emit(QuintInput.of(is, world, null, user, container));
		if (!container.getValue()) {
			callback.setReturnValue(TypedActionResult.fail(is));
			callback.cancel();
		}
	}
	
	@Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
	public void useOnBlockCallEvent(ItemUsageContext context, CallbackInfoReturnable<ActionResult> callback) {
		final ItemStack is = (ItemStack)(Object)this;
		Container<Boolean> container = new Container<Boolean>(false);
		GameObjects.GameEvents.ITEM_USE.emit(QuintInput.of(is, context.getWorld(), context.getBlockPos(), context.getPlayer(), container));
		if (!container.getValue()) {
			callback.setReturnValue(ActionResult.FAIL);
			callback.cancel();
		}
		// We can reuse the container as it has to be false to get here
		Loader.MARK_ITEM_USED.emit(QuintInput.of(is, context.getWorld(), context.getBlockPos(), Field.of(context.getPlayer()), container));
		if (!container.getValue()) {
			callback.setReturnValue(ActionResult.FAIL);
			callback.cancel();
		}
	}
	
}
