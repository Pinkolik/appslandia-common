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

import java.math.BigDecimal;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class DecimalUtils {

	private static final double POSITIVE_ZERO = 0d;

	public static double round(double x, int scale) {
		return round(x, scale, BigDecimal.ROUND_HALF_UP);
	}

	public static double round(double x, int scale, int roundingMode) {
		try {
			double roundedValue = (new BigDecimal(Double.toString(x)).setScale(scale, roundingMode)).doubleValue();
			return fixSign(roundedValue, x);

		} catch (NumberFormatException ex) {
			if (Double.isInfinite(x)) {
				return x;
			} else {
				return Double.NaN;
			}
		}
	}

	// Negative values rounded to zero should result in negative zero
	public static double fixSign(double roundedValue, double x) {
		return roundedValue == POSITIVE_ZERO ? POSITIVE_ZERO * x : roundedValue;
	}

	public static boolean isDifferent(double d1, double d2, double delta) {
		if (Double.compare(d1, d2) == 0) {
			return false;
		}
		if ((Math.abs(d1 - d2) <= delta)) {
			return false;
		}
		return true;
	}

	public static boolean isDifferent(float f1, float f2, float delta) {
		if (Float.compare(f1, f2) == 0) {
			return false;
		}
		if ((Math.abs(f1 - f2) <= delta)) {
			return false;
		}
		return true;
	}
}
