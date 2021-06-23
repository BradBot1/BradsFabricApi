package com.bb1.api.datapacks;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

import com.bb1.api.Loader;

import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

public class BFAPIDatapackProvider implements DatapackProvider, ResourceReloader {

	public BFAPIDatapackProvider() { DatapackManager.getInstance().registerProvider(this); Loader.registerReloader(this); }
	
	@Override
	public String getProviderName() { return "BFAPIDatapackProvider"; }
	
	private Map<Identifier, DatapackHandler> handlers = new HashMap<Identifier, DatapackHandler>();

	@Override
	public void registerHandler(DatapackHandler datapackHandler) { this.handlers.put(datapackHandler.getIdentifier(), datapackHandler); }

	@Override
	public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
		CompletableFuture<Map<Identifier, Collection<Identifier>>> completableFuture = CompletableFuture.supplyAsync(new Supplier<Map<Identifier, Collection<Identifier>>>() {

			@Override
			public Map<Identifier, Collection<Identifier>> get() {
				Map<Identifier, Collection<Identifier>> map = new HashMap<Identifier, Collection<Identifier>>();
				for (Entry<Identifier, DatapackHandler> entry : handlers.entrySet()) {
					map.put(entry.getKey(), manager.findResources(entry.getValue().getPath(), (s)->s.endsWith(entry.getValue().getExtension())));
				}
				return map;
			}
			
		}, prepareExecutor);
		return completableFuture.thenCompose(synchronizer::whenPrepared).thenAcceptAsync(map -> {
			for (Entry<Identifier, Collection<Identifier>> entry : map.entrySet()) {
				handlers.get(entry.getKey()).reload(manager, entry.getValue());
			}
        }, applyExecutor);
	}

}
