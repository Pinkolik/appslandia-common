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
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.appslandia.common.base.DestroyException;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.ArrayUtils;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.SYS;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class MacDigester extends InitializeObject implements Digester {
	private String algorithm, provider;
	private Mac mac;
	private byte[] secret;

	final Object mutex = new Object();
	final Random random = new SecureRandom();

	@Override
	protected void init() throws Exception {
		AssertUtils.assertNotNull(this.algorithm, "algorithm is required.");
		AssertUtils.assertNotNull(this.secret, "secret is required.");

		// MAC
		if (this.provider == null) {
			this.mac = Mac.getInstance(this.algorithm);
		} else {
			this.mac = Mac.getInstance(this.algorithm, this.provider);
		}
		this.mac.init(new SecretKeySpec(this.secret, this.algorithm));
		CryptoUtils.clear(this.secret);
	}

	@Override
	public int getDigestSize() {
		this.initialize();
		return this.mac.getMacLength();
	}

	@Override
	public void destroy() throws DestroyException {
	}

	@Override
	public byte[] digest(byte[] message) throws CryptoException {
		this.initialize();
		AssertUtils.assertNotNull(message, "message is required.");

		synchronized (this.mutex) {
			return this.mac.doFinal(message);
		}
	}

	@Override
	public boolean verify(byte[] message, byte[] mac) throws CryptoException {
		this.initialize();
		AssertUtils.assertNotNull(message, "message is required.");
		AssertUtils.assertNotNull(mac, "mac is required.");

		byte[] msgMac = null;
		synchronized (this.mutex) {
			msgMac = this.mac.doFinal(message);
		}
		return Arrays.equals(msgMac, mac);
	}

	public MacDigester setAlgorithm(String algorithm) {
		this.assertNotInitialized();
		this.algorithm = algorithm;
		return this;
	}

	public MacDigester setProvider(String provider) {
		this.assertNotInitialized();
		this.provider = provider;
		return this;
	}

	public MacDigester setSecret(byte[] secret) {
		this.assertNotInitialized();
		if (secret != null) {
			this.secret = ArrayUtils.copy(secret);
		}
		return this;
	}

	public MacDigester setSecret(String secretOrEnv) {
		this.assertNotInitialized();

		if (secretOrEnv != null) {
			String resolvedValue = SYS.resolveExpr(secretOrEnv);
			this.secret = resolvedValue.getBytes(StandardCharsets.UTF_8);
		}
		return this;
	}

	@Override
	public MacDigester copy() {
		MacDigester impl = new MacDigester().setAlgorithm(this.algorithm).setProvider(this.provider);
		impl.secret = ArrayUtils.copy(this.secret);
		return impl;
	}
}
