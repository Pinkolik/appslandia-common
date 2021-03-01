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
import java.security.SecureRandom;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.SecretKey;

import com.appslandia.common.utils.ArrayUtils;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.RandomUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class PbeMacDigester extends PbeObject implements Digester {
	private String algorithm, provider;
	private Mac mac;

	final Object mutex = new Object();
	final Random random = new SecureRandom();

	@Override
	protected void init() throws Exception {
		super.init();

		AssertUtils.assertNotNull(this.algorithm, "algorithm is required.");

		// MAC
		if (this.provider == null) {
			this.mac = Mac.getInstance(this.algorithm);
		} else {
			this.mac = Mac.getInstance(this.algorithm, this.provider);
		}
	}

	@Override
	public int getDigestSize() {
		this.initialize();
		return this.saltSize + this.mac.getMacLength();
	}

	@Override
	public byte[] digest(byte[] message) throws CryptoException {
		this.initialize();
		AssertUtils.assertNotNull(message, "message is required.");

		byte[] salt = RandomUtils.nextBytes(this.saltSize, this.random);
		SecretKey secretKey = buildSecretKey(salt, this.algorithm);

		try {
			byte[] msgMac = null;
			synchronized (this.mutex) {
				this.mac.init(secretKey);
				msgMac = this.mac.doFinal(message);
			}
			return ArrayUtils.append(salt, msgMac);

		} catch (GeneralSecurityException ex) {
			throw new CryptoException(ex);
		} finally {
			CryptoUtils.destroyQuietly(secretKey);
		}
	}

	@Override
	public boolean verify(byte[] message, byte[] mac) throws CryptoException {
		this.initialize();

		AssertUtils.assertNotNull(message, "message is required.");
		AssertUtils.assertNotNull(mac, "mac is required.");
		AssertUtils.assertTrue(mac.length > this.saltSize, "mac is invalid.");

		byte[] salt = new byte[this.saltSize];
		ArrayUtils.copy(mac, salt);

		SecretKey secretKey = buildSecretKey(salt, this.algorithm);
		try {
			byte[] msgMac = null;
			synchronized (this.mutex) {
				this.mac.init(secretKey);
				msgMac = this.mac.doFinal(message);
			}
			return ArrayUtils.contains(mac, msgMac, this.saltSize);

		} catch (GeneralSecurityException ex) {
			throw new CryptoException(ex);
		} finally {
			CryptoUtils.destroyQuietly(secretKey);
		}
	}

	public PbeMacDigester setAlgorithm(String algorithm) {
		this.assertNotInitialized();
		this.algorithm = algorithm;
		return this;
	}

	public PbeMacDigester setProvider(String provider) {
		this.assertNotInitialized();
		this.provider = provider;
		return this;
	}

	@Override
	public PbeMacDigester setSaltSize(int saltSize) {
		super.setSaltSize(saltSize);
		return this;
	}

	@Override
	public PbeMacDigester setIterationCount(int iterationCount) {
		super.setIterationCount(iterationCount);
		return this;
	}

	@Override
	public PbeMacDigester setKeySize(int keySize) {
		super.setKeySize(keySize);
		return this;
	}

	@Override
	public PbeMacDigester setPassword(char[] password) {
		super.setPassword(password);
		return this;
	}

	@Override
	public PbeMacDigester setPassword(String passwordOrEnv) {
		super.setPassword(passwordOrEnv);
		return this;
	}

	@Override
	public PbeMacDigester setSecretKeyGenerator(SecretKeyGenerator secretKeyGenerator) {
		super.setSecretKeyGenerator(secretKeyGenerator);
		return this;
	}

	@Override
	public PbeMacDigester copy() {
		PbeMacDigester impl = new PbeMacDigester().setAlgorithm(this.algorithm).setProvider(this.provider);
		impl.setSaltSize(this.saltSize).setIterationCount(this.iterationCount).setKeySize(this.keySize);

		if (this.password != null) {
			impl.setPassword(this.password);
		}
		if (this.secretKeyGenerator != null) {
			impl.secretKeyGenerator = this.secretKeyGenerator.copy();
		}
		return impl;
	}
}
