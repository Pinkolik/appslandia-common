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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import com.appslandia.common.base.PropertyConfig;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class SecureConfig extends PropertyConfig {
	private static final long serialVersionUID = 1L;

	final TextEncryptor textEncryptor;

	public SecureConfig(char[] password) {
		AssertUtils.assertNotNull(password);
		this.textEncryptor = new TextEncryptor(new PbeEncryptor().setTransformation("AES/CBC/PKCS5Padding").setKeySize(32).setPassword(password));
	}

	public SecureConfig(String password) {
		AssertUtils.assertNotNull(password);
		this.textEncryptor = new TextEncryptor(new PbeEncryptor().setTransformation("AES/CBC/PKCS5Padding").setKeySize(32).setPassword(password));
	}

	public SecureConfig(Encryptor encryptor) {
		AssertUtils.assertNotNull(encryptor);
		this.textEncryptor = new TextEncryptor(encryptor);
	}

	public SecureConfig(TextEncryptor textEncryptor) {
		AssertUtils.assertNotNull(textEncryptor);
		this.textEncryptor = textEncryptor;
	}

	@Override
	public SecureConfig load(InputStream is) throws IOException {
		super.load(is);
		return this;
	}

	@Override
	public SecureConfig load(Reader r) throws IOException {
		super.load(r);
		return this;
	}

	@Override
	public SecureConfig load(File file) throws IOException {
		super.load(file);
		return this;
	}

	@Override
	public SecureConfig load(String file) throws IOException {
		super.load(file);
		return this;
	}

	@Override
	public String getString(String key) {
		String value = super.getString(key);
		if (value == null) {
			return null;
		}
		if (!CryptoUtils.isEncValue(value)) {
			return value;
		}
		return this.textEncryptor.decrypt(CryptoUtils.parseEncValue(value));
	}

	// Unsecured
	public SecureConfig set(String key, String value) {
		super.set(key, value);
		return this;
	}

	public SecureConfig set(String key, boolean value) {
		super.set(key, value);
		return this;
	}

	public SecureConfig set(String key, int value) {
		super.set(key, value);
		return this;
	}

	public SecureConfig set(String key, long value) {
		super.set(key, value);
		return this;
	}

	public SecureConfig set(String key, double value) {
		super.set(key, value);
		return this;
	}

	// Secured
	public SecureConfig enc(String key, String value) throws CryptoException {
		value = StringUtils.trimToNull(value);
		this.map.put(key, (value != null) ? CryptoUtils.markEncValue(this.textEncryptor.encrypt(value)) : null);
		return this;
	}

	public SecureConfig enc(String key, boolean value) throws CryptoException {
		this.map.put(key, CryptoUtils.markEncValue(this.textEncryptor.encrypt(Boolean.toString(value))));
		return this;
	}

	public SecureConfig enc(String key, int value) throws CryptoException {
		this.map.put(key, CryptoUtils.markEncValue(this.textEncryptor.encrypt(Integer.toString(value))));
		return this;
	}

	public SecureConfig enc(String key, long value) throws CryptoException {
		this.map.put(key, CryptoUtils.markEncValue(this.textEncryptor.encrypt(Long.toString(value))));
		return this;
	}

	public SecureConfig enc(String key, float value) throws CryptoException {
		this.map.put(key, CryptoUtils.markEncValue(this.textEncryptor.encrypt(Float.toString(value))));
		return this;
	}

	public SecureConfig enc(String key, double value) throws CryptoException {
		this.map.put(key, CryptoUtils.markEncValue(this.textEncryptor.encrypt(Double.toString(value))));
		return this;
	}
}
