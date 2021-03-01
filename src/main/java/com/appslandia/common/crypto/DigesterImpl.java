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

import java.security.MessageDigest;
import java.util.Arrays;

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.AssertUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class DigesterImpl extends InitializeObject implements Digester {
	private String algorithm, provider;

	private MessageDigest digest;
	final Object mutex = new Object();

	public DigesterImpl() {
	}

	public DigesterImpl(String algorithm) {
		this.algorithm = algorithm;
	}

	public DigesterImpl(String algorithm, String provider) {
		this.algorithm = algorithm;
		this.provider = provider;
	}

	@Override
	protected void init() throws Exception {
		AssertUtils.assertNotNull(this.algorithm, "algorithm is required.");

		// MessageDigest
		if (this.provider == null) {
			this.digest = MessageDigest.getInstance(this.algorithm);
		} else {
			this.digest = MessageDigest.getInstance(this.algorithm, this.provider);
		}
	}

	@Override
	public int getDigestSize() {
		this.initialize();
		return this.digest.getDigestLength();
	}

	@Override
	public byte[] digest(byte[] message) throws CryptoException {
		this.initialize();
		AssertUtils.assertNotNull(message, "message is required.");

		synchronized (this.mutex) {
			return this.digest.digest(message);
		}
	}

	@Override
	public boolean verify(byte[] message, byte[] digested) throws CryptoException {
		this.initialize();
		AssertUtils.assertNotNull(message, "message is required.");
		AssertUtils.assertNotNull(digested, "digested is required.");

		byte[] digest = null;
		synchronized (this.mutex) {
			digest = this.digest.digest(message);
		}
		return Arrays.equals(digested, digest);
	}

	public DigesterImpl setAlgorithm(String algorithm) {
		this.assertNotInitialized();
		this.algorithm = algorithm;
		return this;
	}

	public DigesterImpl setProvider(String provider) {
		this.assertNotInitialized();
		this.provider = provider;
		return this;
	}

	@Override
	public DigesterImpl copy() {
		return new DigesterImpl().setAlgorithm(this.algorithm).setProvider(this.provider);
	}
}
