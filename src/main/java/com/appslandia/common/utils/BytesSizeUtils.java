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

import java.util.Locale;
import java.util.regex.Pattern;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class BytesSizeUtils {

	private static final Pattern BYTES_SIZE_PATTERN = Pattern.compile("((\\d+.\\d+|\\d+)(TB|GB|MB|KB|B)\\s*)+", Pattern.CASE_INSENSITIVE);

	public static long translateToBytes(String sizeAmt, boolean base10) throws IllegalArgumentException {
		sizeAmt = StringUtils.trimToNull(sizeAmt);
		AssertUtils.assertNotNull(sizeAmt, "sizeAmt is required.");

		if (!BYTES_SIZE_PATTERN.matcher(sizeAmt).matches()) {
			throw new IllegalArgumentException("sizeAmt is invalid (value=" + sizeAmt + ")");
		}

		double result = 0L;
		int i = 0;
		while (i < sizeAmt.length()) {
			int j = i;
			while (Character.isDigit(sizeAmt.charAt(j)) || (sizeAmt.charAt(j) == '.'))
				j++;
			int k = j;
			while ((k <= sizeAmt.length() - 1) && (Character.isLetter(sizeAmt.charAt(k)) || sizeAmt.charAt(k) == (' ')))
				k++;

			double amt = Double.parseDouble(sizeAmt.substring(i, j));
			String unit = sizeAmt.substring(j, k).trim().toUpperCase(Locale.ENGLISH);

			switch (unit) {
			case "GB":
				result += amt * (base10 ? 1000_000_000 : 1_073_741_824);
				break;
			case "MB":
				result += amt * (base10 ? 1000_000 : 1_048_576);
				break;
			case "KB":
				result += amt * (base10 ? 1000 : 1024);
				break;
			default:
				result += amt;
				break;
			}
			i = k;
		}
		return (long) Math.ceil(result);
	}
}
