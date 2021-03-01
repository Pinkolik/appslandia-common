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

import java.nio.charset.StandardCharsets;

import org.junit.Assert;
import org.junit.Test;

import com.appslandia.common.base.ThreadSafeTester;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class SignerEncryptorTest {

	@Test
	public void test() {
		SignerEncryptor impl = new SignerEncryptor();
		impl.setEncryptor(new PbeEncryptor().setTransformation("AES").setKeySize(16).setPassword("password".toCharArray()));
		impl.setSigner(new PbeMacDigester().setAlgorithm("HmacMD5").setKeySize(16).setPassword("password".toCharArray()));

		try {
			byte[] data = "data".getBytes(StandardCharsets.UTF_8);
			byte[] encrypted = impl.encrypt(data);

			byte[] decrypted = impl.decrypt(encrypted);
			Assert.assertArrayEquals(data, decrypted);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_copy() {
		SignerEncryptor impl = new SignerEncryptor();
		impl.setEncryptor(new PbeEncryptor().setTransformation("AES").setKeySize(16).setPassword("password".toCharArray()));
		impl.setSigner(new PbeMacDigester().setAlgorithm("HmacMD5").setKeySize(16).setPassword("password".toCharArray()));
		impl = impl.copy();

		try {
			byte[] data = "data".getBytes(StandardCharsets.UTF_8);
			byte[] encrypted = impl.encrypt(data);

			byte[] decrypted = impl.decrypt(encrypted);
			Assert.assertArrayEquals(data, decrypted);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_threadSafe() {
		final SignerEncryptor impl = new SignerEncryptor();
		impl.setEncryptor(new PbeEncryptor().setTransformation("AES").setKeySize(16).setPassword("password".toCharArray()));
		impl.setSigner(new PbeMacDigester().setAlgorithm("HmacMD5").setKeySize(16).setPassword("password".toCharArray()));

		new ThreadSafeTester() {

			@Override
			protected Runnable newTask() {
				return new Runnable() {

					@Override
					public void run() {
						try {
							byte[] data = "data".getBytes(StandardCharsets.UTF_8);
							byte[] encrypted = impl.encrypt(data);

							byte[] decrypted = impl.decrypt(encrypted);
							Assert.assertArrayEquals(data, decrypted);

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

	@Test
	public void test_noSigner() {
		SignerEncryptor impl = new SignerEncryptor();
		impl.setEncryptor(new PbeEncryptor().setTransformation("AES").setKeySize(16).setPassword("password".toCharArray()));
		try {
			byte[] data = "data".getBytes(StandardCharsets.UTF_8);
			byte[] encrypted = impl.encrypt(data);

			byte[] decrypted = impl.decrypt(encrypted);
			Assert.assertArrayEquals(data, decrypted);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_noSigner_copy() {
		SignerEncryptor impl = new SignerEncryptor();
		impl.setEncryptor(new PbeEncryptor().setTransformation("AES").setKeySize(16).setPassword("password".toCharArray()));
		impl = impl.copy();
		try {
			byte[] data = "data".getBytes(StandardCharsets.UTF_8);
			byte[] encrypted = impl.encrypt(data);

			byte[] decrypted = impl.decrypt(encrypted);
			Assert.assertArrayEquals(data, decrypted);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_noSigner_threadSafe() {
		final SignerEncryptor impl = new SignerEncryptor();
		impl.setEncryptor(new PbeEncryptor().setTransformation("AES").setKeySize(16).setPassword("password".toCharArray()));

		new ThreadSafeTester() {

			@Override
			protected Runnable newTask() {
				return new Runnable() {

					@Override
					public void run() {
						try {
							byte[] data = "data".getBytes(StandardCharsets.UTF_8);
							byte[] encrypted = impl.encrypt(data);

							byte[] decrypted = impl.decrypt(encrypted);
							Assert.assertArrayEquals(data, decrypted);

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
