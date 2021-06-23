package com.bb1.api.datapacks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bb1.api.providers.Provider;

public interface DatapackProvider extends Provider {
	
	public void registerHandler(DatapackHandler datapackHandler);
	
	@Override
	public default Logger getProviderLogger() { return LogManager.getLogger("DatapackProvider | "+getProviderName()); }
	
}
