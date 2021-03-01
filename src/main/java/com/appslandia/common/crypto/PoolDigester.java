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

import com.appslandia.common.base.DestroyException;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.base.RoundRobinPool;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.ValueUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class PoolDigester extends InitializeObject implements Digester {
	private Digester digester;

	private int poolSize;
	private Digester[] digesters;
	private RoundRobinPool<Digester> digesterPool;

	public PoolDigester() {
	}

	public PoolDigester(Digester digester) {
		this.digester = digester;
	}

	public PoolDigester(Digester digester, int poolSize) {
		this.digester = digester;
		this.poolSize = poolSize;
	}

	@Override
	protected void init() throws Exception {
		AssertUtils.assertNotNull(this.digester, "digester is required.");

		this.poolSize = ValueUtils.valueOrMin(this.poolSize, Runtime.getRuntime().availableProcessors());
		this.digesters = new Digester[this.poolSize];
		this.digesters[0] = this.digester;

		for (int i = 1; i < this.poolSize; i++) {
			this.digesters[i] = this.digester.copy();
		}
		this.digesterPool = new RoundRobinPool<>(this.digesters);
	}

	@Override
	public void destroy() throws DestroyException {
		if (this.digesters != null) {
			for (Digester digester : this.digesters) {
				digester.destroy();
			}
		}
	}

	@Override
	public int getDigestSize() {
		this.initialize();
		return this.digester.getDigestSize();
	}

	@Override
	public byte[] digest(byte[] message) throws CryptoException {
		this.initialize();
		AssertUtils.assertNotNull(message, "message is required.");
		return this.digesterPool.next().digest(message);
	}

	@Override
	public boolean verify(byte[] message, byte[] digested) throws CryptoException {
		this.initialize();
		AssertUtils.assertNotNull(message, "message is required.");
		AssertUtils.assertNotNull(digested, "digested is required.");

		return this.digesterPool.next().verify(message, digested);
	}

	public PoolDigester setDigester(Digester digester) {
		this.assertNotInitialized();
		this.digester = digester;
		return this;
	}

	public PoolDigester setPoolSize(int poolSize) {
		this.assertNotInitialized();
		this.poolSize = poolSize;
		return this;
	}

	@Override
	public PoolDigester copy() {
		PoolDigester impl = new PoolDigester().setPoolSize(this.poolSize);
		if (this.digester != null) {
			impl.digester = this.digester.copy();
		}
		return impl;
	}
}
