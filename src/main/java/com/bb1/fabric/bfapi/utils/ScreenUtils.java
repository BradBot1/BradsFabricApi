package com.bb1.fabric.bfapi.utils;

import java.util.function.BiFunction;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerFactory;
import net.minecraft.text.Text;

public final class ScreenUtils {
	
	public static final ScreenHandlerFactory buildScreenHandlerFactory(BiFunction<Integer, PlayerInventory, ScreenHandler> generator) {
		return (syncId, inv, player)->generator.apply(syncId, inv);
	}
	
	public static final NamedScreenHandlerFactory nameScreenHandlerFactory(Text name, ScreenHandlerFactory factory) {
		return new NamedScreenHandlerFactory() {

			@Override
			public ScreenHandler createMenu(int var1, PlayerInventory var2, PlayerEntity var3) {
				return factory.createMenu(var1, var2, var3);
			}

			@Override
			public Text getDisplayName() {
				return name;
			}
			
		};
	}
	
}
