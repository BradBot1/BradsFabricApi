package com.bb1.fabric.bfapi.mixins;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.bb1.fabric.bfapi.screen.slot.WrappedSlot;
import com.bb1.fabric.bfapi.utils.Field;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

@Mixin(Slot.class)
public class SlotMixin implements WrappedSlot {
	
	@Shadow
	public @Final Inventory inventory;
	@Shadow
	public @Final int index;
	@Unique
	private boolean _locked;
	@Unique
	private ClickHandler _clickHandler;
	@Unique
	private final UUID _uuid = UUID.randomUUID();
	
	@Override
	public void lockSlot(boolean lock) {
		this._locked = lock;
	}

	@Override
	public boolean isLocked() {
		return _locked;
	}

	@Override
	public void setClickHandler(@Nullable ClickHandler handler) {
		this._clickHandler = handler;
	}

	@Override
	public @Nullable ClickHandler getClickHandler() {
		return this._clickHandler;
	}
	
	@Override
	public Inventory getInventory() {
		return this.inventory;
	}
	
	@Override
	public int getIndex() {
		return this.index;
	}
	
	@Override
	public @NotNull UUID getSlotUUID() {
		return this._uuid;
	}
	
	@Override
	public void setStack(ItemStack stack) {
		this.inventory.setStack(this.index, stack);
	}
	
	@Override
	public ItemStack getStack() {
		return this.inventory.getStack(this.index);
	}
	
	@Override
	public void markDirty() {
		this.inventory.markDirty();
	}
	
	// modifying slot methods to make enforce the WrappedSlot's options
	
	@Inject(method = "onQuickTransfer", at = @At("HEAD"), cancellable = true)
	public void onQuickTransferCheckIfLocked(ItemStack newItem, ItemStack original, CallbackInfo callbackInfo) {
		if (this._locked) { callbackInfo.cancel(); }
		if (this._clickHandler!=null) {
			this._clickHandler.onClick(this, null);
		}
	}
	
	@Inject(method = "canInsert", at = @At("RETURN"), cancellable = true)
	public void ensureNotLockedBeforeInsert(ItemStack stack, CallbackInfoReturnable<Boolean> callbackInfo) {
		boolean ret = callbackInfo.getReturnValueZ() && !this._locked; // ensure we allow regular validation
		callbackInfo.setReturnValue(ret);
		if (!ret && this._clickHandler!=null) {
			this._clickHandler.onClick(this, null);
		}
	}
	
	@Inject(method = "canTakeItems", at = @At("RETURN"), cancellable = true)
	public void whenCheckingIfItemsCanBeTookEnsureNotLocked(PlayerEntity playerEntity, CallbackInfoReturnable<Boolean> callbackInfo) {
		boolean ret = callbackInfo.getReturnValueZ() && !this._locked; // ensure we allow regular validation
		callbackInfo.setReturnValue(ret);
		if (!ret && this._clickHandler!=null) {
			this._clickHandler.onClick(this, Field.of(playerEntity));
		}
	}
	
	@Inject(method = "takeStack", at = @At("HEAD"), cancellable = true)
	 public void ifTrysToTakeStackWhenLockedGiveAir(int amount, CallbackInfoReturnable<ItemStack> callbackInfo) {
		if (this._locked) { // return air if locked
			callbackInfo.setReturnValue(ItemStack.EMPTY);
			callbackInfo.cancel();
		}
		if (this._clickHandler!=null) {
			this._clickHandler.onClick(this, null);
		}
	}
	
	@Inject(method = "onCrafted(Lnet/minecraft/item/ItemStack;I)V", at = @At("HEAD"))
	protected void onCraftedCallClickHandler(ItemStack stack, int amount, CallbackInfo callbackInfo) {
		if (this._clickHandler!=null) {
			this._clickHandler.onClick(this, null);
		}
	}
	
	@Inject(method = "onCrafted(Lnet/minecraft/item/ItemStack;)V", at = @At("HEAD"))
	protected void onCraftedCallClickHandler(ItemStack stack, CallbackInfo callbackInfo) {
		if (this._clickHandler!=null) {
			this._clickHandler.onClick(this, null);
		}
	}
	
	@Inject(method = "onTake(I)V", at = @At("HEAD"))
	protected void onTakeCallClickHandler(int amount, CallbackInfo callbackInfo) {
		if (this._clickHandler!=null) {
			this._clickHandler.onClick(this, null);
		}
	}
	
	@Inject(method = "onTakeItem(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;)V", at = @At("HEAD"))
	public void onTakeCallClickHandler(PlayerEntity player, ItemStack stack, CallbackInfo callbackInfo) {
		if (this._clickHandler!=null) {
			this._clickHandler.onClick(this, Field.of(player));
		}
	}
	
}
