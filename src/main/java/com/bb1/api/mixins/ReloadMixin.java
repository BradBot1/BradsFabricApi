package com.bb1.api.mixins;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.bb1.api.datapacks.DatapackManager;
import com.bb1.api.events.Events;
import com.bb1.api.events.Events.ReloadEvent;

import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.DynamicRegistryManager;

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
@Mixin(ServerResourceManager.class)
public class ReloadMixin {

    @Shadow @Final public ReloadableResourceManager resourceManager;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void addReloadHandlers(DynamicRegistryManager registryManager, CommandManager.RegistrationEnvironment commandEnvironment, int functionPermissionLevel, CallbackInfo ci) {
        resourceManager.registerReloader(DatapackManager.get());
        resourceManager.registerReloader(new ReloadHandler());
    }
    
    public static final class ReloadHandler implements ResourceReloader {

		@Override
		public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
			return CompletableFuture.supplyAsync(() -> null, prepareExecutor).thenCompose(synchronizer::whenPrepared).thenAcceptAsync(nul -> Events.RELOAD_EVENT.onEvent(new ReloadEvent()), applyExecutor);
		}
    	
    }
}