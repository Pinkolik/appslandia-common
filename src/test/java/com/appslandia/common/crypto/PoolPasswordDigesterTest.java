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

package com.appslandia.common.crypto;

import org.junit.Assert;
import org.junit.Test;

import com.appslandia.common.base.ThreadSafeTester;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class PoolPasswordDigesterTest {

	@Test
	public void test() {
		PoolPasswordDigester impl = new PoolPasswordDigester().setPasswordDigester(new PasswordDigester());
		try {

			String password = "password";
			String digested = impl.digest(password);

			Assert.assertTrue(impl.verify(password, digested));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_copy() {
		PoolPasswordDigester impl = new PoolPasswordDigester().setPasswordDigester(new PasswordDigester());
		impl = impl.copy();
		try {

			String password = "password";
			String digested = impl.digest(password);

			Assert.assertTrue(impl.verify(password, digested));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_invalid() {
		PoolPasswordDigester impl = new PoolPasswordDigester().setPasswordDigester(new PasswordDigester());
		try {

			String password = "password";
			String digested = impl.digest(password);

			Assert.assertFalse(impl.verify("invalidPassword", digested));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_threadSafe() {
		final PoolPasswordDigester impl = new PoolPasswordDigester().setPasswordDigester(new PasswordDigester());
		new ThreadSafeTester() {

			@Override
			protected Runnable newTask() {
				return new Runnable() {

					@Override
					public void run() {
						try {

							String password = "password";
							String digested = impl.digest(password);

							Assert.assertTrue(impl.verify(password, digested));

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
