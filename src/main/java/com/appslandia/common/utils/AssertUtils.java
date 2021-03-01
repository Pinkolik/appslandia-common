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
import java.util.function.Supplier;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class AssertUtils {

	public static void assertTrue(boolean expression) {
		if (!expression) {
			throw new IllegalArgumentException();
		}
	}

	public static void assertTrue(boolean expression, String errorMessage) {
		if (!expression) {
			throw new IllegalArgumentException(errorMessage);
		}
	}

	public static void assertTrue(boolean expression, Supplier<String> errorMessage) {
		if (!expression) {
			throw new IllegalArgumentException(errorMessage.get());
		}
	}

	public static void assertFalse(boolean expression) {
		if (expression) {
			throw new IllegalArgumentException();
		}
	}

	public static void assertFalse(boolean expression, String errorMessage) {
		if (expression) {
			throw new IllegalArgumentException(errorMessage);
		}
	}

	public static void assertFalse(boolean expression, Supplier<String> errorMessage) {
		if (expression) {
			throw new IllegalArgumentException(errorMessage.get());
		}
	}

	public static <T> T assertNotNull(T obj) {
		if (obj == null) {
			throw new NullPointerException();
		}
		return obj;
	}

	public static <T> T assertNotNull(T obj, String errorMessage) {
		if (obj == null) {
			throw new NullPointerException(errorMessage);
		}
		return obj;
	}

	public static <T> T assertNotNull(T obj, Supplier<String> errorMessage) {
		if (obj == null) {
			throw new NullPointerException(errorMessage.get());
		}
		return obj;
	}

	public static void assertNull(Object obj) {
		if (obj != null) {
			throw new IllegalArgumentException();
		}
	}

	public static void assertNull(Object obj, String errorMessage) {
		if (obj != null) {
			throw new IllegalArgumentException(errorMessage);
		}
	}

	public static void assertNull(Object obj, Supplier<String> errorMessage) {
		if (obj != null) {
			throw new IllegalArgumentException(errorMessage.get());
		}
	}

	public static void assertState(boolean expression) {
		if (!expression) {
			throw new IllegalStateException();
		}
	}

	public static void assertState(boolean expression, String errorMessage) {
		if (!expression) {
			throw new IllegalStateException(errorMessage);
		}
	}

	public static void assertState(boolean expression, Supplier<String> errorMessage) {
		if (!expression) {
			throw new IllegalStateException(errorMessage.get());
		}
	}

	public static <T> T assertStateNotNull(T obj) {
		if (obj == null) {
			throw new IllegalStateException();
		}
		return obj;
	}

	public static <T> T assertStateNotNull(T obj, String errorMessage) {
		if (obj == null) {
			throw new IllegalStateException(errorMessage);
		}
		return obj;
	}

	public static <T> T assertStateNotNull(T obj, Supplier<String> errorMessage) {
		if (obj == null) {
			throw new IllegalStateException(errorMessage.get());
		}
		return obj;
	}

	public static void assertStateNull(Object obj) {
		if (obj != null) {
			throw new IllegalStateException();
		}
	}

	public static void assertStateNull(Object obj, String errorMessage) {
		if (obj != null) {
			throw new IllegalStateException(errorMessage);
		}
	}

	public static void assertStateNull(Object obj, Supplier<String> errorMessage) {
		if (obj != null) {
			throw new IllegalStateException(errorMessage.get());
		}
	}

	public static String assertNotBlank(String text) {
		text = StringUtils.trimToNull(text);
		if (text == null) {
			throw new IllegalArgumentException();
		}
		return text;
	}

	public static String assertNotBlank(String text, String errorMessage) {
		text = StringUtils.trimToNull(text);
		if (text == null) {
			throw new IllegalArgumentException(errorMessage);
		}
		return text;
	}

	public static String assertNotBlank(String text, Supplier<String> errorMessage) {
		text = StringUtils.trimToNull(text);
		if (text == null) {
			throw new IllegalArgumentException(errorMessage.get());
		}
		return text;
	}

	public static <T> T[] assertHasElements(T[] elements) {
		if ((elements == null) || (elements.length == 0)) {
			throw new IllegalArgumentException();
		}
		return elements;
	}

	public static <T> T[] assertHasElements(T[] elements, String errorMessage) {
		if ((elements == null) || (elements.length == 0)) {
			throw new IllegalArgumentException(errorMessage);
		}
		return elements;
	}

	public static <T> T[] assertHasElements(T[] elements, Supplier<String> errorMessage) {
		if ((elements == null) || (elements.length == 0)) {
			throw new IllegalArgumentException(errorMessage.get());
		}
		return elements;
	}

	public static <T> Collection<T> assertHasElements(Collection<T> elements) {
		if ((elements == null) || (elements.size() == 0)) {
			throw new IllegalArgumentException();
		}
		return elements;
	}

	public static <T> Collection<T> assertHasElements(Collection<T> elements, String errorMessage) {
		if ((elements == null) || (elements.size() == 0)) {
			throw new IllegalArgumentException(errorMessage);
		}
		return elements;
	}

	public static <T> Collection<T> assertHasElements(Collection<T> elements, Supplier<String> errorMessage) {
		if ((elements == null) || (elements.size() == 0)) {
			throw new IllegalArgumentException(errorMessage.get());
		}
		return elements;
	}

	public static int assertPossitive(int value) {
		if (value <= 0) {
			throw new IllegalArgumentException();
		}
		return value;
	}

	public static int assertPossitive(int value, String errorMessage) {
		if (value <= 0) {
			throw new IllegalArgumentException(errorMessage);
		}
		return value;
	}

	public static int assertPossitive(int value, Supplier<String> errorMessage) {
		if (value <= 0) {
			throw new IllegalArgumentException(errorMessage.get());
		}
		return value;
	}

	public static int assertNonNegative(int value) {
		if (value < 0) {
			throw new IllegalArgumentException();
		}
		return value;
	}

	public static int assertNonNegative(int value, String errorMessage) {
		if (value < 0) {
			throw new IllegalArgumentException(errorMessage);
		}
		return value;
	}

	public static int assertNonNegative(int value, Supplier<String> errorMessage) {
		if (value < 0) {
			throw new IllegalArgumentException(errorMessage.get());
		}
		return value;
	}

	public static long assertPossitive(long value) {
		if (value <= 0) {
			throw new IllegalArgumentException();
		}
		return value;
	}

	public static long assertPossitive(long value, String errorMessage) {
		if (value <= 0) {
			throw new IllegalArgumentException(errorMessage);
		}
		return value;
	}

	public static long assertPossitive(long value, Supplier<String> errorMessage) {
		if (value <= 0) {
			throw new IllegalArgumentException(errorMessage.get());
		}
		return value;
	}

	public static long assertNonNegative(long value) {
		if (value < 0) {
			throw new IllegalArgumentException();
		}
		return value;
	}

	public static long assertNonNegative(long value, String errorMessage) {
		if (value < 0) {
			throw new IllegalArgumentException(errorMessage);
		}
		return value;
	}

	public static long assertNonNegative(long value, Supplier<String> errorMessage) {
		if (value < 0) {
			throw new IllegalArgumentException(errorMessage.get());
		}
		return value;
	}

	public static double assertPossitive(double value, String errorMessage) {
		if (value <= 0.0) {
			throw new IllegalArgumentException(errorMessage);
		}
		return value;
	}

	public static double assertPossitive(double value, Supplier<String> errorMessage) {
		if (value <= 0.0) {
			throw new IllegalArgumentException(errorMessage.get());
		}
		return value;
	}

	public static double assertNonNegative(double value) {
		if (value < 0.0) {
			throw new IllegalArgumentException();
		}
		return value;
	}

	public static double assertNonNegative(double value, String errorMessage) {
		if (value < 0.0) {
			throw new IllegalArgumentException(errorMessage);
		}
		return value;
	}

	public static double assertNonNegative(double value, Supplier<String> errorMessage) {
		if (value < 0.0) {
			throw new IllegalArgumentException(errorMessage.get());
		}
		return value;
	}
}
