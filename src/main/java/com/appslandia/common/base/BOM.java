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

import java.util.Arrays;

import com.appslandia.common.utils.AssertUtils;

/**
 * 
 * 
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public enum BOM {
	// @formatter:off

	UTF_8("UTF-8", new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF }),

	UTF_16BE("UTF-16BE", new byte[] { (byte) 0xFE, (byte) 0xFF }), 
	UTF_16LE("UTF-16LE", new byte[] { (byte) 0xFF, (byte) 0xFE }),

	UTF_32BE("UTF-32BE", new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF }), 
	UTF_32LE("UTF-32LE", new byte[] { (byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00 });

	// @formatter:on

	final String encoding;
	final byte[] bytes;

	BOM(String encoding, byte[] bytes) {
		this.encoding = encoding;
		this.bytes = bytes;
	}

	public String getEncoding() {
		return this.encoding;
	}

	public byte[] getBytes() {
		return Arrays.copyOf(this.bytes, this.bytes.length);
	}

	public int length() {
		return this.bytes.length;
	}

	public static BOM parse(String encoding) {
		if (encoding == null) {
			return null;
		}
		for (BOM bom : BOM.values()) {
			if (bom.encoding.equalsIgnoreCase(encoding)) {
				return bom;
			}
		}
		return null;
	}

	public static BOM parse(byte[] bom, int c) {
		AssertUtils.assertTrue(bom.length == 4);
		AssertUtils.assertTrue(c <= 4);

		if (c == 4) {
			if (BOM.UTF_32BE.matches(bom)) {
				return BOM.UTF_32BE;
			}
			if (BOM.UTF_32LE.matches(bom)) {
				return BOM.UTF_32LE;
			}
		}
		if (c >= 3) {
			if (BOM.UTF_8.matches(bom)) {
				return BOM.UTF_8;
			}
		}
		if (c >= 2) {
			if (BOM.UTF_16BE.matches(bom)) {
				return BOM.UTF_16BE;
			}
			if (BOM.UTF_16LE.matches(bom)) {
				return BOM.UTF_16LE;
			}
		}
		return null;
	}

	private boolean matches(byte[] bom) {
		for (int i = 0; i < length(); i++) {
			if (this.bytes[i] != bom[i]) {
				return false;
			}
		}
		return true;
	}
}
