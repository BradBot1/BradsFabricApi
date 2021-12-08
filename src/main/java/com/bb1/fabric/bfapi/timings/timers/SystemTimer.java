package com.bb1.fabric.bfapi.timings.timers;

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
public class SystemTimer extends AbstractTimer {

	public SystemTimer(long delay) { super(delay); }
	
	public SystemTimer(long delay, boolean paused) { super(delay, paused); }

	@Override
	protected @NotNull String buildUnitsOnTo(long number) { return number+"ms"; }

	@Override
	protected long getTime() { return System.currentTimeMillis(); }

	@Override
	public @NotNull SystemTimer clone() { return new SystemTimer(getRemainingTime(), isPaused()); }

	@Override
	public long getRemainingTimeInMS() { return getRemainingTime(); }
	
}
