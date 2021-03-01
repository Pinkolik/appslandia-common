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

package com.appslandia.common.utils;

import java.util.Comparator;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class MathUtils {

	public static boolean isPow2(long n) {
		if ((n < 1) || (n > Long.MAX_VALUE / 2 + 1)) {
			return false;
		}
		return (n & (n - 1)) == 0;
	}

	public static int ceil(int n, int base) {
		AssertUtils.assertTrue(n >= 0);
		AssertUtils.assertTrue(base > 0);
		return ((n + base - 1) / base) * base;
	}

	public static byte[] toByteArray(int value) {
		byte[] bytes = new byte[4];
		for (int i = 3; i >= 0; i--) {
			bytes[i] = (byte) (value & 0xff);
			value >>= 8;
		}
		return bytes;
	}

	public static int toInt(byte[] bytes) {
		// @formatter:off
	    return (bytes[0] & 0xff) << 24
	         | (bytes[1] & 0xff) << 16
	         | (bytes[2] & 0xff) << 8
	         | (bytes[3] & 0xff);
	    // @formatter:on
	}

	public static byte[] toByteArray(long value) {
		byte[] bytes = new byte[8];
		for (int i = 7; i >= 0; i--) {
			bytes[i] = (byte) (value & 0xffL);
			value >>= 8;
		}
		return bytes;
	}

	public static long toLong(byte[] bytes) {
		// @formatter:off
	    return (bytes[0] & 0xffL) << 56
	         | (bytes[1] & 0xffL) << 48
	         | (bytes[2] & 0xffL) << 40
	         | (bytes[3] & 0xffL) << 32
	         | (bytes[4] & 0xffL) << 24
	         | (bytes[5] & 0xffL) << 16
	         | (bytes[6] & 0xffL) << 8
	         | (bytes[7] & 0xffL);
	    // @formatter:on
	}

	public static byte[] toByteArray(int begin, int end) {
		AssertUtils.assertTrue(begin <= end);

		byte[] byteArray = new byte[end - begin + 1];
		for (int i = begin; i <= end; i++) {
			byteArray[i - begin] = (byte) i;
		}
		return byteArray;
	}

	public static <T> T min(T a, T b, Comparator<T> comparator) {
		return comparator.compare(a, b) <= 0 ? a : b;
	}

	public static <T> T max(T a, T b, Comparator<T> comparator) {
		return comparator.compare(a, b) <= 0 ? b : a;
	}
}
