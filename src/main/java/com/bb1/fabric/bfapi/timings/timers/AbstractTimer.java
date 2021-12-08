package com.bb1.fabric.bfapi.timings.timers;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

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
 * @author BradBot_1
 * 
 * A way to control timings
 * 
 * @apiNote All {@link AbstractTimer}s should be {@link Cloneable}
 */
public abstract class AbstractTimer implements Cloneable {
	
	protected final long creationTime;
	
	protected long finishTime;
	
	private boolean paused = false;
	/** Used to handle pausing correctly */
	private long pauseDelay = -1;
	
	private final long origionalDelay;
	
	public AbstractTimer(long delay) {
		this.origionalDelay = delay;
		this.creationTime = System.currentTimeMillis();
		this.finishTime = getTime()+delay;
	}
	
	public AbstractTimer(long delay, boolean paused) {
		this(delay);
		if (paused) {
			this.paused = paused;
			this.pauseDelay = delay;
		}
	}
	/**
	 * @apiNote should return 0 if expired
	 * 
	 * @return The time remaining on this timer
	 */
	public long getRemainingTime() {
		if (isPaused()) return this.pauseDelay;
		long time = finishTime-getTime();
		return (time<0) ? 0 : time;
	}
	/** @return returns the time remaining as {@link #getRemainingTime()}unit(s) */
	public @NotNull String getRemainingTimeAsFormattedString() { return buildUnitsOnTo(getRemainingTime()); }
	/** @return If the timer has expired/ran out */
	public boolean hasEnded() { return (isPaused()) ? false : getRemainingTime()<=0; }
	
	protected abstract @NotNull String buildUnitsOnTo(long number);
	
	protected abstract long getTime();
	
	public abstract long getRemainingTimeInMS();
	/** Restarts the timer */
	public AbstractTimer renew() {
		if (isPaused()) {
			this.pauseDelay = this.origionalDelay;
		} else {
			this.finishTime = getTime()+this.origionalDelay;
		}
		return this;
	}
	
	public boolean isPaused() { return this.paused; }
	
	public void pause() {
		if (isPaused() || this.pauseDelay>=0) return;
		this.pauseDelay = getRemainingTime();
		this.paused = true;
	}
	
	public void unpause() {
		if (!isPaused() || this.pauseDelay<0) return;
		this.finishTime = getTime()+this.pauseDelay;
		this.pauseDelay = -1;
		this.paused = false;
	}
	
	@Override
	public String toString() { return "Timer{remaining:"+getRemainingTimeAsFormattedString()+", created: "+this.creationTime+"}"; }
	
	@Override
	public boolean equals(Object obj) { // TODO: check more values
		return obj instanceof AbstractTimer && ((AbstractTimer)obj).hasEnded()==hasEnded() && ((AbstractTimer)obj).getRemainingTimeAsFormattedString().equals(getRemainingTimeAsFormattedString());
	}
	
	@Override
	public int hashCode() { return Objects.hash(this.creationTime, this.finishTime); }
	/**
	 * Creates a new {@link AbstractTimer} with the same delay etc
	 * 
	 * @return A clone of the timer
	 * 
	 * @apiNote The {@link #creationTime} will <b>not</b> be the same
	 */
	@Override
	public abstract @NotNull AbstractTimer clone();
	
}
