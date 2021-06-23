package com.bb1.api.events;

import java.util.HashSet;
import java.util.Set;

import com.bb1.api.events.Event.EventHandler;
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
public class Event<E> extends net.fabricmc.fabric.api.event.Event<EventHandler<E>> {
	
	public static Event<Void> createEmptyEvent() { return new Event<Void>(); }

	private final Set<EventHandler<E>> listeners = new HashSet<EventHandler<E>>();

	public Event() {
		this.invoker = new EventInvoker();
	}

	@Override
	public void register(EventHandler<E> listener) {
		listeners.add(listener);
	}
	
	public void onEvent(E event) {
		invoker.onEvent(event);
	}
	/**
	 * A simple interface to handle events
	 */
	@FunctionalInterface
	public static interface EventHandler<E> { public void onEvent(E event); }
	
	protected class EventInvoker implements EventHandler<E> {

		@Override
		public void onEvent(E event) {
			for (EventHandler<E> listener : listeners) {
				if (event instanceof CancellableEvent && ((CancellableEvent)event).isCancelled()) break; // Cancelled so no more handling
				listener.onEvent(event);
			}
		}
		
	}
	
}
