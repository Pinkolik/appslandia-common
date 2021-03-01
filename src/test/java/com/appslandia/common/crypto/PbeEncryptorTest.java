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
public class PbeEncryptorTest {

	@Test
	public void test() {
		PbeEncryptor impl = new PbeEncryptor();
		impl.setTransformation("AES").setKeySize(16);
		impl.setPassword("password".toCharArray());
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
		PbeEncryptor impl = new PbeEncryptor();
		impl.setTransformation("AES").setKeySize(16);
		impl.setPassword("password".toCharArray());
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
	public void test_CBC() {
		PbeEncryptor impl = new PbeEncryptor();
		impl.setTransformation("AES/CBC/PKCS5Padding").setKeySize(16);
		impl.setPassword("password".toCharArray());
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
	public void test_PCBC() {
		PbeEncryptor impl = new PbeEncryptor();
		impl.setTransformation("AES/PCBC/PKCS5Padding").setKeySize(16);
		impl.setPassword("password".toCharArray());
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
	public void test_CFB() {
		PbeEncryptor impl = new PbeEncryptor();
		impl.setTransformation("AES/CFB/PKCS5Padding").setKeySize(16);
		impl.setPassword("password".toCharArray());
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
	public void test_CFB8() {
		PbeEncryptor impl = new PbeEncryptor();
		impl.setTransformation("AES/CFB8/PKCS5Padding").setKeySize(16);
		impl.setPassword("password".toCharArray());
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
	public void test_OFB() {
		PbeEncryptor impl = new PbeEncryptor();
		impl.setTransformation("AES/OFB/PKCS5Padding").setKeySize(16);
		impl.setPassword("password".toCharArray());
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
	public void test_OFB8() {
		PbeEncryptor impl = new PbeEncryptor();
		impl.setTransformation("AES/OFB8/PKCS5Padding").setKeySize(16);
		impl.setPassword("password".toCharArray());
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
	public void test_CTR() {
		PbeEncryptor impl = new PbeEncryptor();
		impl.setTransformation("AES/CTR/PKCS5Padding").setKeySize(16);
		impl.setPassword("password".toCharArray());
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
	public void test_ECB() {
		PbeEncryptor impl = new PbeEncryptor();
		impl.setTransformation("AES/ECB/PKCS5Padding").setKeySize(16);
		impl.setPassword("password".toCharArray());
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
	public void test_CTS() {
		PbeEncryptor impl = new PbeEncryptor();
		impl.setTransformation("AES/CTS/PKCS5Padding").setKeySize(16);
		impl.setPassword("password".toCharArray());
		try {
			byte[] data = "012345678901234567890123456789".getBytes(StandardCharsets.UTF_8);
			byte[] encrypted = impl.encrypt(data);

			byte[] decrypted = impl.decrypt(encrypted);
			Assert.assertArrayEquals(data, decrypted);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_GCM() {
		PbeEncryptor impl = new PbeEncryptor();
		impl.setTransformation("AES/GCM/NoPadding").setKeySize(16);
		impl.setPassword("password".toCharArray());
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
		final PbeEncryptor impl = new PbeEncryptor();
		impl.setTransformation("AES/CBC/PKCS5Padding").setKeySize(16);
		impl.setPassword("password".toCharArray());

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
