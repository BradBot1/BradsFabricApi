package com.bb1.fabric.bfapi.screen.chest;

import org.jetbrains.annotations.NotNull;

import com.bb1.fabric.bfapi.screen.Screen;
import com.bb1.fabric.bfapi.screen.WrappedScreen;
import com.bb1.fabric.bfapi.screen.slot.SlotHandler;

import net.minecraft.entity.player.PlayerInventory;

public class ChestScreen implements Screen {
	
	private final int rows;
	
	public ChestScreen(final int rows) {
		this.rows = rows;
	}
	
	public int getRows() {
		return this.rows;
	}

	@Override
	public WrappedScreen toWrappedScreen(int syncId, PlayerInventory playerInventory) {
		return new WrappedChestScreen(syncId, playerInventory, this);
	}

	@Override
	public void buildScreen(@NotNull SlotHandler slotHandler) { }

	@Override
	public int getSize() {
		return this.rows * 9;
	}
	
}
