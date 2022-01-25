package com.bb1.fabric.bfapi.screen.chest;

import org.jetbrains.annotations.NotNull;

import com.bb1.fabric.bfapi.screen.slot.DisplaySlot;
import com.bb1.fabric.bfapi.screen.slot.SlotHandler;

import net.minecraft.item.ItemStack;

public class BorderedChestScreen extends ChestScreen {
	
	private final ItemStack borderItem;
	
	public BorderedChestScreen(final int rows, ItemStack stack) {
		super(rows);
		this.borderItem = stack.copy();
	}

	@Override
	public void buildScreen(@NotNull SlotHandler slotHandler) {
		DisplaySlot slot = new DisplaySlot(this.borderItem);
		switch(this.getRows()) {
			case 2:
				slotHandler.setSlot(9, slot);
				slotHandler.setSlot(17, slot);
			case 1:
				slotHandler.setSlot(0, slot);
				slotHandler.setSlot(8, slot);
				return;
			case 6:
				slotHandler.setSlot(36, slot);
				slotHandler.setSlot(44, slot);
			case 5:
				slotHandler.setSlot(27, slot);
				slotHandler.setSlot(35, slot);
			case 4:
				slotHandler.setSlot(18, slot);
				slotHandler.setSlot(26, slot);
			case 3:
				slotHandler.setSlot(9, slot);
				slotHandler.setSlot(17, slot);
		}
		for (int i = 0; i < 9; i++) {
			slotHandler.setSlot(i, slot);
		}
		for (int i = ((this.getRows() - 1) * 9); i < (this.getRows() * 9); i++) {
			slotHandler.setSlot(i, slot);
		}
	}
	
}
