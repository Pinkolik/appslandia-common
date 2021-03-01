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
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import com.appslandia.common.base.DestroyException;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.AssertUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class DsaDigester extends InitializeObject implements Digester {
	private String algorithm, provider;

	private Signature sign;
	private Signature ver;

	private PrivateKey privateKey;
	private PublicKey publicKey;

	final Object sigMutex = new Object();
	final Object verMutex = new Object();

	@Override
	protected void init() throws Exception {
		AssertUtils.assertNotNull(this.algorithm, "algorithm is required.");
		AssertUtils.assertTrue((this.privateKey != null) || (this.publicKey != null), "No key is provided.");

		// Sign
		if (this.privateKey != null) {
			if (this.provider == null) {
				this.sign = Signature.getInstance(this.algorithm);
			} else {
				this.sign = Signature.getInstance(this.algorithm, this.provider);
			}
			this.sign.initSign(this.privateKey);
		}

		// Verify
		if (this.publicKey != null) {
			if (this.provider == null) {
				this.ver = Signature.getInstance(this.algorithm);
			} else {
				this.ver = Signature.getInstance(this.algorithm, this.provider);
			}
			this.ver.initVerify(this.publicKey);
		}
	}

	@Override
	public void destroy() throws DestroyException {
		CryptoUtils.destroyQuietly(this.privateKey);
	}

	@Override
	public int getDigestSize() {
		this.initialize();
		return -1;
	}

	@Override
	public byte[] digest(byte[] message) throws CryptoException {
		this.initialize();
		AssertUtils.assertNotNull(message, "message is required.");
		AssertUtils.assertNotNull(this.sign, "privateKey is required.");

		try {
			synchronized (this.sigMutex) {
				this.sign.update(message);
				return this.sign.sign();
			}
		} catch (GeneralSecurityException ex) {
			throw new CryptoException(ex);
		}
	}

	@Override
	public boolean verify(byte[] message, byte[] signature) throws CryptoException {
		this.initialize();
		AssertUtils.assertNotNull(message, "message is required.");
		AssertUtils.assertNotNull(signature, "signature is required.");
		AssertUtils.assertNotNull(this.ver, "publicKey is required.");

		try {
			synchronized (this.verMutex) {
				this.ver.update(message);
				return this.ver.verify(signature);
			}
		} catch (GeneralSecurityException ex) {
			throw new CryptoException(ex);
		}
	}

	public DsaDigester setAlgorithm(String algorithm) {
		assertNotInitialized();
		this.algorithm = algorithm;
		return this;
	}

	public DsaDigester setProvider(String provider) {
		assertNotInitialized();
		this.provider = provider;
		return this;
	}

	public DsaDigester setPrivateKey(PrivateKey privateKey) {
		assertNotInitialized();
		this.privateKey = privateKey;
		return this;
	}

	public DsaDigester setPrivateKey(String privateKeyPem) {
		assertNotInitialized();
		if (privateKeyPem != null) {
			this.privateKey = KeyFactoryUtil.DSA.toPrivateKey(privateKeyPem);
		}
		return this;
	}

	public DsaDigester setPublicKey(PublicKey publicKey) {
		assertNotInitialized();
		this.publicKey = publicKey;
		return this;
	}

	public DsaDigester setPublicKey(String publicKeyPem) {
		assertNotInitialized();
		if (publicKeyPem != null) {
			this.publicKey = KeyFactoryUtil.DSA.toPublicKey(publicKeyPem);
		}
		return this;
	}

	@Override
	public DsaDigester copy() {
		DsaDigester impl = new DsaDigester().setAlgorithm(this.algorithm).setProvider(this.provider);

		if (this.privateKey != null) {
			impl.privateKey = new KeyFactoryUtil(this.privateKey.getAlgorithm()).copy(this.privateKey);
		}
		impl.publicKey = this.publicKey;
		return impl;
	}
}
