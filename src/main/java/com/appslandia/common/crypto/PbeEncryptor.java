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
import java.security.spec.AlgorithmParameterSpec;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;

import com.appslandia.common.utils.ArrayUtils;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.RandomUtils;
import com.appslandia.common.utils.ValueUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class PbeEncryptor extends PbeObject implements Encryptor {
	private String transformation, provider;
	private int gcmTagSize;

	private String algorithm;
	private String mode;
	private int ivSize;

	private Cipher cipher;

	final Object mutex = new Object();
	final Random random = new SecureRandom();

	static final ConcurrentMap<Integer, byte[]> ZERO_IV_CACHE = new ConcurrentHashMap<>();

	@Override
	protected void init() throws Exception {
		super.init();

		// Algorithm & Mode
		AssertUtils.assertNotNull(this.transformation, "transformation is required.");

		String[] trans = this.transformation.split("/");
		this.algorithm = trans[0].toUpperCase(Locale.ENGLISH);
		AssertUtils.assertFalse("RSA".equals(this.algorithm), "RSA algorithm is not supported.");

		this.mode = (trans.length > 1) ? trans[1].toUpperCase(Locale.ENGLISH) : null;

		// GCM
		if ("GCM".equals(this.mode)) {
			this.gcmTagSize = ValueUtils.valueOrAlt(this.gcmTagSize, 16);
		}

		// cipher
		if (this.provider == null) {
			this.cipher = Cipher.getInstance(this.transformation);
		} else {
			this.cipher = Cipher.getInstance(this.transformation, this.provider);
		}

		// IV
		if (this.mode != null) {
			// GCM ivSize: 12
			this.ivSize = requiresIV(this.mode) ? ("GCM".equals(this.mode) ? 12 : this.cipher.getBlockSize()) : 0;
		}
	}

	protected boolean requiresIV(String mode) {
		return "CBC".equals(mode) || "GCM".equals(mode) || "CTR".equals(mode) || "CTS".equals(mode) || "PCBC".equals(mode) || mode.startsWith("CFB")
				|| mode.startsWith("OFB");
	}

	protected AlgorithmParameterSpec buildIvParameter(byte[] iv) {
		if ("GCM".equals(this.mode)) {
			return new GCMParameterSpec(this.gcmTagSize * 8, iv);
		}
		return new IvParameterSpec(iv);
	}

	@Override
	public byte[] encrypt(byte[] message) throws CryptoException {
		this.initialize();
		AssertUtils.assertNotNull(message, "message is required.");

		byte[] iv = null;
		if (this.ivSize > 0) {
			// Each encryption will use unique secret key so no random IV needed
			iv = ZERO_IV_CACHE.computeIfAbsent(this.ivSize, s -> new byte[s]);
		}

		byte[] salt = RandomUtils.nextBytes(this.saltSize, this.random);
		SecretKey secretKey = buildSecretKey(salt, this.algorithm);

		try {
			byte[] encMsg = null;
			synchronized (this.mutex) {
				if (iv != null) {
					this.cipher.init(Cipher.ENCRYPT_MODE, secretKey, buildIvParameter(iv));
				} else {
					this.cipher.init(Cipher.ENCRYPT_MODE, secretKey);
				}
				encMsg = this.cipher.doFinal(message);
			}
			return ArrayUtils.append(salt, encMsg);

		} catch (GeneralSecurityException ex) {
			throw new CryptoException(ex);
		} finally {
			CryptoUtils.destroyQuietly(secretKey);
		}
	}

	@Override
	public byte[] decrypt(byte[] message) throws CryptoException {
		this.initialize();
		AssertUtils.assertNotNull(message, "message is required.");

		byte[] iv = null;
		if (this.ivSize > 0) {
			iv = ZERO_IV_CACHE.computeIfAbsent(this.ivSize, s -> new byte[s]);
		}

		AssertUtils.assertTrue(message.length > this.saltSize, "message is invalid.");
		byte[] salt = new byte[this.saltSize];
		ArrayUtils.copy(message, salt);

		SecretKey secretKey = buildSecretKey(salt, this.algorithm);
		try {
			synchronized (this.mutex) {
				if (iv != null) {
					this.cipher.init(Cipher.DECRYPT_MODE, secretKey, buildIvParameter(iv));
				} else {
					this.cipher.init(Cipher.DECRYPT_MODE, secretKey);
				}
				return this.cipher.doFinal(message, this.saltSize, message.length - this.saltSize);
			}
		} catch (GeneralSecurityException ex) {
			throw new CryptoException(ex);
		} finally {
			CryptoUtils.destroyQuietly(secretKey);
		}
	}

	public int getIvSize() {
		this.initialize();
		return this.ivSize;
	}

	public PbeEncryptor setTransformation(String transformation) {
		this.assertNotInitialized();
		this.transformation = transformation;
		return this;
	}

	public PbeEncryptor setProvider(String provider) {
		this.assertNotInitialized();
		this.provider = provider;
		return this;
	}

	@Override
	public PbeEncryptor setSaltSize(int saltSize) {
		super.setSaltSize(saltSize);
		return this;
	}

	@Override
	public PbeEncryptor setIterationCount(int iterationCount) {
		super.setIterationCount(iterationCount);
		return this;
	}

	@Override
	public PbeEncryptor setKeySize(int keySize) {
		super.setKeySize(keySize);
		return this;
	}

	@Override
	public PbeEncryptor setPassword(char[] password) {
		super.setPassword(password);
		return this;
	}

	@Override
	public PbeEncryptor setPassword(String passwordOrEnv) {
		super.setPassword(passwordOrEnv);
		return this;
	}

	@Override
	public PbeEncryptor setSecretKeyGenerator(SecretKeyGenerator secretKeyGenerator) {
		super.setSecretKeyGenerator(secretKeyGenerator);
		return this;
	}

	public PbeEncryptor setGcmTagSize(int gcmTagSize) {
		assertNotInitialized();
		this.gcmTagSize = gcmTagSize;
		return this;
	}

	@Override
	public PbeEncryptor copy() {
		PbeEncryptor impl = new PbeEncryptor().setTransformation(this.transformation).setProvider(this.provider);
		impl.setSaltSize(this.saltSize).setIterationCount(this.iterationCount).setKeySize(this.keySize);

		if (this.password != null) {
			impl.setPassword(this.password);
		}
		if (this.secretKeyGenerator != null) {
			impl.secretKeyGenerator = this.secretKeyGenerator.copy();
		}
		impl.gcmTagSize = this.gcmTagSize;
		return impl;
	}
}
