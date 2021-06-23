package com.bb1.api.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.bb1.api.events.Events;
import com.bb1.api.events.Events.PlayerEvents.PlayerNBTReadEvent;
import com.bb1.api.events.Events.PlayerEvents.PlayerNBTWriteEvent;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Copyright 2021 BradBot_1
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@Mixin(ServerPlayerEntity.class)
public class PlayerNBTMixin {
	
	@Inject(method="readCustomDataFromNbt", at = @At("TAIL"))
	public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		Events.PlayerEvents.PLAYER_NBT_READ_EVENT.onEvent(new PlayerNBTReadEvent((ServerPlayerEntity)(Object)this, nbt));
	}
	
	@Inject(method="writeCustomDataToNbt", at = @At("HEAD"))
	public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		Events.PlayerEvents.PLAYER_NBT_WRITE_EVENT.onEvent(new PlayerNBTWriteEvent((ServerPlayerEntity)(Object)this, nbt));
	}

}
