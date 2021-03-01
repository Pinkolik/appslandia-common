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

import org.junit.Assert;
import org.junit.Test;

import com.appslandia.common.utils.RandomUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class SimplePoolTest {

	@Test
	public void test() {
		Object obj1 = new Object();
		Object obj2 = new Object();
		Object obj3 = new Object();

		SimplePool<Object> pool = new SimplePool<>();

		pool.put(obj1);
		pool.put(obj2);
		pool.put(obj3);

		Assert.assertTrue(obj3 == pool.get());
		Assert.assertTrue(obj2 == pool.get());
		Assert.assertTrue(obj1 == pool.get());

		Assert.assertNull(pool.get());
	}

	@Test
	public void test_threadSafe() {
		final SimplePool<Object> pool = new SimplePool<>();
		new ThreadSafeTester() {

			@Override
			protected Runnable newTask() {
				return new Runnable() {

					@Override
					public void run() {
						try {

							int n = RandomUtils.nextInt(1, 2);
							if (n == 1) {
								pool.put(new Object());
							} else {
								pool.get();
							}

						} catch (Exception ex) {
							Assert.fail(ex.getMessage());
						} finally {
							countDown();
						}
					}
				};
			}
		}.executeThenAwait();
	}
}
