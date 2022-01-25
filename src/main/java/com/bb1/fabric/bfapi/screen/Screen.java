package com.bb1.fabric.bfapi.screen;

import org.jetbrains.annotations.NotNull;

import com.bb1.fabric.bfapi.screen.slot.SlotHandler;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;

public interface Screen {
	
	public int getSize();
	
	public void buildScreen(@NotNull SlotHandler slotHandler);
	
	public default NamedScreenHandlerFactory toNamedScreenHandlerFactory(@NotNull Text text) {
		return new SimpleNamedScreenHandlerFactory((syncId,pInv,player)->{
			WrappedScreen ws = this.toWrappedScreen(syncId, pInv);
			this.buildScreen(ws.getSlotHandler());
			return ws.asHandler();
		}, text);
	}
	
	public WrappedScreen toWrappedScreen(int syncId, @NotNull PlayerInventory playerInventory);
	
}
