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

import com.appslandia.common.base.Out;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ParseUtils {

	public static boolean parseBool(String value, boolean defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		if (isTrueValue(value)) {
			return true;
		}
		if (isFalseValue(value)) {
			return false;
		}
		return defaultValue;
	}

	public static int parseInt(String value, int defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		try {
			return Integer.parseInt(value);

		} catch (NumberFormatException ex) {
			return defaultValue;
		}
	}

	public static long parseLong(String value, long defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		try {
			return Long.parseLong(value);

		} catch (NumberFormatException ex) {
			return defaultValue;
		}
	}

	public static float parseFloat(String value, float defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		try {
			float val = Float.parseFloat(value);
			return !Float.isNaN(val) ? val : defaultValue;

		} catch (NumberFormatException ex) {
			return defaultValue;
		}
	}

	public static double parseDouble(String value, double defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		try {
			double val = Double.parseDouble(value);
			return !Double.isNaN(val) ? val : defaultValue;

		} catch (NumberFormatException ex) {
			return defaultValue;
		}
	}

	public static boolean parseBool(String value, Out<Boolean> valid) {
		valid.value = Boolean.FALSE;
		if (value == null) {
			return false;
		}

		valid.value = Boolean.TRUE;
		if (isTrueValue(value)) {
			return true;
		}
		if (isFalseValue(value)) {
			return false;
		}

		valid.value = Boolean.FALSE;
		return false;
	}

	public static int parseInt(String value, Out<Boolean> valid) {
		valid.value = Boolean.FALSE;
		if (value == null) {
			return 0;
		}
		try {
			int val = Integer.parseInt(value);
			valid.value = Boolean.TRUE;
			return val;

		} catch (NumberFormatException ex) {
			return 0;
		}
	}

	public static long parseLong(String value, Out<Boolean> valid) {
		valid.value = Boolean.FALSE;
		if (value == null) {
			return 0L;
		}
		try {
			long val = Long.parseLong(value);
			valid.value = Boolean.TRUE;
			return val;

		} catch (NumberFormatException ex) {
			return 0L;
		}
	}

	public static float parseFloat(String value, Out<Boolean> valid) {
		valid.value = Boolean.FALSE;
		if (value == null) {
			return 0.0f;
		}
		try {
			float val = Float.parseFloat(value);

			valid.value = !Float.isNaN(val);
			return valid.value ? val : 0.0f;

		} catch (NumberFormatException ex) {
			return 0.0f;
		}
	}

	public static double parseDouble(String value, Out<Boolean> valid) {
		valid.value = Boolean.FALSE;
		if (value == null) {
			return 0.0d;
		}
		try {
			double val = Double.parseDouble(value);

			valid.value = !Double.isNaN(val);
			return valid.value ? val : 0.0d;

		} catch (NumberFormatException ex) {
			return 0.0d;
		}
	}

	public static boolean isTrueValue(String value) {
		return "true".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value);
	}

	public static boolean isFalseValue(String value) {
		return "false".equalsIgnoreCase(value) || "no".equalsIgnoreCase(value);
	}
}
