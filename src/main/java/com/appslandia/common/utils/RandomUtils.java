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

import java.security.SecureRandom;
import java.util.Random;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class RandomUtils {

	public static byte[] nextBytes(int length) {
		return nextBytes(length, new SecureRandom());
	}

	public static byte[] nextBytes(int length, Random random) {
		byte[] bytes = new byte[length];
		random.nextBytes(bytes);
		return bytes;
	}

	public static int nextInt(int min, int max) {
		return nextInt(min, max, new Random());
	}

	public static int nextInt(int min, int max, Random random) {
		return min + (int) (random.nextFloat() * (max - min + 1));
	}

	public static int[] nextIndexes(int n) {
		return nextIndexes(n, new Random());
	}

	public static int[] nextIndexes(int n, Random random) {
		int[] indexes = new int[n];
		for (int i = 0; i < n; i++) {
			indexes[i] = i;
		}
		ArrayUtils.shuffle(indexes, random);
		return indexes;
	}

	public static int rdCompare() {
		return rdCompare(new Random());
	}

	public static int rdCompare(Random random) {
		return nextInt(-1, 1, random);
	}
}
