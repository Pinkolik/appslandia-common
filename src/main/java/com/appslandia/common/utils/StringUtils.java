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

import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.regex.Pattern;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class StringUtils {

	public static final String EMPTY_STRING = "";
	public static final String[] EMPTY_ARRAY = {};

	public static final String DOUBLE_LINE_SEP = System.lineSeparator() + System.lineSeparator();

	public static String toLowerCase(String str, Locale locale) {
		if (str == null) {
			return null;
		}
		return str.toLowerCase(locale);
	}

	public static String toUpperCase(String str, Locale locale) {
		if (str == null) {
			return null;
		}
		return str.toUpperCase(locale);
	}

	public static String firstLowerCase(String str, Locale locale) {
		if (str == null) {
			return null;
		}
		if (!str.isEmpty()) {
			StringBuilder sb = new StringBuilder(str.length());
			return sb.append(str.substring(0, 1).toLowerCase(locale)).append(str.substring(1)).toString();
		}
		return str;
	}

	public static String firstUpperCase(String str, Locale locale) {
		if (str == null) {
			return null;
		}
		if (!str.isEmpty()) {
			StringBuilder sb = new StringBuilder(str.length());
			return sb.append(str.substring(0, 1).toUpperCase(locale)).append(str.substring(1)).toString();
		}
		return str;
	}

	public static String trimToNull(String str) {
		return trimToDefault(str, null);
	}

	public static String trimToEmpty(String str) {
		return (str != null) ? str.trim() : EMPTY_STRING;
	}

	public static String trimToDefault(String str, String defaultValue) {
		if (str == null) {
			return defaultValue;
		}
		str = str.trim();
		return !str.isEmpty() ? str : defaultValue;
	}

	public static String trimToNull(String str, char charToTrim) {
		if (str == null) {
			return null;
		}
		int start = -1;
		while ((++start < str.length()) && (str.charAt(start) == charToTrim)) {
		}

		int end = str.length();
		while ((--end >= 0) && (str.charAt(end) == charToTrim)) {
		}
		if (start > end) {
			return null;
		}
		return str.substring(start, end + 1);
	}

	public static String nullOrLowerCase(String str, Locale locale) {
		if (str == null) {
			return null;
		}
		return !str.isEmpty() ? str.toLowerCase(locale) : null;
	}

	public static String nullOrUpperCase(String str, Locale locale) {
		if (str == null) {
			return null;
		}
		return !str.isEmpty() ? str.toUpperCase(locale) : null;
	}

	public static boolean isNullOrEmpty(String str) {
		return (str == null) || str.isEmpty();
	}

	private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");

	public static boolean isNullOrBlank(String str) {
		return (str == null) || str.isEmpty() || WHITESPACE_PATTERN.matcher(str).matches();
	}

	public static boolean startsWithIgnoreCase(String str, String substr) {
		AssertUtils.assertNotNull(str);
		AssertUtils.assertNotNull(substr);

		if (substr.isEmpty()) {
			return true;
		}
		if (str.regionMatches(true, 0, substr, 0, substr.length())) {
			return true;
		}
		return false;
	}

	public static boolean endsWithIgnoreCase(String str, String substr) {
		AssertUtils.assertNotNull(str);
		AssertUtils.assertNotNull(substr);

		if (substr.isEmpty()) {
			return true;
		}
		if (str.regionMatches(true, str.length() - substr.length(), substr, 0, substr.length())) {
			return true;
		}
		return false;
	}

	public static boolean containsIgnoreCase(String str, String substr) {
		AssertUtils.assertNotNull(str);
		AssertUtils.assertNotNull(substr);

		if (substr.isEmpty()) {
			return true;
		}
		int maxOffset = str.length() - substr.length();
		for (int offset = 0; offset <= maxOffset; offset++) {
			if (str.regionMatches(true, offset, substr, 0, substr.length())) {
				return true;
			}
		}
		return false;
	}

	private static final Pattern NON_DIGITS_PATTERN = Pattern.compile("[^\\d]+");

	public static String removeNonDigits(String str) {
		if (str == null) {
			return null;
		}
		str = NON_DIGITS_PATTERN.matcher(str).replaceAll(StringUtils.EMPTY_STRING);
		return !str.isEmpty() ? str : null;
	}

	public static String join(char delimiter, boolean willWrap, Iterable<? extends CharSequence> elements) {
		Objects.requireNonNull(elements);

		if (elements instanceof Collection) {
			Collection<? extends CharSequence> col = ObjectUtils.cast(elements);
			if (col.isEmpty()) {
				return null;
			}
		}

		String sep = new String(new char[] { delimiter });
		StringJoiner joiner = willWrap ? new StringJoiner(sep, sep, sep) : new StringJoiner(sep);

		for (CharSequence cs : elements) {
			joiner.add(cs);
		}
		return joiner.toString();
	}

	public static String join(char delimiter, boolean willWrap, CharSequence... elements) {
		Objects.requireNonNull(elements);

		if (elements.length == 0) {
			return null;
		}

		String sep = new String(new char[] { delimiter });
		StringJoiner joiner = willWrap ? new StringJoiner(sep, sep, sep) : new StringJoiner(sep);

		for (CharSequence cs : elements) {
			joiner.add(cs);
		}
		return joiner.toString();
	}
}
