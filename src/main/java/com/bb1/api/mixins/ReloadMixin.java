package com.bb1.api.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.bb1.api.datapacks.DatapackManager;

import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.server.command.CommandManager;

@Mixin(ServerResourceManager.class)
public class ReloadMixin {

    @Shadow private ReloadableResourceManager resourceManager;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void addServerLanguageManager(CommandManager.RegistrationEnvironment registrationEnvironment, int i, CallbackInfo ci) {
        this.resourceManager.registerListener(DatapackManager.get());
    }
}