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

package com.appslandia.common.jwt;

import java.lang.reflect.Array;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import com.appslandia.common.utils.AssertUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JwtUtils {

	private static final Pattern JWT_SEP_PATTERN = Pattern.compile("\\.");

	public static String[] parseParts(String token) {
		AssertUtils.assertNotNull(token);
		String[] parts = JWT_SEP_PATTERN.split(token);

		if (parts.length == 2) {
			return token.endsWith(".") ? new String[] { parts[0], parts[1], null } : null;
		}

		if (parts.length != 3) {
			return null;
		}
		return parts;
	}

	// number of seconds from 1970-01-01T00:00:00Z UTC until the specified UTC
	// date/time
	public static Long toNumericDate(Date value) {
		if (value == null) {
			return null;
		}
		return value.getTime() / 1000;
	}

	public static Long toNumericDate(Long timeInMs) {
		if (timeInMs == null) {
			return null;
		}
		return timeInMs / 1000;
	}

	public static boolean isFutureTime(long numericDate, int leewaySec) {
		return (System.currentTimeMillis() / 1000) - leewaySec < numericDate;
	}

	public static boolean isPastTime(long numericDate, int leewaySec) {
		return (System.currentTimeMillis() / 1000) + leewaySec >= numericDate;
	}

	public static Date toDate(Long numericDate) {
		if (numericDate == null) {
			return null;
		}
		return new Date(numericDate * 1000);
	}

	public static boolean isSupportedValue(Object value) {
		AssertUtils.assertNotNull(value);

		if (value instanceof List) {
			return validateList((List<?>) value);
		}
		if (value instanceof Map) {
			return validateMap((Map<?, ?>) value);
		}

		Class<?> type = value.getClass();
		if (type.isArray()) {
			return validateArray(value);
		}
		return isBasicType(type);
	}

	private static boolean validateMap(Map<?, ?> map) {
		for (Entry<?, ?> entry : map.entrySet()) {
			// Key
			if ((entry.getKey() == null) || (entry.getKey().getClass() != String.class)) {
				return false;
			}
			// Value
			if ((entry.getValue() != null) && !isSupportedValue(entry.getValue())) {
				return false;
			}
		}
		return true;
	}

	private static boolean validateList(List<?> list) {
		for (Object value : list) {

			// Value
			if ((value != null) && !isSupportedValue(value)) {
				return false;
			}
		}
		return true;
	}

	private static boolean validateArray(Object array) {
		int len = Array.getLength(array);
		for (int i = 0; i < len; i++) {
			Object value = Array.get(array, i);

			// Value
			if ((value != null) && !isSupportedValue(value)) {
				return false;
			}
		}
		return true;
	}

	private static boolean isBasicType(Class<?> c) {
		return (c == String.class) || (c == Integer.class) || (c == Long.class) || (c == Double.class) || (c == Boolean.class)
				|| Date.class.isAssignableFrom(c);
	}
}
