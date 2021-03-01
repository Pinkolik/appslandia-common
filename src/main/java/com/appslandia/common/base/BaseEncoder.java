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

package com.appslandia.common.base;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.appslandia.common.utils.AssertUtils;

/**
 * 
 * 
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public enum BaseEncoder {

	BASE64, BASE64_NP, BASE64_URL, BASE64_URL_NP, BASE64_MIME, BASE64_MIME_NP;

	public String encode(byte[] message) {
		AssertUtils.assertNotNull(message);
		switch (this) {

		case BASE64:
			return new String(Base64.getEncoder().encode(message), StandardCharsets.ISO_8859_1);
		case BASE64_NP:
			return new String(Base64.getEncoder().withoutPadding().encode(message), StandardCharsets.ISO_8859_1);

		case BASE64_URL:
			return new String(Base64.getUrlEncoder().encode(message), StandardCharsets.ISO_8859_1);
		case BASE64_URL_NP:
			return new String(Base64.getUrlEncoder().withoutPadding().encode(message), StandardCharsets.ISO_8859_1);

		case BASE64_MIME:
			return new String(Base64.getMimeEncoder().encode(message), StandardCharsets.ISO_8859_1);
		case BASE64_MIME_NP:
			return new String(Base64.getMimeEncoder().withoutPadding().encode(message), StandardCharsets.ISO_8859_1);
		default:
			throw new Error();
		}
	}

	public byte[] decode(String encoded) throws IllegalArgumentException {
		AssertUtils.assertNotNull(encoded);
		switch (this) {

		case BASE64:
			return Base64.getDecoder().decode(encoded.getBytes(StandardCharsets.ISO_8859_1));
		case BASE64_NP:
			return Base64.getDecoder().decode(encoded.getBytes(StandardCharsets.ISO_8859_1));

		case BASE64_URL:
			return Base64.getUrlDecoder().decode(encoded.getBytes(StandardCharsets.ISO_8859_1));
		case BASE64_URL_NP:
			return Base64.getUrlDecoder().decode(encoded.getBytes(StandardCharsets.ISO_8859_1));

		case BASE64_MIME:
			return Base64.getMimeDecoder().decode(encoded.getBytes(StandardCharsets.ISO_8859_1));
		case BASE64_MIME_NP:
			return Base64.getMimeDecoder().decode(encoded.getBytes(StandardCharsets.ISO_8859_1));

		default:
			throw new Error();
		}
	}
}
