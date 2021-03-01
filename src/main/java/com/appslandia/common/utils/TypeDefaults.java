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

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class TypeDefaults {

	private static final Map<Class<?>, Object> PRIMITIVE_DEFAULT_MAP;

	static {
		Map<Class<?>, Object> defaults = new HashMap<>();

		put(defaults, boolean.class, false);
		put(defaults, char.class, '\0');
		put(defaults, byte.class, (byte) 0);
		put(defaults, short.class, (short) 0);
		put(defaults, int.class, 0);
		put(defaults, long.class, 0L);
		put(defaults, float.class, 0f);
		put(defaults, double.class, 0d);

		PRIMITIVE_DEFAULT_MAP = Collections.unmodifiableMap(defaults);
	}

	private static <T> void put(Map<Class<?>, Object> map, Class<T> type, T value) {
		map.put(type, value);
	}

	public static <T> T defaultValue(Class<T> type) {
		return ObjectUtils.cast(PRIMITIVE_DEFAULT_MAP.get(type));
	}
}
