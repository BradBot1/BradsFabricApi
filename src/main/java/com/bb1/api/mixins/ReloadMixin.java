package com.bb1.api.mixins;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.bb1.api.datapacks.DatapackManager;

import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.registry.DynamicRegistryManager;

@Mixin(ServerResourceManager.class)
public class ReloadMixin {

    @Shadow @Final public ReloadableResourceManager resourceManager;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void addReloadHandlers(DynamicRegistryManager registryManager, CommandManager.RegistrationEnvironment commandEnvironment, int functionPermissionLevel, CallbackInfo ci) {
        resourceManager.registerReloader(DatapackManager.get());
    }
}