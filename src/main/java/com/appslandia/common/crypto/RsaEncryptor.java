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
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.MGF1ParameterSpec;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;

import com.appslandia.common.base.DestroyException;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.AssertUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class RsaEncryptor extends InitializeObject implements Encryptor {
	private String transformation, provider;

	private PublicKey publicKey;
	private PrivateKey privateKey;

	private Cipher encrypt;
	private Cipher decrypt;

	final Object encMutex = new Object();
	final Object decMutex = new Object();
	final Random random = new SecureRandom();

	@Override
	protected void init() throws Exception {
		// Algorithm
		AssertUtils.assertNotNull(this.transformation, "transformation is required.");

		String[] trans = this.transformation.split("/");
		AssertUtils.assertTrue("RSA".equalsIgnoreCase(trans[0]), "RSA algorithm is required.");

		String padding = (trans.length == 3) ? trans[2] : null;
		AlgorithmParameterSpec paramSpec = (padding != null) ? parseParamSpec(padding) : null;

		AssertUtils.assertTrue((this.privateKey != null) || (this.publicKey != null), "No key is provided.");

		// ENCRYPT
		if (this.publicKey != null) {
			if (this.provider == null) {
				this.encrypt = Cipher.getInstance(this.transformation);
			} else {
				this.encrypt = Cipher.getInstance(this.transformation, this.provider);
			}
			this.encrypt.init(Cipher.ENCRYPT_MODE, this.publicKey, paramSpec);
		}

		// DECRYPT
		if (this.privateKey != null) {
			if (this.provider == null) {
				this.decrypt = Cipher.getInstance(this.transformation);
			} else {
				this.decrypt = Cipher.getInstance(this.transformation, this.provider);
			}
			this.decrypt.init(Cipher.DECRYPT_MODE, this.privateKey, paramSpec);
		}
	}

	@Override
	public void destroy() throws DestroyException {
		CryptoUtils.destroyQuietly(this.privateKey);
	}

	@Override
	public byte[] encrypt(byte[] message) throws CryptoException {
		this.initialize();
		AssertUtils.assertNotNull(message, "message is required.");
		AssertUtils.assertNotNull(this.encrypt, "publicKey is required.");

		try {
			synchronized (this.encMutex) {
				return this.encrypt.doFinal(message);
			}
		} catch (GeneralSecurityException ex) {
			throw new CryptoException(ex);
		}
	}

	@Override
	public byte[] decrypt(byte[] message) throws CryptoException {
		this.initialize();
		AssertUtils.assertNotNull(message, "message is required.");
		AssertUtils.assertNotNull(this.decrypt, "privateKey is required.");

		try {
			synchronized (this.decMutex) {
				return this.decrypt.doFinal(message);
			}
		} catch (GeneralSecurityException ex) {
			throw new CryptoException(ex);
		}
	}

	public RsaEncryptor setTransformation(String transformation) {
		this.assertNotInitialized();
		this.transformation = transformation;
		return this;
	}

	public RsaEncryptor setProvider(String provider) {
		this.assertNotInitialized();
		this.provider = provider;
		return this;
	}

	public RsaEncryptor setPrivateKey(PrivateKey privateKey) {
		assertNotInitialized();
		this.privateKey = privateKey;
		return this;
	}

	public RsaEncryptor setPrivateKey(String privateKeyPem) {
		assertNotInitialized();
		if (privateKeyPem != null) {
			this.privateKey = KeyFactoryUtil.RSA.toPrivateKey(privateKeyPem);
		}
		return this;
	}

	public RsaEncryptor setPublicKey(PublicKey publicKey) {
		assertNotInitialized();
		this.publicKey = publicKey;
		return this;
	}

	public RsaEncryptor setPublicKey(String publicKeyPem) {
		assertNotInitialized();
		if (publicKeyPem != null) {
			this.publicKey = KeyFactoryUtil.RSA.toPublicKey(publicKeyPem);
		}
		return this;
	}

	@Override
	public RsaEncryptor copy() {
		RsaEncryptor impl = new RsaEncryptor().setTransformation(this.transformation).setProvider(this.provider);

		if (this.privateKey != null) {
			impl.privateKey = new KeyFactoryUtil(this.privateKey.getAlgorithm()).copy(this.privateKey);
		}
		impl.publicKey = this.publicKey;
		return impl;
	}

	static AlgorithmParameterSpec parseParamSpec(String padding) throws NoSuchPaddingException {
		if (padding.equalsIgnoreCase("NoPadding") || padding.equalsIgnoreCase("PKCS1Padding")) {
			return null;
		}
		if (padding.equalsIgnoreCase("OAEPPadding")) {
			return OAEPParameterSpec.DEFAULT;
		}
		if (padding.equalsIgnoreCase("OAEPWithMD5AndMGF1Padding")) {
			return new OAEPParameterSpec("MD5", "MGF1", new MGF1ParameterSpec("MD5"), PSource.PSpecified.DEFAULT);
		}
		if (padding.equalsIgnoreCase("OAEPWithSHA-1AndMGF1Padding")) {
			return new OAEPParameterSpec("SHA-1", "MGF1", MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT);
		}
		if (padding.equalsIgnoreCase("OAEPWithSHA-224AndMGF1Padding")) {
			return new OAEPParameterSpec("SHA-224", "MGF1", MGF1ParameterSpec.SHA224, PSource.PSpecified.DEFAULT);
		}
		if (padding.equalsIgnoreCase("OAEPWithSHA-256AndMGF1Padding")) {
			return new OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT);
		}
		if (padding.equalsIgnoreCase("OAEPWithSHA-384AndMGF1Padding")) {
			return new OAEPParameterSpec("SHA-384", "MGF1", MGF1ParameterSpec.SHA384, PSource.PSpecified.DEFAULT);
		}
		if (padding.equalsIgnoreCase("OAEPWithSHA-512AndMGF1Padding")) {
			return new OAEPParameterSpec("SHA-512", "MGF1", MGF1ParameterSpec.SHA512, PSource.PSpecified.DEFAULT);
		}
		throw new NoSuchPaddingException(padding + " unavailable with RSA.");
	}
}
