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

package com.appslandia.common.jwt;

import java.util.Arrays;

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.crypto.CryptoException;
import com.appslandia.common.crypto.Digester;
import com.appslandia.common.crypto.DsaDigester;
import com.appslandia.common.crypto.MacDigester;
import com.appslandia.common.utils.AssertUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JwtSigner extends InitializeObject {

	private static final byte[] EMPTY_SIG = new byte[] {};

	public static final JwtSigner NONE = new JwtSigner().setAlg("none");

	private String alg;
	private Digester signer;
	private String kid;

	@Override
	protected void init() throws Exception {
		AssertUtils.assertNotNull(this.alg, "alg is required.");

		if (this != NONE) {
			AssertUtils.assertNotNull(this.signer, "signer is required.");
		}
	}

	public byte[] sign(byte[] message) throws CryptoException {
		this.initialize();
		AssertUtils.assertNotNull(message, "message is required.");

		if (this == NONE) {
			return EMPTY_SIG;
		}
		return this.signer.digest(message);
	}

	public boolean verify(byte[] message, byte[] signature) throws CryptoException {
		this.initialize();
		AssertUtils.assertNotNull(message, "message is required.");
		AssertUtils.assertNotNull(signature, "signature is required.");

		if (this == NONE) {
			return Arrays.equals(signature, EMPTY_SIG);
		}
		return this.signer.verify(message, signature);
	}

	public String getAlg() {
		this.initialize();
		return this.alg;
	}

	public JwtSigner setAlg(String alg) {
		assertNotInitialized();
		this.alg = alg;
		return this;
	}

	public Digester getSigner() {
		this.initialize();
		return this.signer;
	}

	public JwtSigner setSigner(MacDigester signer) {
		assertNotInitialized();
		this.signer = signer;
		return this;
	}

	public JwtSigner setSigner(DsaDigester signer) {
		assertNotInitialized();
		this.signer = signer;
		return this;
	}

	public String getKid() {
		this.initialize();
		return this.kid;
	}

	public JwtSigner setKid(String kid) {
		assertNotInitialized();
		this.kid = kid;
		return this;
	}

	public JwtSigner copy() {
		if (this == NONE) {
			return NONE;
		}
		JwtSigner impl = new JwtSigner();
		impl.alg = this.alg;
		if (this.signer != null) {
			impl.signer = this.signer.copy();
		}
		impl.kid = this.kid;
		return impl;
	}
}
