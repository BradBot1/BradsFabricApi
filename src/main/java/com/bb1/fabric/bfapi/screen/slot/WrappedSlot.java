package com.bb1.fabric.bfapi.screen.slot;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.bb1.fabric.bfapi.utils.ExceptionWrapper;
import com.bb1.fabric.bfapi.utils.Field;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public interface WrappedSlot {
	
	public static @Nullable WrappedSlot getWrappedSlot(@Nullable Object input) {
		return ExceptionWrapper.executeWithReturn(input, (i)->(WrappedSlot)i);
	}
	
	// for locating a slot
	
	public @NotNull UUID getSlotUUID();
	
	// for stopping a person from taking/adding items to a slot
	
	public default void lock() { this.lockSlot(true); }
	
	public default void unlock() { this.lockSlot(false); }
	
	public void lockSlot(boolean lock);
	
	public boolean isLocked();
	
	// for controlling actions that occur when a slot is clicked
	
	public void setClickHandler(@Nullable ClickHandler handler);
	
	public @Nullable ClickHandler getClickHandler();
	
	// helpful junk
	
	public int getIndex();
	
	public Inventory getInventory();
	
	// stuff that is already in a slot, used so you don't have to cast again
	
	public void setStack(ItemStack stack);
	
	public ItemStack getStack();
	
	public void markDirty();
	
	@FunctionalInterface
	public static interface ClickHandler {
		
		public void onClick(@NotNull WrappedSlot caller, @Nullable Field<Entity> clicker);
		
	}
	
}
