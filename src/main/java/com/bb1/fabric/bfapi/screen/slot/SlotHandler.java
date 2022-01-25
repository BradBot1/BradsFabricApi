package com.bb1.fabric.bfapi.screen.slot;

import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.bb1.fabric.bfapi.screen.Screen;
import com.bb1.fabric.bfapi.utils.Field;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeMatcher;

public final class SlotHandler extends SimpleInventory {
	
	private Screen _inner;
	private final Slot[] _slots;
	
	public SlotHandler(@Nullable Slot fillerSlot, @NotNull Screen inner) {
		super(inner.getSize());
		this._slots = new Slot[inner.getSize()];
		fillerSlot = fillerSlot==null ? new Slot() : fillerSlot;
		for (int i = 0; i < inner.getSize(); i++) {
			this._slots[i] = fillerSlot.clone();
		}
		this._inner = inner;
	}
	
	public void setSlot(int index, @NotNull Slot slot) {
		if (index < 0 || index >= this._slots.length) { return; }
		this._slots[index].onRemoved(this._inner, index);
		this._slots[index] = slot.clone();
		slot.onAdded(this._inner, index);
	}
	/**
	 * This method is only here for the {@link WrappedSlot} implementation, to interact with slots in the intended way please see other methods
	 */
	@Internal
	public @NotNull Slot getSlot(int index) {
		return this._slots[index];
	}
	
	public boolean onClick(int index, @NotNull Entity entity) {
		return this.onClick(index, Field.of(entity));
	}
	
	public boolean onClick(int index, @Nullable Field<Entity> entity) {
		return this._slots[index].onClicked(this._inner, index, entity);
	}
	
	// getting an ItemStack
	
	public @NotNull ItemStack getStackAt(int index) {
		return this.getStackAt(index, (Field<Entity>) null); // we want to make sure we dont call the Entity method as that would field wrap a null
	}
	
	public @NotNull ItemStack getStackAt(int index, @NotNull Entity entity) {
		return this.getStackAt(index, Field.of(entity));
	}
	
	public @NotNull ItemStack getStackAt(int index, @Nullable Field<Entity> entity) {
		return this._slots[index].getDisplayStack(entity);
	}
	
	// setting an ItemStack
	
	public boolean setStackAt(int index, @NotNull ItemStack stack) {
		return this.setStackAt(index, stack, (Field<Entity>) null); // same as #getStackAt we shouldn't wrap a null as a field
	}
	
	public boolean setStackAt(int index, @NotNull ItemStack stack, @NotNull Entity entity) {
		return this.setStackAt(index, stack, Field.of(entity));
	}
	
	public boolean setStackAt(int index, @NotNull ItemStack stack, @Nullable Field<Entity> entity) {
		Slot slot = this._slots[index];
		if (!slot.canSetContents(entity, stack)) { return false; }
		slot.setContents(stack);
		return true;
	}
	
	public int getSize() {
		return this._slots.length;
	}
	
	public void onUpdate(long updateCounter) {
		for (Slot slot : this._slots) {
			slot.onUpdate(updateCounter);
		}
	}
	
	// minecraft inventory impl
	
	@Override
	public ItemStack getStack(int slot) {
		return this.getStackAt(slot, (Field<Entity>) null);
	}
	
	@Override
	public void setStack(int slot, ItemStack stack) {
		this.setStackAt(slot, stack, (Field<Entity>) null);
	}
	
	@Override
	public void clear() {
		for (Slot slot : this._slots) {
			if (slot.canSetContents(null, ItemStack.EMPTY)) {
				slot.setContents(ItemStack.EMPTY);
			}
		}
	}
	
	@Override
	public boolean canInsert(ItemStack stack) {
        for (Slot slot : this._slots) {
        	if (!slot.canSetContents(null, stack)) { continue; }
        	ItemStack itemStack = slot.getContents();
            if (!itemStack.isEmpty() && (!ItemStack.canCombine(itemStack, stack) || itemStack.getCount() >= itemStack.getMaxCount())) continue;
            return true;
        }
        return false;
	}
	
	@Override
	public boolean isValid(int slot, ItemStack stack) {
		return this._slots[slot].canSetContents(null, stack) && super.isValid(slot, stack);
	}
	
	@Override
	public void provideRecipeInputs(RecipeMatcher finder) { }
	
	@Override
	public ItemStack addStack(ItemStack stack) {
		ItemStack itemStack = stack.copy();
        for (Slot slot : this._slots) {
        	if (!slot.canSetContents(null, itemStack)) { continue; }
        	if (slot.getContents().isEmpty()) {
        		slot.setContents(itemStack);
        		return ItemStack.EMPTY;
        	}
        	if (!ItemStack.canCombine(itemStack, slot.getContents())) { continue; }
        	ItemStack slotStack = slot.getContents();
        	while (slotStack.getCount() < slotStack.getMaxCount() && itemStack.getCount() > 0) {
        		slotStack.increment(1);
        		itemStack.decrement(1);
        	}
        }
        return itemStack;
	}
	
	@Override
	public ItemStack removeItem(Item item, int count) {
		ItemStack returnItem = new ItemStack(item, 0);
		for (Slot slot : this._slots) {
			if (!slot.canSetContents(null, ItemStack.EMPTY)) { continue; }
			ItemStack slotStack = slot.getContents();
			if (!slotStack.isOf(item)) { continue; }
			while (slotStack.getCount() > 0 && returnItem.getCount() < count) {
        		slotStack.decrement(1);
        		returnItem.increment(1);
        	}
		}
		return returnItem;
	}
	
	@Override
	public ItemStack removeStack(int index) {
		Slot slot = this._slots[index];
		if (!slot.canSetContents(null, ItemStack.EMPTY)) { return ItemStack.EMPTY; }
		ItemStack buffer = slot.getContents();
		slot.setContents(ItemStack.EMPTY);
		return buffer;
	}
	
	@Override
	public ItemStack removeStack(int index, int amount) {
		Slot slot = this._slots[index];
		if (!slot.canSetContents(null, ItemStack.EMPTY)) { return ItemStack.EMPTY; }
		ItemStack buffer = slot.getContents();
		ItemStack returnStack = buffer.copy();
		returnStack.setCount(0);
		while (buffer.getCount() > 0 && buffer.getCount() < amount) {
    		buffer.decrement(1);
    		returnStack.increment(1);
    	}
		return returnStack;
	}
	
}
