package com.bb1.api.providers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.jetbrains.annotations.NotNull;

/**
 * Used to mark a class as a provider
 */
public interface Provider {
	/**
	 * The name of the provider
	 */
	@NotNull
	public String getProviderName();
	/**
	 * For if a provider needs to save data
	 * 
	 * @apiNote Used to instruct a provider to save its current data to its saving location
	 */
	public default void save() { }
	/**
	 * For if a provider needs to load
	 * 
	 * @apiNote Used to instruct a provider to delete its current data and load data from its saving location
	 */
	public default void load() { }
	
	public default Logger getProviderLogger() { return LogManager.getLogger("Provider | "+getProviderName()); }
}
