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

import java.util.Arrays;

import javax.security.auth.DestroyFailedException;
import javax.security.auth.Destroyable;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class CryptoUtils {

	public static void clear(char[] chars) {
		if (chars != null) {
			Arrays.fill(chars, ('0'));
		}
	}

	public static void clear(byte[] bytes) {
		if (bytes != null) {
			Arrays.fill(bytes, (byte) 0);
		}
	}

	public static char[] toCharArray(byte[] src) {
		char[] arr = new char[src.length / 2];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (char) (((0xff & (src[i * 2])) << 8) | (0xff & src[i * 2 + 1]));
		}
		return arr;
	}

	public static byte[] toByteArray(char[] src) {
		byte[] arr = new byte[src.length * 2];
		for (int i = 0; i < src.length; i++) {
			char chr = src[i];
			arr[i * 2] = (byte) (0xff & (chr >> 8));
			arr[i * 2 + 1] = (byte) (0xff & (chr));
		}
		return arr;
	}

	public static void destroyQuietly(Object destroyable) {
		if (!(destroyable instanceof Destroyable)) {
			return;
		}
		Destroyable obj = (Destroyable) destroyable;
		if (!obj.isDestroyed()) {
			try {
				obj.destroy();
			} catch (DestroyFailedException ig) {
			}
		}
	}

	private static final String ENC_BEGIN = "ENC(";
	private static final String ENC_END = ")";

	public static String markEncValue(String value) {
		return ENC_BEGIN + value + ENC_END;
	}

	public static boolean isEncValue(String value) {
		return value.startsWith(ENC_BEGIN) && value.endsWith(ENC_END);
	}

	public static String parseEncValue(String value) {
		return value.substring(ENC_BEGIN.length(), value.length() - ENC_END.length());
	}
}
