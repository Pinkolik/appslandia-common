// The MIT License (MIT)
// Copyright © 2015 AppsLandia. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.appslandia.common.threading;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class TaskScheduler<T> {

	protected final boolean cancelMayInterruptIfRunning;

	protected final ScheduledExecutorService executor;
	protected final AtomicReference<ScheduledFuture<?>> taskRef = new AtomicReference<>();

	public TaskScheduler() {
		this(false);
	}

	public TaskScheduler(boolean cancelMayInterruptIfRunning) {
		this.cancelMayInterruptIfRunning = cancelMayInterruptIfRunning;
		this.executor = Executors.newSingleThreadScheduledExecutor();
	}

	public TaskScheduler(boolean cancelMayInterruptIfRunning, ThreadFactory threadFactory) {
		this.cancelMayInterruptIfRunning = cancelMayInterruptIfRunning;
		this.executor = Executors.newSingleThreadScheduledExecutor(threadFactory);
	}

	public TaskScheduler(boolean cancelMayInterruptIfRunning, ScheduledExecutorService executor) {
		this.cancelMayInterruptIfRunning = cancelMayInterruptIfRunning;
		this.executor = executor;
	}

	public void scheduleAtFixedRate(Task<T> task, long initialDelay, long period, TimeUnit unit) {
		cancel();
		this.taskRef.set(this.executor.scheduleAtFixedRate(task, initialDelay, period, unit));
	}

	public void scheduleWithFixedDelay(Task<T> task, long initialDelay, long delay, TimeUnit unit) {
		cancel();
		this.taskRef.set(this.executor.scheduleWithFixedDelay(task, initialDelay, delay, unit));
	}

	public void cancel() {
		ScheduledFuture<?> t = this.taskRef.getAndSet(null);
		if (t != null) {
			t.cancel(this.cancelMayInterruptIfRunning);
		}
	}

	public void shutdownNow() {
		this.executor.shutdownNow();
	}

	public boolean hasTask() {
		return this.taskRef.get() != null;
	}

	public static abstract class Task<T> implements Runnable {
		protected final T data;

		public Task() {
			this(null);
		}

		public Task(T data) {
			this.data = data;
		}
	}
}
