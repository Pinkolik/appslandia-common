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

import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.Arrays;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.appslandia.common.base.DestroyException;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.SYS;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class KeyStoreUtil extends InitializeObject {

	public static final String TYPE_JCEKS = "JCEKS";
	public static final String TYPE_JKS = "JKS";
	public static final String TYPE_PKCS12 = "PKCS12";

	private String type, provider;
	private KeyStore keyStore;

	private char[] password;
	private InputStream inputStream;

	private KeyStore.ProtectionParameter protectionParameter = null;

	public KeyStoreUtil() {
	}

	public KeyStoreUtil(String type) {
		this.type = type;
	}

	public KeyStoreUtil(String type, String provider) {
		this.type = type;
		this.provider = provider;
	}

	@Override
	protected void init() throws Exception {
		AssertUtils.assertNotNull(this.type, "type is required.");

		if (this.provider == null) {
			this.keyStore = KeyStore.getInstance(this.type);
		} else {
			this.keyStore = KeyStore.getInstance(this.type, this.provider);
		}
		this.keyStore.load(this.inputStream, this.password);
	}

	@Override
	public void destroy() throws DestroyException {
		CryptoUtils.clear(this.password);
		CryptoUtils.destroyQuietly(this.protectionParameter);
	}

	public KeyStore getKeyStore() {
		initialize();
		return this.keyStore;
	}

	public SecretKey getSecretKey(String alias) throws CryptoException {
		initialize();
		try {
			return (SecretKey) this.keyStore.getEntry(alias, this.getProtectionParameter());
		} catch (GeneralSecurityException ex) {
			throw new CryptoException(ex);
		}
	}

	public KeyStoreUtil setSecretKey(String alias, SecretKey key) throws CryptoException {
		initialize();
		try {
			this.keyStore.setEntry(alias, new KeyStore.SecretKeyEntry(key), this.getProtectionParameter());
			return this;
		} catch (GeneralSecurityException ex) {
			throw new CryptoException(ex);
		}
	}

	public KeyStoreUtil setSecretKey(String alias, byte[] key, String algorithm) throws CryptoException {
		initialize();
		try {
			this.keyStore.setEntry(alias, new KeyStore.SecretKeyEntry(new SecretKeySpec(key, algorithm)), this.getProtectionParameter());
			return this;
		} catch (GeneralSecurityException ex) {
			throw new CryptoException(ex);
		}
	}

	public KeyStoreUtil setType(String type) {
		assertNotInitialized();
		this.type = type;
		return this;
	}

	public KeyStoreUtil setProvider(String provider) {
		assertNotInitialized();
		this.provider = provider;
		return this;
	}

	public KeyStoreUtil setPassword(char[] password) {
		assertNotInitialized();
		if (password != null) {
			this.password = Arrays.copyOf(password, password.length);
		}
		return this;
	}

	public KeyStoreUtil setPassword(String passwordOrEnv) throws IllegalStateException {
		assertNotInitialized();

		if (passwordOrEnv != null) {
			String resolvedValue = SYS.resolveExpr(passwordOrEnv);
			this.password = resolvedValue.toCharArray();
		}
		return this;
	}

	public KeyStoreUtil setInputStream(InputStream inputStream) {
		assertNotInitialized();
		this.inputStream = inputStream;
		return this;
	}

	public KeyStoreUtil setProtectionParameter(KeyStore.ProtectionParameter protectionParameter) {
		assertNotInitialized();
		this.protectionParameter = protectionParameter;
		return this;
	}

	public KeyStoreUtil setProtectionParameter(char[] password) {
		assertNotInitialized();
		if (password != null) {
			this.protectionParameter = new KeyStore.PasswordProtection(password);
		}
		return this;
	}

	public KeyStoreUtil setProtectionParameter(String passwordOrEnv) throws IllegalStateException {
		assertNotInitialized();

		if (passwordOrEnv != null) {
			String resolvedValue = SYS.resolveExpr(passwordOrEnv);
			this.protectionParameter = new KeyStore.PasswordProtection(resolvedValue.toCharArray());
		}
		return this;
	}

	protected KeyStore.ProtectionParameter getProtectionParameter() {
		return AssertUtils.assertNotNull(this.protectionParameter, "protectionParameter is required.");
	}
}
