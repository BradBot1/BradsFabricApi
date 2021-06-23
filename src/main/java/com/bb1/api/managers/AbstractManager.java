package com.bb1.api.managers;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bb1.api.providers.Provider;

public abstract class AbstractManager<P extends Provider> {
	
	private final Set<P> providers = new HashSet<P>();

	protected AbstractManager() { }
	
	public void registerProvider(P provider) { this.providers.add(provider); }
	
	public Set<P> getProviders() { return this.providers; }
	
	protected Logger getLogger() { return LogManager.getLogger("Manager | "+getClass().getName()); }

}
