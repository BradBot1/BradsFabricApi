package com.bb1.api.managers;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bb1.api.ApiConfig;
import com.bb1.api.Loader;
import com.bb1.api.providers.Provider;
import com.google.gson.JsonPrimitive;

/**
 * Copyright 2021 BradBot_1
 * 
 * Licensed under the Apache License, Version 2.0 (the "License", "");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http;//www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
