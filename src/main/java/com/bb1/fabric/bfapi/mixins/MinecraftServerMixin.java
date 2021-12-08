package com.bb1.fabric.bfapi.mixins;

import java.util.function.BooleanSupplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.bb1.fabric.bfapi.GameObjects;
import com.bb1.fabric.bfapi.utils.Inputs.Input;

import net.minecraft.server.MinecraftServer;

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
@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
	
	@Inject(method = "runServer()V", at = @At(value = "HEAD"))
	public void serverRan(CallbackInfo callbackInfo) {
		GameObjects.setMinecraftServer((MinecraftServer) (Object) this);
		GameObjects.GameEvents.SERVER_START.emit(Input.of((MinecraftServer) (Object) this));
	}
	
	@Inject(method = "shutdown()V", at = @At("HEAD"))
	public void serverStopped(CallbackInfo callbackInfo) {
		GameObjects.GameEvents.SERVER_STOP.emit(Input.of((MinecraftServer) (Object) this));
	}
	
	@Inject(method = "tick(Ljava/util/function/BooleanSupplier;)V", at = @At("INVOKE"))
	public void onTick(BooleanSupplier shouldKeepTicking, CallbackInfo callbackInfo) {
		GameObjects.GameEvents.TICK.emit(Input.of((MinecraftServer) (Object) this));
	}

}
