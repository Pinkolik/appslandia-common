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

import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import com.appslandia.common.base.BaseEncoder;
import com.appslandia.common.utils.ArrayUtils;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.RandomUtils;
import com.appslandia.common.utils.ValueUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class PasswordDigester extends TextDigester {

	public static final PasswordDigester DEFAULT = new PasswordDigester();

	private int saltSize;
	private int iterationCount;
	private int keySize;

	private String secretKeyAlgorithm, provider;
	private SecretKeyFactory secretKeyFactory;

	final Object mutex = new Object();
	final Random random = new SecureRandom();

	@Override
	protected void init() throws Exception {
		// baseEncoder
		this.baseEncoder = ValueUtils.valueOrAlt(this.baseEncoder, BaseEncoder.BASE64);

		// key
		this.keySize = ValueUtils.valueOrMin(this.keySize, 32);
		this.saltSize = ValueUtils.valueOrAlt(this.saltSize, this.keySize);
		this.iterationCount = ValueUtils.valueOrMin(this.iterationCount, 10_000);

		this.secretKeyAlgorithm = ValueUtils.valueOrAlt(this.secretKeyAlgorithm, "PBKDF2WithHmacSHA512");

		// secretKeyFactory
		if (this.provider == null) {
			this.secretKeyFactory = SecretKeyFactory.getInstance(this.secretKeyAlgorithm);
		} else {
			this.secretKeyFactory = SecretKeyFactory.getInstance(this.secretKeyAlgorithm, this.provider);
		}
	}

	@Override
	public String digest(String password) throws CryptoException {
		this.initialize();
		AssertUtils.assertNotNull(password, "password is required.");

		byte[] salt = RandomUtils.nextBytes(this.saltSize, this.random);
		char[] pwdChars = password.toCharArray();
		try {
			byte[] secKey = generateSecret(pwdChars, salt);
			return this.baseEncoder.encode(ArrayUtils.append(salt, secKey));
		} finally {
			CryptoUtils.clear(pwdChars);
		}
	}

	@Override
	public boolean verify(String password, String digested) throws CryptoException {
		this.initialize();
		AssertUtils.assertNotNull(password, "password is required.");
		AssertUtils.assertNotNull(digested, "digested is required.");

		byte[] dg = this.baseEncoder.decode(digested);
		AssertUtils.assertTrue(dg.length > this.saltSize, "digested is invalid.");

		byte[] salt = new byte[this.saltSize];
		byte[] secKey = new byte[dg.length - this.saltSize];
		ArrayUtils.copy(dg, salt, secKey);

		char[] pwdChars = password.toCharArray();
		try {
			byte[] computedSecKey = generateSecret(pwdChars, salt);
			return Arrays.equals(computedSecKey, secKey);
		} finally {
			CryptoUtils.clear(pwdChars);
		}
	}

	private byte[] generateSecret(char[] password, byte[] salt) throws CryptoException {
		PBEKeySpec keySpec = new PBEKeySpec(password, salt, this.iterationCount, this.keySize * 8);
		SecretKey secretkey = null;
		synchronized (this.mutex) {
			try {
				secretkey = this.secretKeyFactory.generateSecret(keySpec);
			} catch (GeneralSecurityException ex) {
				throw new CryptoException(ex);
			} finally {
				keySpec.clearPassword();
			}
		}
		byte[] key = secretkey.getEncoded();
		CryptoUtils.destroyQuietly(secretkey);
		return key;
	}

	public PasswordDigester setSaltSize(int saltSize) {
		this.assertNotInitialized();
		this.saltSize = saltSize;
		return this;
	}

	public PasswordDigester setIterationCount(int iterationCount) {
		this.assertNotInitialized();
		this.iterationCount = iterationCount;
		return this;
	}

	public PasswordDigester setKeySize(int keySize) {
		this.assertNotInitialized();
		this.keySize = keySize;
		return this;
	}

	public PasswordDigester setSecretKeyAlgorithm(String secretKeyAlgorithm) {
		this.assertNotInitialized();
		this.secretKeyAlgorithm = secretKeyAlgorithm;
		return this;
	}

	public PasswordDigester setProvider(String provider) {
		this.assertNotInitialized();
		this.provider = provider;
		return this;
	}

	@Override
	public PasswordDigester setDigester(Digester digester) {
		throw new UnsupportedOperationException();
	}

	@Override
	public PasswordDigester setTextCharset(Charset charset) {
		throw new UnsupportedOperationException();
	}

	@Override
	public PasswordDigester setTextCharset(String textCharset) {
		throw new UnsupportedOperationException();
	}

	@Override
	public PasswordDigester copy() {
		PasswordDigester impl = new PasswordDigester();
		impl.setSaltSize(this.saltSize).setIterationCount(this.iterationCount).setKeySize(this.keySize);
		impl.setSecretKeyAlgorithm(this.secretKeyAlgorithm).setProvider(this.provider);
		return impl;
	}
}
