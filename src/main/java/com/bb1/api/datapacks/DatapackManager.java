package com.bb1.api.datapacks;

import com.bb1.api.managers.AbstractInputtableManager;

public class DatapackManager extends AbstractInputtableManager<DatapackProvider, DatapackHandler> {

	private static final DatapackManager INSTANCE = new DatapackManager();
	
	public static DatapackManager getInstance() { return INSTANCE; }
	
	private DatapackManager() { }
	
	@Override
	protected void onInput(DatapackHandler input) {
		for (DatapackProvider datapackProvider : getProviders()) {
			datapackProvider.registerHandler(input);
		}
	}
	
	@Override
	protected void onRegister(DatapackProvider provider) {
		for (DatapackHandler datapackHandler : getInput()) {
			provider.registerHandler(datapackHandler);
		}
	}

}
