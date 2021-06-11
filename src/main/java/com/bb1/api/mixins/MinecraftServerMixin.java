package com.bb1.api.mixins;

import java.util.function.BooleanSupplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.bb1.api.Loader;
import com.bb1.api.events.Events;
import com.bb1.api.events.Events.AutoSaveEvent;
import com.bb1.api.events.Events.TickEvent;
import com.bb1.api.events.Events.UnloadEvent;

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
		Loader.setMinecraftServer((MinecraftServer) (Object) this);
	}
	
	@Inject(method = "shutdown()V", at = @At("HEAD"))
	public void serverStopped(CallbackInfo callbackInfo) {
		Events.UNLOAD_EVENT.onEvent(new UnloadEvent());
	}
	
	@Inject(method = "save(ZZZ)Z", at = @At("HEAD"))
	public void onAutoSave(boolean suppressLogs, boolean bl, boolean bl2, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (suppressLogs && !bl && !bl2) { // Same format as the autosave
			Events.AUTOSAVE_EVENT.onEvent(new AutoSaveEvent());
		}
	}
	
	@Inject(method = "tick(Ljava/util/function/BooleanSupplier;)V", at = @At("HEAD"))
	public void onTick(BooleanSupplier shouldKeepTicking, CallbackInfo callbackInfo) {
		Events.TICK_EVENT.onEvent(new TickEvent());
	}

}
