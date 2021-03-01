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

import com.appslandia.common.base.BaseEncoder;
import com.appslandia.common.base.InitializeObject;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class TextBasedCrypto extends InitializeObject {

	protected Charset textCharset;
	protected BaseEncoder baseEncoder;

	public TextBasedCrypto setTextCharset(Charset charset) {
		this.assertNotInitialized();
		this.textCharset = charset;
		return this;
	}

	public TextBasedCrypto setTextCharset(String textCharset) {
		this.assertNotInitialized();
		if (textCharset != null) {
			this.textCharset = Charset.forName(textCharset);
		}
		return this;
	}

	public TextBasedCrypto setBaseEncoder(BaseEncoder baseEncoder) {
		this.assertNotInitialized();
		this.baseEncoder = baseEncoder;
		return this;
	}

	public TextBasedCrypto setBaseEncoder(String baseEncoder) {
		this.assertNotInitialized();
		if (baseEncoder != null) {
			this.baseEncoder = BaseEncoder.valueOf(baseEncoder);
		}
		return this;
	}
}
