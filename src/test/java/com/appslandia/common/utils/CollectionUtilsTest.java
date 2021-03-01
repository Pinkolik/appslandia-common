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

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@SuppressWarnings("unchecked")
public class CollectionUtilsTest {

	private static Object[] buildToMapArguments(int entries) {
		Object[] args = new Object[entries * 2];
		for (int i = 0; i < entries; i++) {
			int j = i * 2;
			args[j] = "key" + i; // key = value + i
			args[j + 1] = i; // value
		}
		return args;
	}

	@Test
	public void test_toMap() {

		// Max 9 entries
		for (int entries = 1; entries <= 9; entries++) {
			Class<?>[] paramTypes = new Class<?>[entries * 2];
			Arrays.fill(paramTypes, Object.class);

			Map<String, Integer> map = null;
			try {
				Method m = CollectionUtils.class.getMethod("toMap", paramTypes);
				Object[] args = buildToMapArguments(entries);
				map = (Map<String, Integer>) m.invoke(null, args);

			} catch (Exception ex) {
				Assert.fail(ex.getMessage());
			}
			Assert.assertNotNull(map);
			try {
				for (Entry<String, Integer> e : map.entrySet()) {
					Assert.assertEquals(e.getKey(), "key" + e.getValue());
				}
			} catch (Exception ex) {
				Assert.fail(ex.getMessage());
			}
		}
	}

	@Test
	public void test_unmodifiableMap() {
		// Max 9 entries
		for (int entries = 1; entries <= 9; entries++) {
			Class<?>[] paramTypes = new Class<?>[entries * 2];
			Arrays.fill(paramTypes, Object.class);

			Map<String, Integer> map = null;
			try {
				Method m = CollectionUtils.class.getMethod("unmodifiableMap", paramTypes);
				Object[] args = buildToMapArguments(entries);
				map = (Map<String, Integer>) m.invoke(null, args);

			} catch (Exception ex) {
				Assert.fail(ex.getMessage());
			}
			Assert.assertNotNull(map);
			try {
				for (Entry<String, Integer> e : map.entrySet()) {
					Assert.assertEquals(e.getKey(), "key" + e.getValue());
				}
			} catch (Exception ex) {
				Assert.fail(ex.getMessage());
			}
		}
	}
}
