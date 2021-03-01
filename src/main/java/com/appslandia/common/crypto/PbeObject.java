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

import java.util.Arrays;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.appslandia.common.base.DestroyException;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.SYS;
import com.appslandia.common.utils.ValueUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class PbeObject extends InitializeObject {

	protected int saltSize;
	protected int iterationCount;
	protected int keySize;

	protected char[] password;
	protected SecretKeyGenerator secretKeyGenerator;

	@Override
	protected void init() throws Exception {
		AssertUtils.assertTrue(this.keySize > 0, "keySize is required.");

		this.saltSize = ValueUtils.valueOrAlt(this.saltSize, this.keySize);
		this.iterationCount = ValueUtils.valueOrMin(this.iterationCount, 10_000);

		AssertUtils.assertNotNull(this.password, "password is required.");

		if (this.secretKeyGenerator == null) {
			this.secretKeyGenerator = new SecretKeyGenerator();
		}
	}

	@Override
	public void destroy() throws DestroyException {
		if (this.password != null) {
			CryptoUtils.clear(this.password);
		}
	}

	protected SecretKey buildSecretKey(final byte[] salt, final String algorithm) throws CryptoException {
		byte[] key = this.secretKeyGenerator.generate(this.password, salt, this.iterationCount, this.keySize);
		SecretKey secretKey = new SecretKeySpec(key, algorithm);
		CryptoUtils.clear(key);
		return secretKey;
	}

	public PbeObject setSaltSize(int saltSize) {
		this.assertNotInitialized();
		this.saltSize = saltSize;
		return this;
	}

	public PbeObject setIterationCount(int iterationCount) {
		this.assertNotInitialized();
		this.iterationCount = iterationCount;
		return this;
	}

	public PbeObject setKeySize(int keySize) {
		this.assertNotInitialized();
		this.keySize = keySize;
		return this;
	}

	public PbeObject setPassword(char[] password) {
		this.assertNotInitialized();
		if (password != null) {
			this.password = Arrays.copyOf(password, password.length);
		}
		return this;
	}

	public PbeObject setPassword(String passwordOrEnv) {
		this.assertNotInitialized();

		if (passwordOrEnv != null) {
			String resolvedValue = SYS.resolveExpr(passwordOrEnv);
			this.password = resolvedValue.toCharArray();
		}
		return this;
	}

	public PbeObject setSecretKeyGenerator(SecretKeyGenerator secretKeyGenerator) {
		this.assertNotInitialized();
		this.secretKeyGenerator = secretKeyGenerator;
		return this;
	}
}
