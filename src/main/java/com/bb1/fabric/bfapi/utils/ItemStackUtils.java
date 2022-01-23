package com.bb1.fabric.bfapi.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public final class ItemStackUtils {
	
	public static final ItemStack clone(ItemStack itemStack) {
		return ItemStack.fromNbt(itemStack.writeNbt(new NbtCompound()));
	}
	
}
