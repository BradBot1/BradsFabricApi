package com.bb1.fabric.bfapi.mixins;

import static com.bb1.fabric.bfapi.events.MinecraftEvents.COMMAND_REGISTRATION_EVENT;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.bb1.fabric.bfapi.utils.Inputs.DualInput;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

@Mixin(CommandManager.class)
public abstract class CommandRegistrationMixin {
	
	public abstract @Shadow CommandDispatcher<ServerCommandSource> getDispatcher();

	@Inject(method = "<init>", at = @At("TAIL"))
	public void onInstanceMade(CommandManager.RegistrationEnvironment environment, CallbackInfo callbackInfo) {
		COMMAND_REGISTRATION_EVENT.emit(DualInput.of(getDispatcher(), environment));
	}

}

