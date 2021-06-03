package com.bb1.api.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.bb1.api.Loader;
import com.bb1.api.events.Events;
import com.bb1.api.events.Events.AutoSaveEvent;
import com.bb1.api.events.Events.UnloadEvent;

import net.minecraft.server.MinecraftServer;

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
	
}
