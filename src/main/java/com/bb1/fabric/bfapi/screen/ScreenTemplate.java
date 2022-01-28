package com.bb1.fabric.bfapi.screen;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jetbrains.annotations.NotNull;

import com.bb1.fabric.bfapi.screen.slot.WrappedSlot;
import com.bb1.fabric.bfapi.screen.slot.WrappedSlot.ClickHandler;
import com.bb1.fabric.bfapi.utils.Inputs.TriInput;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;

public final class ScreenTemplate {
	
	private final Map<Integer, TriInput<ItemStack, ClickHandler, Boolean>> _map = new HashMap<Integer, TriInput<ItemStack, ClickHandler, Boolean>>(); // not null, null, not null
	
	public ScreenTemplate setStack(int index, ItemStack value) {
		if (!this._map.containsKey(index)) {
			this._map.put(index, TriInput.of(value, null, false));
			return this;
		}
		final TriInput<ItemStack, ClickHandler, Boolean> currentValue = this._map.get(index);
		this._map.put(index, TriInput.of(value, currentValue.getSecond(), currentValue.getThird()));
		return this;
	}
	
	public ScreenTemplate setHandler(int index, ClickHandler value) {
		if (!this._map.containsKey(index)) {
			this._map.put(index, TriInput.of(ItemStack.EMPTY, value, false));
			return this;
		}
		final TriInput<ItemStack, ClickHandler, Boolean> currentValue = this._map.get(index);
		this._map.put(index, TriInput.of(ItemStack.EMPTY, value, currentValue.getThird()));
		return this;
	}
	
	public ScreenTemplate removeHandler(int index) {
		return this.setHandler(index, null);
	}
	
	public ScreenTemplate setLocked(int index, boolean value) {
		if (!this._map.containsKey(index)) {
			this._map.put(index, TriInput.of(ItemStack.EMPTY, null, value));
			return this;
		}
		final TriInput<ItemStack, ClickHandler, Boolean> currentValue = this._map.get(index);
		this._map.put(index, TriInput.of(currentValue.get(), currentValue.getSecond(), value));
		return this;
	}
	
	public ScreenTemplate lock(int index) {
		return this.setLocked(index, true);
	}
	
	public ScreenTemplate unlock(int index) {
		return this.setLocked(index, false);
	}
	
	public ScreenHandler applyTo(@NotNull ScreenHandler handler) {
		for (Entry<Integer, TriInput<ItemStack, ClickHandler, Boolean>> entry : this._map.entrySet()) {
			final WrappedSlot slot = WrappedSlot.getWrappedSlot(handler.getSlot(entry.getKey()));
			final ItemStack is = entry.getValue().get();
			if (is!=null && !is.isEmpty()) { slot.setStack(is); } else { slot.setStack(ItemStack.EMPTY); }
			if (entry.getValue().getThird()) { slot.lock(); } else { slot.unlock(); }
			slot.setClickHandler(entry.getValue().getSecond());
		}
		return handler;
	}
	
	// funky template junk
	
	public ScreenTemplate display(int index, ItemStack displayItem) {
		if (!this._map.containsKey(index)) {
			this._map.put(index, TriInput.of(displayItem, null, true));
			return this;
		}
		final TriInput<ItemStack, ClickHandler, Boolean> currentValue = this._map.get(index);
		this._map.put(index, TriInput.of(displayItem, currentValue.getSecond(), true));
		return this;
	}
	
	public ScreenTemplate addBorder(ItemStack borderStack, int rows) {
		switch(rows) {
			case 2:
				display(9, borderStack);
				display(17, borderStack);
			case 1:
				display(0, borderStack);
				display(8, borderStack);
				return this;
			case 6:
				display(36, borderStack);
				display(44, borderStack);
			case 5:
				display(27, borderStack);
				display(35, borderStack);
			case 4:
				display(18, borderStack);
				display(26, borderStack);
			case 3:
				display(9, borderStack);
				display(17, borderStack);
		}
		for (int i = 0; i < 9; i++) {
			display(i, borderStack);
		}
		for (int i = ((rows - 1) * 9); i < (rows * 9); i++) {
			display(i, borderStack);
		}
		return this;
	}
	
	public ScreenTemplate removeBorder(int rows) {
		return addBorder(ItemStack.EMPTY, rows);
	}
	
}
