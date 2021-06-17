package com.bb1.api.providers;

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import com.bb1.api.events.Events;
import com.bb1.api.events.Events.ProviderInformationEvent;

/**
 * Copyright 2021 BradBot_1
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
	
	public default <T> Collection<T> callInfoEventAndGet(Class<T> objectsType) {
		ProviderInformationEvent event = new ProviderInformationEvent(this);
		Events.PROVIDER_INFO_EVENT.onEvent(event);
		return event.get(objectsType);
	}
	
}
