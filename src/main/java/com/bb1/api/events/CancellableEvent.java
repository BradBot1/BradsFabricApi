package com.bb1.api.events;
/**
 * For showing that the event can be cancelled
 */
public interface CancellableEvent {
	/**
	 * If the event has been cancelled
	 */
	public boolean isCancelled();
	/**
	 * Cancels the event
	 */
	public void cancel();
}