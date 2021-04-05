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

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ValueUtils {

	public static int valueOrMin(Integer checkValue, int min) {
		if ((checkValue == null) || (checkValue < min)) {
			return min;
		}
		return checkValue;
	}

	public static long valueOrMin(Long checkValue, long min) {
		if ((checkValue == null) || (checkValue < min)) {
			return min;
		}
		return checkValue;
	}

	public static int valueOrMax(Integer checkValue, int max) {
		if ((checkValue == null) || (checkValue > max)) {
			return max;
		}
		return checkValue;
	}

	public static long valueOrMax(Long checkValue, long max) {
		if ((checkValue == null) || (checkValue > max)) {
			return max;
		}
		return checkValue;
	}

	public static int inRange(Integer checkValue, int min, int max) {
		if ((checkValue == null) || (checkValue < min)) {
			return min;
		}
		if (checkValue > max) {
			return max;
		}
		return checkValue;
	}

	public static long inRange(Long checkValue, long min, long max) {
		if ((checkValue == null) || (checkValue < min)) {
			return min;
		}
		if (checkValue > max) {
			return max;
		}
		return checkValue;
	}

	public static double inRange(Double checkValue, double min, double max) {
		if ((checkValue == null) || (checkValue < min)) {
			return min;
		}
		if (checkValue > max) {
			return max;
		}
		return checkValue;
	}

	public static <T> T valueOrAlt(T checkValue, T altValue) {
		return (checkValue != null) ? checkValue : altValue;
	}

	public static int valueOrAlt(int checkValue, int altValue) {
		return (checkValue != 0) ? checkValue : altValue;
	}

	public static long valueOrAlt(long checkValue, long altValue) {
		return (checkValue != 0L) ? checkValue : altValue;
	}

	public static double valueOrAlt(double checkValue, double altValue) {
		return (checkValue != 0.0) ? checkValue : altValue;
	}

	public static boolean isFloatRange(double value) {
		double posDouble = (value >= 0d) ? value : (value * -1d);

		if (posDouble != 0d && (posDouble < Float.MIN_VALUE || posDouble > Float.MAX_VALUE)) {
			return false;
		}
		return true;
	}

	public static boolean isIntRange(long value) {
		if ((value < Integer.MIN_VALUE) || (value > Integer.MAX_VALUE)) {
			return false;
		}
		return true;
	}

	public static boolean isShortRange(long value) {
		if ((value < Short.MIN_VALUE) || (value > Short.MAX_VALUE)) {
			return false;
		}
		return true;
	}

	public static boolean isByteRange(long value) {
		if ((value < Byte.MIN_VALUE) || (value > Byte.MAX_VALUE)) {
			return false;
		}
		return true;
	}
}
