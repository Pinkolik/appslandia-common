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

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ObjectUtils {

	public static final String NULL_STR = "null";

	public static String toStringOrEmpty(Object value) {
		return value != null ? value.toString() : StringUtils.EMPTY_STRING;
	}

	public static String toStringOrNull(Object value) {
		return (value != null) ? value.toString() : null;
	}

	@SuppressWarnings("unchecked")
	public static <F, T> T cast(F o) throws ClassCastException {
		return (T) o;
	}

	public static boolean equals_str(Object a, Object b) {
		return Objects.equals(toStringOrNull(a), toStringOrNull(b));
	}

	public static String toHashInfo(Object value) {
		if (value == null) {
			return NULL_STR;
		}
		return value.getClass().getName() + "@" + Integer.toHexString(value.hashCode());
	}

	public static String asString(Object[] array) {
		if (array == null) {
			return NULL_STR;
		}
		return Arrays.stream(array).map(e -> toStringOrNull(e)).collect(Collectors.joining(", "));
	}

	public static String asString(Object array) {
		if (array != null) {
			AssertUtils.assertTrue(array.getClass().isArray());
		}
		if (array == null) {
			return NULL_STR;
		}
		return StreamUtils.stream(ArrayUtils.iterator(array)).map(e -> toStringOrNull(e)).collect(Collectors.joining(", "));
	}

	public static String asString(Iterable<?> iterable) {
		if (iterable == null) {
			return NULL_STR;
		}
		return StreamUtils.stream(iterable.iterator()).map(e -> toStringOrNull(e)).collect(Collectors.joining(", "));
	}

	public static String asString(Iterator<?> iterator) {
		if (iterator == null) {
			return NULL_STR;
		}
		return StreamUtils.stream(iterator).map(e -> toStringOrNull(e)).collect(Collectors.joining(", "));
	}

	public static String asString(Enumeration<?> enumeration) {
		if (enumeration == null) {
			return NULL_STR;
		}
		return StreamUtils.stream(enumeration).map(e -> toStringOrNull(e)).collect(Collectors.joining(", "));
	}
}
