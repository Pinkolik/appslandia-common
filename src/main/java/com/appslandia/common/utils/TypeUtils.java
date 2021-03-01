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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class TypeUtils {

	private static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPER_MAP;
	private static final Map<Class<?>, Class<?>> WRAPPER_PRIMITIVE_MAP;

	static {
		Map<Class<?>, Class<?>> primToWrap = new HashMap<>();
		Map<Class<?>, Class<?>> wrapToPrim = new HashMap<>();

		add(primToWrap, wrapToPrim, boolean.class, Boolean.class);
		add(primToWrap, wrapToPrim, byte.class, Byte.class);
		add(primToWrap, wrapToPrim, char.class, Character.class);
		add(primToWrap, wrapToPrim, double.class, Double.class);
		add(primToWrap, wrapToPrim, float.class, Float.class);
		add(primToWrap, wrapToPrim, int.class, Integer.class);
		add(primToWrap, wrapToPrim, long.class, Long.class);
		add(primToWrap, wrapToPrim, short.class, Short.class);
		add(primToWrap, wrapToPrim, void.class, Void.class);

		PRIMITIVE_WRAPPER_MAP = Collections.unmodifiableMap(primToWrap);
		WRAPPER_PRIMITIVE_MAP = Collections.unmodifiableMap(wrapToPrim);
	}

	private static void add(Map<Class<?>, Class<?>> forward, Map<Class<?>, Class<?>> backward, Class<?> key, Class<?> value) {
		forward.put(key, value);
		backward.put(value, key);
	}

	public static Set<Class<?>> allPrimitiveTypes() {
		return PRIMITIVE_WRAPPER_MAP.keySet();
	}

	public static Set<Class<?>> allWrapperTypes() {
		return WRAPPER_PRIMITIVE_MAP.keySet();
	}

	public static boolean isWrapperType(Class<?> type) {
		return WRAPPER_PRIMITIVE_MAP.containsKey(type);
	}

	public static Class<?> wrap(Class<?> type) {
		Class<?> wrapped = PRIMITIVE_WRAPPER_MAP.get(type);
		return (wrapped == null) ? type : wrapped;
	}

	public static Class<?> unwrap(Class<?> type) {
		Class<?> unwrapped = WRAPPER_PRIMITIVE_MAP.get(type);
		return (unwrapped == null) ? type : unwrapped;
	}

	public static boolean isPrimitiveOrWrapper(Class<?> type) {
		if (type.isPrimitive()) {
			return true;
		}
		return WRAPPER_PRIMITIVE_MAP.containsKey(wrap(type));
	}
}
