package com.bb1.fabric.bfapi.screen.slot;

import static net.minecraft.item.ItemStack.EMPTY;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.bb1.fabric.bfapi.screen.Screen;
import com.bb1.fabric.bfapi.utils.Field;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public class Slot implements Cloneable {
	
	protected @NotNull ItemStack contents = EMPTY;
	
	public Slot() { }
	
	public Slot(@NotNull ItemStack contents) {
		this.contents = contents;
	}
	
	public void setContents(@NotNull ItemStack newContents) {
		this.contents = newContents;
	}
	
	public @NotNull ItemStack getContents() {
		return this.contents;
	}
	
	// validation stuff
	
	public boolean canSetContents(@Nullable Field<Entity> entity, @NotNull ItemStack newContents) {
		return true;
	}
	
	// event stuff
	
	public void onAdded(@NotNull Screen screen, int index) { }
	
	public void onRemoved(@NotNull Screen screen, int index) { }
	
	public boolean onClicked(@NotNull Screen screen, int index, @Nullable Field<Entity> entity) {
		return true;
	}
	
	// api stuff
	
	public @NotNull ItemStack getDisplayStack(@Nullable Field<Entity> entity) {
		return this.contents;
	}
	
	public void onUpdate(long updateCounter) { }
	
	@Override
	public Slot clone() {
		return new Slot(this.contents.copy());
	}
	
}
