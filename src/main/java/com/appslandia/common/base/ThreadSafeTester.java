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

package com.appslandia.common.base;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.appslandia.common.utils.AssertUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class ThreadSafeTester extends InitializeObject {

	private int tasks = 100;
	private int threads = 8;

	private CountDownLatch countDownLatch;
	private ExecutorService executorService;

	@Override
	protected void init() throws Exception {
		AssertUtils.assertTrue(this.tasks > 0);
		AssertUtils.assertTrue(this.threads > 0);

		this.executorService = Executors.newFixedThreadPool(this.threads);
		this.countDownLatch = new CountDownLatch(this.tasks);
	}

	public ThreadSafeTester tasks(int tasks) {
		assertNotInitialized();
		this.tasks = tasks;
		return this;
	}

	public ThreadSafeTester threads(int threads) {
		assertNotInitialized();
		this.threads = threads;
		return this;
	}

	protected abstract Runnable newTask();

	public ThreadSafeTester executeThenAwait() {
		return executeThenAwait(0, null);
	}

	public ThreadSafeTester executeThenAwait(long timeout, TimeUnit unit) {
		initialize();
		for (int i = 0; i < this.tasks; i++) {
			this.executorService.execute(newTask());
		}
		try {
			if (timeout == 0) {
				this.countDownLatch.await();
			} else {
				this.countDownLatch.await(timeout, unit);
			}
		} catch (InterruptedException ex) {
			throw new UncheckedException(ex);
		}

		this.executorService.shutdown();
		return this;
	}

	protected void countDown() {
		this.countDownLatch.countDown();
	}
}
