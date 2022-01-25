package com.bb1.fabric.bfapi.screen.slot;

import static net.minecraft.item.ItemStack.EMPTY;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.bb1.fabric.bfapi.screen.WrappedScreen;
import com.bb1.fabric.bfapi.utils.Field;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class DisplaySlot extends Slot {
	/**
	 * This is the slot we will show the contents of
	 */
	protected final @NotNull Slot inner;
	
	public DisplaySlot(@NotNull ItemStack stack) {
		this(new Slot(stack));
	}
	
	public DisplaySlot(@Nullable Slot inner) {
		this.inner = inner==null ? new Slot(Items.LIGHT_GRAY_STAINED_GLASS_PANE.getDefaultStack()) : inner;;
	}
	
	public void setContents(@Nullable ItemStack itemStack) { }
	
	public @NotNull ItemStack getContents() {
		return EMPTY;
	}
	
	// validation stuff
	
	public boolean canSetContents(@Nullable Field<Entity> entity, @NotNull ItemStack newContents) {
		return false;
	}
	
	// event stuff
	
	public void onAdded(@NotNull WrappedScreen to, int index) { }
	
	public void onRemoved(@NotNull WrappedScreen from, int index) { }
	
	public boolean onClicked(@NotNull WrappedScreen inventory, int index, @Nullable Field<Entity> entity) {
		return false;
	}
	
	// api stuff
	
	public @NotNull ItemStack getDisplayStack(@Nullable Field<Entity> entity) {
		return this.inner.getContents();
	}
	
	@Override
	public DisplaySlot clone() {
		return this; // due to the immutability of this slot we shouldn't need to create a new instance 
	}
	
}
