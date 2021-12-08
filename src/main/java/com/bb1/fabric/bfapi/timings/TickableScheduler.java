package com.bb1.fabric.bfapi.timings;

import org.jetbrains.annotations.NotNull;

import com.bb1.fabric.bfapi.timings.timers.AbstractTimer;

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
 * A tick based implementation of {@link IScheduler} where checks on tasks are only ran during a tick
 * 
 * @apiNote Requires external ticking mechanism to call {@link #tick()}
 * 
 */
public class TickableScheduler implements IScheduler {
	
	private boolean doingTick = false;
	
	private final TaskSet taskSet = new TaskSet();
	
	public TickableScheduler() { }
	
	@Override
	public void schedule(@NotNull ITask task, @NotNull AbstractTimer timer) {
		this.taskSet.put(task, timer);
	}

	@Override
	public void runTask(@NotNull ITask task) {
		try {
			task.runTask();
		} catch (Throwable e) {
			// TODO Log errors
		}
	}
	
	public synchronized void tick() {
		if (this.doingTick) return; // Cannot do more than one tick at a time
		this.doingTick = true;
		for (ITask task : taskSet.getReadyTasks()) {
			runTask(task);
		}
		this.doingTick = false;
	}

}
