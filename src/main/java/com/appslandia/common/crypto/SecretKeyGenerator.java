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

import java.security.GeneralSecurityException;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.RandomUtils;
import com.appslandia.common.utils.ValueUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class SecretKeyGenerator extends InitializeObject {

	public static final SecretKeyGenerator PBKDF2_HMACSHA256 = new SecretKeyGenerator("PBKDF2WithHmacSHA256");
	public static final SecretKeyGenerator PBKDF2_HMACSHA512 = new SecretKeyGenerator("PBKDF2WithHmacSHA512");

	private String algorithm, provider;
	private SecretKeyFactory secretKeyFactory;
	final Object mutex = new Object();

	public SecretKeyGenerator() {
	}

	public SecretKeyGenerator(String algorithm) {
		this.algorithm = algorithm;
	}

	public SecretKeyGenerator(String algorithm, String provider) {
		this.algorithm = algorithm;
		this.provider = provider;
	}

	@Override
	protected void init() throws Exception {
		// algorithm
		this.algorithm = ValueUtils.valueOrAlt(this.algorithm, "PBKDF2WithHmacSHA256");

		// secretKeyFactory
		if (this.provider == null) {
			this.secretKeyFactory = SecretKeyFactory.getInstance(this.algorithm);
		} else {
			this.secretKeyFactory = SecretKeyFactory.getInstance(this.algorithm, this.provider);
		}
	}

	public byte[] generate(char[] password, byte[] salt, int iterationCount, int keySize) throws CryptoException {
		this.initialize();

		PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterationCount, keySize * 8);
		SecretKey secretkey = null;
		try {
			synchronized (this.mutex) {
				secretkey = this.secretKeyFactory.generateSecret(keySpec);
			}
		} catch (GeneralSecurityException ex) {
			throw new CryptoException(ex);
		} finally {
			keySpec.clearPassword();
		}
		byte[] key = secretkey.getEncoded();
		CryptoUtils.destroyQuietly(secretkey);
		return key;
	}

	public SecretKeyGenerator setAlgorithm(String algorithm) {
		this.assertNotInitialized();
		this.algorithm = algorithm;
		return this;
	}

	public SecretKeyGenerator setProvider(String provider) {
		this.assertNotInitialized();
		this.provider = provider;
		return this;
	}

	public SecretKeyGenerator copy() {
		return new SecretKeyGenerator().setAlgorithm(this.algorithm).setProvider(this.provider);
	}

	public static byte[] generate(int keySize) {
		char[] password = PasswordUtil.generatePassword(24);
		byte[] key = PBKDF2_HMACSHA512.generate(password, RandomUtils.nextBytes(keySize), 10_000, keySize);
		CryptoUtils.clear(password);
		return key;
	}
}
