package com.bb1.api.datapacks;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

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
public final class DatapackManager implements ResourceReloader {

	private static final DatapackManager INSTANCE = new DatapackManager();

	public static DatapackManager get() { return INSTANCE; }

	private Map<Identifier, DatapackAddon> addons = new HashMap<Identifier, DatapackAddon>();

	private DatapackManager() { }

	public void register(DatapackAddon addon) { this.addons.put(addon.getIdentifier(), addon); }

	@Override
	public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
		CompletableFuture<Map<Identifier, Collection<Identifier>>> completableFuture = CompletableFuture.supplyAsync(new Supplier<Map<Identifier, Collection<Identifier>>>() {

			@Override
			public Map<Identifier, Collection<Identifier>> get() {
				Map<Identifier, Collection<Identifier>> map = new HashMap<Identifier, Collection<Identifier>>();
				for (Entry<Identifier, DatapackAddon> entry : addons.entrySet()) {
					map.put(entry.getKey(), manager.findResources(entry.getValue().getPath(), (s)->s.endsWith(entry.getValue().getExtension())));
				}
				return map;
			}
			
		}, prepareExecutor);
		return completableFuture.thenCompose(synchronizer::whenPrepared).thenAcceptAsync(map -> {
			for (Entry<Identifier, Collection<Identifier>> entry : map.entrySet()) {
				addons.get(entry.getKey()).reload(manager, entry.getValue());
			}
			// Events.RELOAD_EVENT.onEvent(new ReloadEvent());
        }, applyExecutor);
	}

}
