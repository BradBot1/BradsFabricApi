package com.bb1.fabric.bfapi.timings.timers;

import java.util.function.Function;
import java.util.function.Supplier;

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
 */
public final class CustomTimer extends AbstractTimer {
	
	private final @NotNull Supplier<@NotNull Long> timeProvider;
	
	private final @NotNull Function<@NotNull Long, @NotNull String> unitFunction;
	
	private final @NotNull Function<@NotNull Long, @NotNull Long> msFunction;
	
	public CustomTimer(final long delay, final @NotNull Supplier<@NotNull Long> timeProvider, final @NotNull Function<@NotNull Long, @NotNull String> unitFunction, final @NotNull Function<@NotNull Long, @NotNull Long> msFunction) {
		super(delay);
		this.timeProvider = timeProvider;
		this.unitFunction = unitFunction;
		this.msFunction = msFunction;
	}
	
	public CustomTimer(final long delay, final @NotNull Supplier<@NotNull Long> timeProvider, final @NotNull Function<@NotNull Long, @NotNull String> unitFunction, final @NotNull Function<@NotNull Long, @NotNull Long> msFunction, boolean paused) {
		super(delay, paused);
		this.timeProvider = timeProvider;
		this.unitFunction = unitFunction;
		this.msFunction = msFunction;
	}
	
	@Override
	protected @NotNull String buildUnitsOnTo(long number) { return unitFunction.apply(number); }

	@Override
	protected long getTime() { return timeProvider.get(); }

	@Override
	public @NotNull CustomTimer clone() { return new CustomTimer(getRemainingTime(), this.timeProvider, this.unitFunction, this.msFunction, isPaused()); }
	
	@Override
	public long getRemainingTimeInMS() { return msFunction.apply(getRemainingTime()); }
}
