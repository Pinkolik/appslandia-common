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
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.appslandia.common.base.ThreadSafeTester;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class RsaEncryptorTest {

	private KeyPair keyPair;

	@Before
	public void initialize() {
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(2048, new SecureRandom());
			keyPair = generator.generateKeyPair();
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test() {
		RsaEncryptor impl = new RsaEncryptor();
		impl.setTransformation("RSA/ECB/PKCS1Padding");
		impl.setPublicKey(keyPair.getPublic()).setPrivateKey(keyPair.getPrivate());
		try {
			byte[] data = "data".getBytes(StandardCharsets.UTF_8);
			byte[] enc = impl.encrypt(data);

			Assert.assertArrayEquals(impl.decrypt(enc), data);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_copy() {
		RsaEncryptor impl = new RsaEncryptor();
		impl.setTransformation("RSA/ECB/PKCS1Padding");
		impl.setPublicKey(keyPair.getPublic()).setPrivateKey(keyPair.getPrivate());
		impl = impl.copy();

		try {
			byte[] data = "data".getBytes(StandardCharsets.UTF_8);
			byte[] enc = impl.encrypt(data);

			Assert.assertArrayEquals(impl.decrypt(enc), data);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_OAEPPadding() {
		RsaEncryptor impl = new RsaEncryptor();
		impl.setTransformation("RSA/ECB/OAEPPadding");
		impl.setPublicKey(keyPair.getPublic()).setPrivateKey(keyPair.getPrivate());

		try {
			byte[] data = "data".getBytes(StandardCharsets.UTF_8);
			byte[] enc = impl.encrypt(data);

			Assert.assertArrayEquals(impl.decrypt(enc), data);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_OAEPPadding_MD5() {
		RsaEncryptor impl = new RsaEncryptor();
		impl.setTransformation("RSA/ECB/OAEPWithMD5AndMGF1Padding");
		impl.setPublicKey(keyPair.getPublic()).setPrivateKey(keyPair.getPrivate());

		try {
			byte[] data = "data".getBytes(StandardCharsets.UTF_8);
			byte[] enc = impl.encrypt(data);

			Assert.assertArrayEquals(impl.decrypt(enc), data);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_OAEPWithSHA1AndMGF1Padding() {
		RsaEncryptor impl = new RsaEncryptor();
		impl.setTransformation("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
		impl.setPublicKey(keyPair.getPublic()).setPrivateKey(keyPair.getPrivate());

		try {
			byte[] data = "data".getBytes(StandardCharsets.UTF_8);
			byte[] enc = impl.encrypt(data);

			Assert.assertArrayEquals(impl.decrypt(enc), data);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_OAEPWithSHA224AndMGF1Padding() {
		RsaEncryptor impl = new RsaEncryptor();
		impl.setTransformation("RSA/ECB/OAEPWithSHA-224AndMGF1Padding");
		impl.setPublicKey(keyPair.getPublic()).setPrivateKey(keyPair.getPrivate());

		try {
			byte[] data = "data".getBytes(StandardCharsets.UTF_8);
			byte[] enc = impl.encrypt(data);

			Assert.assertArrayEquals(impl.decrypt(enc), data);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_OAEPWithSHA256AndMGF1Padding() {
		RsaEncryptor impl = new RsaEncryptor();
		impl.setTransformation("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
		impl.setPublicKey(keyPair.getPublic()).setPrivateKey(keyPair.getPrivate());

		try {
			byte[] data = "data".getBytes(StandardCharsets.UTF_8);
			byte[] enc = impl.encrypt(data);

			Assert.assertArrayEquals(impl.decrypt(enc), data);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_OAEPWithSHA384AndMGF1Padding() {
		RsaEncryptor impl = new RsaEncryptor();
		impl.setTransformation("RSA/ECB/OAEPWithSHA-384AndMGF1Padding");
		impl.setPublicKey(keyPair.getPublic()).setPrivateKey(keyPair.getPrivate());

		try {
			byte[] data = "data".getBytes(StandardCharsets.UTF_8);
			byte[] enc = impl.encrypt(data);

			Assert.assertArrayEquals(impl.decrypt(enc), data);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_OAEPWithSHA512AndMGF1Padding() {
		RsaEncryptor impl = new RsaEncryptor();
		impl.setTransformation("RSA/ECB/OAEPWithSHA-512AndMGF1Padding");
		impl.setPublicKey(keyPair.getPublic()).setPrivateKey(keyPair.getPrivate());

		try {
			byte[] data = "data".getBytes(StandardCharsets.UTF_8);
			byte[] enc = impl.encrypt(data);

			Assert.assertArrayEquals(impl.decrypt(enc), data);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_threadSafe() {
		final RsaEncryptor impl = new RsaEncryptor();
		impl.setTransformation("RSA/ECB/PKCS1Padding");
		impl.setPublicKey(keyPair.getPublic()).setPrivateKey(keyPair.getPrivate());

		new ThreadSafeTester() {

			@Override
			protected Runnable newTask() {
				return new Runnable() {

					@Override
					public void run() {
						try {
							byte[] data = "data".getBytes(StandardCharsets.UTF_8);
							byte[] enc = impl.encrypt(data);

							Assert.assertArrayEquals(impl.decrypt(enc), data);

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
