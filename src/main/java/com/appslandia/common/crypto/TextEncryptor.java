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
import java.nio.charset.StandardCharsets;

import com.appslandia.common.base.BaseEncoder;
import com.appslandia.common.base.DestroyException;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.ValueUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class TextEncryptor extends TextBasedCrypto {
	private Encryptor encryptor;

	public TextEncryptor() {
	}

	public TextEncryptor(Encryptor encryptor) {
		this.encryptor = encryptor;
	}

	@Override
	protected void init() throws Exception {
		AssertUtils.assertNotNull(this.encryptor, "encryptor is required.");

		this.textCharset = ValueUtils.valueOrAlt(this.textCharset, StandardCharsets.UTF_8);
		this.baseEncoder = ValueUtils.valueOrAlt(this.baseEncoder, BaseEncoder.BASE64);
	}

	public String encrypt(String message) throws CryptoException {
		this.initialize();
		AssertUtils.assertNotNull(message, "message is required.");

		return this.baseEncoder.encode(this.encryptor.encrypt(message.getBytes(this.textCharset)));
	}

	public String decrypt(String message) throws CryptoException {
		this.initialize();
		AssertUtils.assertNotNull(message, "message is required.");

		return new String(this.encryptor.decrypt(this.baseEncoder.decode(message)), this.textCharset);
	}

	@Override
	public void destroy() throws DestroyException {
		if (this.encryptor != null) {
			this.encryptor.destroy();
		}
	}

	public TextEncryptor setEncryptor(Encryptor encryptor) {
		this.assertNotInitialized();
		this.encryptor = encryptor;
		return this;
	}

	@Override
	public TextEncryptor setTextCharset(Charset charset) {
		super.setTextCharset(charset);
		return this;
	}

	@Override
	public TextEncryptor setTextCharset(String textCharset) {
		super.setTextCharset(textCharset);
		return this;
	}

	@Override
	public TextEncryptor setBaseEncoder(BaseEncoder baseEncoder) {
		super.setBaseEncoder(baseEncoder);
		return this;
	}

	public TextEncryptor copy() {
		TextEncryptor impl = new TextEncryptor();
		impl.setTextCharset(this.textCharset).setBaseEncoder(this.baseEncoder);
		if (this.encryptor != null) {
			impl.encryptor = this.encryptor.copy();
		}
		return impl;
	}
}
