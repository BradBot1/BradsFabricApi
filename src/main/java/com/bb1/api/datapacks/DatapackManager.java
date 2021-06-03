package com.bb1.api.datapacks;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

import com.bb1.api.events.Events;
import com.bb1.api.events.Events.ReloadEvent;

import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloadListener;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

public final class DatapackManager implements ResourceReloadListener {

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
			Events.RELOAD_EVENT.onEvent(new ReloadEvent());
        }, applyExecutor);
	}

}
