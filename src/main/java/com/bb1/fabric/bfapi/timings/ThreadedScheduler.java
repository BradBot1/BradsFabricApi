package com.bb1.fabric.bfapi.timings;

import java.util.HashSet;
import java.util.Set;

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
 * A multi-threaded implementation of {@link IScheduler} where each task in ran on a separate thread
 * 
 */
public class ThreadedScheduler implements IScheduler {
	
	private static volatile short uniqueMainThreadID = 0;
	
	private volatile short uniqueTaskThreadID = 0;
	
	private final TaskSet taskSet = new TaskSet();
	
	private boolean stopped = false;
	
	private final Set<Thread> taskThreads = new HashSet<Thread>();
	
	private final Thread handlingThread = new Thread(()->{
		do {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) { }
			for (ITask task : taskSet.getReadyTasks()) {
				runTask(task);
			}
		} while (!stopped);
	}, "ThreadedScheduler-"+(uniqueMainThreadID++));
	
	public ThreadedScheduler() {
		this.handlingThread.start();
	}
	
	@Override
	public void schedule(@NotNull ITask task, @NotNull AbstractTimer timer) {
		if (this.stopped) throw new IllegalStateException("Cannot add a task when the handling thread is stopped!");
		this.taskSet.put(task, timer);
	}

	@Override
	public synchronized void runTask(@NotNull ITask task) {
		final Thread thread = new Thread(task, this.handlingThread.getName()+"-"+(this.uniqueTaskThreadID++));
		taskThreads.add(thread);
		thread.start();
	}
	
	@SuppressWarnings("deprecation")
	public void stop() {
		if (this.stopped) return;
		for (Thread thread : taskThreads) {
			try {
				thread.stop();
			} catch (Throwable e) { }
		}
		this.stopped = true;
	}

}
