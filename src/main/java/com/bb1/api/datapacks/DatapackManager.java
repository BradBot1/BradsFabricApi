package com.bb1.api.datapacks;

import java.util.HashSet;
import java.util.Set;

import com.bb1.api.managers.AbstractManager;

public class DatapackManager extends AbstractManager<DatapackProvider> {

	private static final DatapackManager INSTANCE = new DatapackManager();
	
	public static DatapackManager getInstance() { return INSTANCE; }
	
	private DatapackManager() { }
	
	private Set<DatapackHandler> datapacks = new HashSet<DatapackHandler>();
	
	public void registerHandler(DatapackHandler datapackHandler) {
		this.datapacks.add(datapackHandler);
		for (DatapackProvider datapackProvider : getProviders()) {
			datapackProvider.registerHandler(datapackHandler);
		}
	}
	
	@Override
	public void registerProvider(DatapackProvider provider) {
		for (DatapackHandler datapackHandler : this.datapacks) {
			provider.registerHandler(datapackHandler);
		}
		super.registerProvider(provider);
	}

}
