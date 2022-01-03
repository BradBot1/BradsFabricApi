package com.bb1.fabric.bfapi.mixins;

import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.bb1.fabric.bfapi.nbt.mark.Markable;
import com.google.common.collect.Sets;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;

@Mixin(Entity.class)
public abstract class MarkableEntityMixin implements Markable {
	
	public Set<String> marks = Sets.newConcurrentHashSet();
	
	@Override
	public void applyMark(String mark) {
		this.marks.add(mark);
	}

	@Override
	public boolean hasMark(String mark) {
		return this.marks.contains(mark);
	}

	@Override
	public Set<String> getMarks() {
		return this.marks;
	}
	
	@Inject(method = "writeNbt", at = @At("RETURN"))
	public void writeMarksToNbt(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> callback) {
		NbtList marks = new NbtList();
		for (String mark : this.marks) {
			marks.add(NbtString.of(mark));
		}
		nbt.put(MARK_TAG, marks);
	}
	
	@Inject(method = "readNbt", at = @At("TAIL"))
	public void readMarksFromNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		this.marks.clear();
		for (NbtElement elem : nbt.getList(MARK_TAG, NbtElement.STRING_TYPE)) {
			if (elem instanceof NbtString nbtString) {
				this.marks.add(nbtString.asString());
			}
		}
	}
	
}
