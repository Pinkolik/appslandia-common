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
import com.appslandia.common.utils.ArrayUtils;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.MathUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class SignerEncryptor extends InitializeObject implements Encryptor {

	private Encryptor encryptor;
	private Digester signer;

	public SignerEncryptor() {
	}

	public SignerEncryptor(Encryptor encryptor) {
		this.encryptor = encryptor;
	}

	public SignerEncryptor(Encryptor encryptor, Digester signer) {
		this.encryptor = encryptor;
		this.signer = signer;
	}

	@Override
	protected void init() throws Exception {
		AssertUtils.assertNotNull(this.encryptor, "encryptor is required.");
	}

	@Override
	public void destroy() throws DestroyException {
		if (this.encryptor != null) {
			this.encryptor.destroy();
		}
		if (this.signer != null) {
			this.signer.destroy();
		}
	}

	@Override
	public byte[] encrypt(byte[] message) throws CryptoException {
		this.initialize();
		AssertUtils.assertNotNull(message, "message is required.");

		if (this.signer == null) {
			return this.encryptor.encrypt(message);
		}

		byte[] encMsg = this.encryptor.encrypt(message);
		byte[] signature = this.signer.digest(encMsg);

		if (this.signer.getDigestSize() > 0) {
			return ArrayUtils.append(signature, encMsg);

		} else {
			byte[] sigLen = MathUtils.toByteArray(signature.length);
			return ArrayUtils.append(sigLen, signature, encMsg);
		}
	}

	@Override
	public byte[] decrypt(byte[] message) throws CryptoException {
		this.initialize();
		AssertUtils.assertNotNull(message, "message is required.");

		if (this.signer == null) {
			return this.encryptor.decrypt(message);
		}

		byte[] signature = null;
		byte[] encMsg = null;

		int sigLen = this.signer.getDigestSize();
		if (sigLen > 0) {
			signature = new byte[sigLen];

			AssertUtils.assertTrue(message.length > sigLen, "message is invalid.");
			encMsg = new byte[message.length - sigLen];
			ArrayUtils.copy(message, signature, encMsg);

		} else {
			byte[] len = new byte[4];
			AssertUtils.assertTrue(message.length > 4, "message is invalid.");
			ArrayUtils.copy(message, len);
			sigLen = MathUtils.toInt(len);

			AssertUtils.assertTrue(message.length > 4 + sigLen, "message is invalid.");
			signature = new byte[sigLen];
			encMsg = new byte[message.length - sigLen - 4];

			System.arraycopy(message, 4, signature, 0, sigLen);
			System.arraycopy(message, 4 + sigLen, encMsg, 0, encMsg.length);
		}

		if (!this.signer.verify(encMsg, signature)) {
			throw new CryptoException("message was tampered.");
		}
		return this.encryptor.decrypt(encMsg);
	}

	public SignerEncryptor setEncryptor(Encryptor encryptor) {
		assertNotInitialized();
		this.encryptor = encryptor;
		return this;
	}

	public SignerEncryptor setSigner(Digester signer) {
		assertNotInitialized();
		this.signer = signer;
		return this;
	}

	@Override
	public SignerEncryptor copy() {
		SignerEncryptor impl = new SignerEncryptor();
		if (this.encryptor != null) {
			impl.encryptor = this.encryptor.copy();
		}
		if (this.signer != null) {
			impl.signer = this.signer.copy();
		}
		return impl;
	}
}
