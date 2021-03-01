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

import com.appslandia.common.base.DestroyException;
import com.appslandia.common.base.RoundRobinPool;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.ValueUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class PoolPasswordDigester extends PasswordDigester {
	private PasswordDigester digester;

	private int poolSize;
	private PasswordDigester[] digesters;
	private RoundRobinPool<PasswordDigester> digesterPool;

	public PoolPasswordDigester() {
	}

	public PoolPasswordDigester(PasswordDigester digester) {
		this.digester = digester;
	}

	public PoolPasswordDigester(PasswordDigester digester, int poolSize) {
		this.digester = digester;
		this.poolSize = poolSize;
	}

	@Override
	protected void init() throws Exception {
		AssertUtils.assertNotNull(this.digester, "digester is required.");

		this.poolSize = ValueUtils.valueOrMin(this.poolSize, Runtime.getRuntime().availableProcessors());
		this.digesters = new PasswordDigester[this.poolSize];
		this.digesters[0] = this.digester;

		for (int i = 1; i < this.poolSize; i++) {
			this.digesters[i] = this.digester.copy();
		}
		this.digesterPool = new RoundRobinPool<>(this.digesters);
	}

	@Override
	public void destroy() throws DestroyException {
		if (this.digesters != null) {
			for (PasswordDigester digester : this.digesters) {
				digester.destroy();
			}
		}
	}

	@Override
	public String digest(String password) throws CryptoException {
		this.initialize();
		AssertUtils.assertNotNull(password, "password is required.");
		return this.digesterPool.next().digest(password);
	}

	@Override
	public boolean verify(String password, String digested) throws CryptoException {
		this.initialize();
		AssertUtils.assertNotNull(password, "password is required.");
		AssertUtils.assertNotNull(digested, "digested is required.");

		return this.digesterPool.next().verify(password, digested);
	}

	public PoolPasswordDigester setPasswordDigester(PasswordDigester passwordDigester) {
		this.assertNotInitialized();
		this.digester = passwordDigester;
		return this;
	}

	public PoolPasswordDigester setPoolSize(int poolSize) {
		this.assertNotInitialized();
		this.poolSize = poolSize;
		return this;
	}

	public PoolPasswordDigester setSaltSize(int saltSize) {
		throw new UnsupportedOperationException();
	}

	public PoolPasswordDigester setIterationCount(int iterationCount) {
		throw new UnsupportedOperationException();
	}

	public PoolPasswordDigester setKeySize(int keySize) {
		throw new UnsupportedOperationException();
	}

	public PoolPasswordDigester setSecretKeyAlgorithm(String secretKeyAlgorithm) {
		throw new UnsupportedOperationException();
	}

	public PoolPasswordDigester setProvider(String provider) {
		throw new UnsupportedOperationException();
	}

	@Override
	public PoolPasswordDigester setDigester(Digester digester) {
		throw new UnsupportedOperationException();
	}

	@Override
	public PoolPasswordDigester setTextCharset(Charset charset) {
		throw new UnsupportedOperationException();
	}

	@Override
	public PoolPasswordDigester setTextCharset(String textCharset) {
		throw new UnsupportedOperationException();
	}

	@Override
	public PoolPasswordDigester copy() {
		PoolPasswordDigester impl = new PoolPasswordDigester().setPoolSize(this.poolSize);
		if (this.digester != null) {
			impl.digester = this.digester.copy();
		}
		return impl;
	}
}
