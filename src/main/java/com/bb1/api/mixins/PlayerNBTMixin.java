package com.bb1.api.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.bb1.api.events.Events;
import com.bb1.api.events.Events.PlayerNBTReadEvent;
import com.bb1.api.events.Events.PlayerNBTWriteEvent;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(ServerPlayerEntity.class)
public class PlayerNBTMixin {
	
	@Inject(method="readCustomDataFromNbt", at = @At("TAIL"))
	public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		Events.PLAYER_NBT_READ_EVENT.onEvent(new PlayerNBTReadEvent((ServerPlayerEntity)(Object)this, nbt));
	}
	
	@Inject(method="writeCustomDataToNbt", at = @At("HEAD"))
	public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		Events.PLAYER_NBT_WRITE_EVENT.onEvent(new PlayerNBTWriteEvent((ServerPlayerEntity)(Object)this, nbt));
	}

}
