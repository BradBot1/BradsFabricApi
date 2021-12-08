package com.bb1.fabric.bfapi.mixins;

import java.util.HashSet;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.bb1.fabric.bfapi.nbt.mark.Markable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;

@Mixin(ItemStack.class)
public abstract class MarkableItemMixin implements Markable {
	
	@Shadow public abstract NbtCompound getOrCreateNbt();
	
	@Override
	public void applyMark(String mark) {
		if (hasMark(mark)) { return; }
		NbtCompound tag = getOrCreateNbt();
		NbtList list = tag.getList(MARK_TAG, NbtElement.STRING_TYPE);
		if (list==null) { list = new NbtList(); }
		list.add(NbtString.of(mark));
		tag.put(MARK_TAG, list);
	}

	@Override
	public boolean hasMark(String mark) {
		NbtCompound tag = getOrCreateNbt();
		for (NbtElement element : tag.getList(MARK_TAG, NbtElement.STRING_TYPE)) {
			if (element instanceof NbtString nbtString && mark.equals(element.asString())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Set<String> getMarks() {
		Set<String> marks = new HashSet<String>();
		NbtCompound tag = getOrCreateNbt();
		for (NbtElement element : tag.getList(MARK_TAG, NbtElement.STRING_TYPE)) {
			if (element instanceof NbtString nbtString) {
				marks.add(nbtString.asString());
			}
		}
		return marks;
	}
	
}
