package com.bb1.api.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.bb1.api.Loader;

import net.minecraft.server.command.CommandManager;

@Mixin(CommandManager.class)
public class CommandMangerMixin {

	@Inject(method = "<init>", at = @At("TAIL"))
	public void onInstanceMade(CommandManager.RegistrationEnvironment environment, CallbackInfo callbackInfo) {
		Loader.setCommandManager((CommandManager)(Object)this);
	}

}
