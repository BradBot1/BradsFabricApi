package com.bb1.fabric.bfapi.screen;

import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

import com.bb1.fabric.bfapi.screen.slot.SlotHandler;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

@Internal
public interface WrappedScreen {
	
	public int getSize();
	
	public @NotNull SlotHandler getSlotHandler();
	
	public ScreenHandlerType<?> getType();
	
	public default ScreenHandler asHandler() {
		return (ScreenHandler) (Object) this; // since all implementations of this interface should be a handler we can cast
	}
	
}
