package com.bb1.api.adapters;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import com.bb1.api.Loader;

import net.fabricmc.loader.api.FabricLoader;

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
/**
 * @apiNote All adapters are expected to also be providers
 */
public abstract class AbstractAdapter {
	
	private static final Set<AbstractAdapter> LOADED_ADAPTERS = new HashSet<AbstractAdapter>();
	
	public static Set<AbstractAdapter> getLoadedAdapters() { return LOADED_ADAPTERS; }
	
	private static final Logger LOGGER = LogManager.getLogger("Adapter");
	
	private static final FabricLoader FABRIC_LOADER = FabricLoader.getInstance();
	/** The mod id that this addapter works alongside */
	private final String mod;
	
	protected AbstractAdapter(@NotNull String mod) {
		this.mod = mod;
		if (canLoad()) {
			if (Loader.CONFIG.debugMode) LOGGER.info("Loaded the adapter for the mod "+getRequiredModID());
			load();
			LOADED_ADAPTERS.add(this);
		}
	}
	
	public final String getRequiredModID() { return this.mod; }
	/** If the adapter can load */
	public final boolean canLoad() { return FABRIC_LOADER!=null && FABRIC_LOADER.isModLoaded(getRequiredModID()); }
	/** called if the adapter can load */
	protected abstract void load();
	
	// Provider stuff
	
	public String getProviderName() { return getRequiredModID(); }
}
