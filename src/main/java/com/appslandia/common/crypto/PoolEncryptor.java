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
public class PoolEncryptor extends InitializeObject implements Encryptor {
	private Encryptor encryptor;

	private int poolSize;
	private Encryptor[] encryptors;
	private RoundRobinPool<Encryptor> encryptorPool;

	public PoolEncryptor() {
	}

	public PoolEncryptor(Encryptor encryptor) {
		this.encryptor = encryptor;
	}

	public PoolEncryptor(Encryptor encryptor, int poolSize) {
		this.encryptor = encryptor;
		this.poolSize = poolSize;
	}

	@Override
	protected void init() throws Exception {
		AssertUtils.assertNotNull(this.encryptor, "encryptor is required.");

		this.poolSize = ValueUtils.valueOrMin(this.poolSize, Runtime.getRuntime().availableProcessors());
		this.encryptors = new Encryptor[this.poolSize];
		this.encryptors[0] = this.encryptor;

		for (int i = 1; i < this.poolSize; i++) {
			this.encryptors[i] = this.encryptor.copy();
		}
		this.encryptorPool = new RoundRobinPool<>(this.encryptors);
	}

	@Override
	public void destroy() throws DestroyException {
		if (this.encryptors != null) {
			for (Encryptor enc : this.encryptors) {
				enc.destroy();
			}
		}
	}

	@Override
	public byte[] encrypt(byte[] message) throws CryptoException {
		this.initialize();
		AssertUtils.assertNotNull(message, "message is required.");
		return this.encryptorPool.next().encrypt(message);
	}

	@Override
	public byte[] decrypt(byte[] message) throws CryptoException {
		this.initialize();
		AssertUtils.assertNotNull(message, "message is required.");
		return this.encryptorPool.next().decrypt(message);
	}

	public PoolEncryptor setEncryptor(Encryptor encryptor) {
		this.assertNotInitialized();
		this.encryptor = encryptor;
		return this;
	}

	public PoolEncryptor setPoolSize(int poolSize) {
		this.assertNotInitialized();
		this.poolSize = poolSize;
		return this;
	}

	@Override
	public PoolEncryptor copy() {
		PoolEncryptor impl = new PoolEncryptor().setPoolSize(this.poolSize);
		if (this.encryptor != null) {
			impl.encryptor = this.encryptor.copy();
		}
		return impl;
	}
}
