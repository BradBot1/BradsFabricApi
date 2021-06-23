package com.bb1.api.managers;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bb1.api.ApiConfig;
import com.bb1.api.Loader;
import com.bb1.api.providers.Provider;
import com.google.gson.JsonPrimitive;

public abstract class AbstractManager<P extends Provider> {
	
	private final Set<P> providers = new HashSet<P>();
	
	private ApiConfig config = Loader.CONFIG;

	protected AbstractManager() { }
	
	public void registerProvider(P provider) {
		if (config.blockedProviders.contains(new JsonPrimitive(provider.getProviderName()))) {
			if (config.debugMode) getLogger().warn("Unable to accept "+provider.getProviderName()+" as it has been disabled");
			return;
		}
		this.providers.add(provider);
		onRegister(provider);
	}
	
	protected abstract void onRegister(P provider);
	
	public final Set<P> getProviders() { return this.providers; }
	
	protected Logger getLogger() { return LogManager.getLogger("Manager | "+getClass().getName()); }

}
