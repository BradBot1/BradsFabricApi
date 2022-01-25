package com.bb1.fabric.bfapi.screen.chest;

import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

import com.bb1.fabric.bfapi.screen.WrappedScreen;
import com.bb1.fabric.bfapi.screen.slot.Slot;
import com.bb1.fabric.bfapi.screen.slot.SlotHandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.SlotActionType;

@Internal
final class WrappedChestScreen extends GenericContainerScreenHandler implements WrappedScreen {
	
	@Internal
	public static final ScreenHandlerType<?> convertSize(int size) {
		switch (size) {
		case 0:
			return ScreenHandlerType.GENERIC_3X3;
		case 1:
			return ScreenHandlerType.GENERIC_9X1;
		case 2:
			return ScreenHandlerType.GENERIC_9X2;
		case 3:
			return ScreenHandlerType.GENERIC_9X3;
		case 4:
			return ScreenHandlerType.GENERIC_9X4;
		case 5:
			return ScreenHandlerType.GENERIC_9X5;
		case 6:
			return ScreenHandlerType.GENERIC_9X6;
		default:
			return ScreenHandlerType.GENERIC_9X1;
		}
	}
	
	private final SlotHandler slotHandler;
	private long updateCounter = 0;
	
	WrappedChestScreen(int syncId, PlayerInventory playerInventory, ChestScreen inner) {
		super(convertSize(inner.getRows()), syncId, playerInventory, new SlotHandler(new Slot(), inner), inner.getRows());
		this.slotHandler = (SlotHandler) this.getInventory();
	}

	@Override
	public int getSize() {
		return this.getRows() * 9;
	}

	@Override
	public @NotNull SlotHandler getSlotHandler() {
		return this.slotHandler;
	}
	
	@Override
	public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
		if (slotIndex >= 0 && slotIndex < this.slotHandler.getSize()) {
			this.slotHandler.onClick(slotIndex, player);
		}
		super.onSlotClick(slotIndex, button, actionType, player);
	}
	
	@Override
	public boolean canInsertIntoSlot(ItemStack stack, net.minecraft.screen.slot.Slot slot) {
		return this.slotHandler.getSlot(slot.getIndex()).canSetContents(null, stack);
	}
	
	@Override
	public boolean canInsertIntoSlot(net.minecraft.screen.slot.Slot slot) {
		return this.slotHandler.getSlot(slot.getIndex()).canSetContents(null, ItemStack.EMPTY);
	}
	
	@Override
	public void sendContentUpdates() {
		this.slotHandler.onUpdate(this.updateCounter++);
		super.sendContentUpdates();
	}
	
}
