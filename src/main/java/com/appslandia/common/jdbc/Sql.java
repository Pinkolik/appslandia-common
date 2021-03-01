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

package com.appslandia.common.jdbc;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.base.Out;
import com.appslandia.common.utils.AssertUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class Sql extends InitializeObject implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int DEFAULT_ARRAY_MAX_LENGTH = 32;

	private String sql;
	private Map<String, Integer> arrayLens;

	private String translatedSql;
	private Map<String, int[]> indexesMap;

	public Sql() {
	}

	public Sql(String sql) {
		this.sql = sql;
	}

	public Sql arrayLen(String parameterName, int maxLength) {
		assertNotInitialized();
		AssertUtils.assertTrue(maxLength > 0, "maxLength is required.");

		if (this.arrayLens == null) {
			this.arrayLens = new HashMap<>();
		}
		this.arrayLens.put(parameterName, maxLength);
		return this;
	}

	public Sql sql(String sql) {
		assertNotInitialized();
		this.sql = sql;
		return this;
	}

	@Override
	protected void init() throws Exception {
		AssertUtils.assertNotNull(this.sql, "sql is required.");
		translateSql();
	}

	private void translateSql() {
		StringBuilder sb = new StringBuilder(this.sql);
		Map<String, int[]> indexesMap = new LinkedHashMap<>();

		int start = 0;
		int index = 0;

		while (true) {

			int paramIdx = start;
			while (paramIdx < sb.length() - 1 && sb.charAt(paramIdx) != getParamPrefix()) {
				paramIdx++;
			}
			if (paramIdx >= sb.length() - 1)
				break;

			// Parse parameter
			Out<Integer> paramEnd = new Out<Integer>();
			Out<String> paramName = new Out<String>();

			boolean isParamContext = isParamContext(sb, paramIdx, paramEnd, paramName);
			if (!isParamContext) {
				start = paramIdx + 1;
				continue;
			}

			// IN or LIKE_ANY?
			Out<Integer> fieldIdx = new Out<Integer>();
			Out<String> fieldName = new Out<String>();

			boolean isInContext = isContext(sb, paramIdx, "IN", fieldIdx, fieldName);
			boolean isLikeAnyContext = isContext(sb, paramIdx, "LIKE_ANY", fieldIdx, fieldName);

			boolean isArrayParam = isInContext || isLikeAnyContext;
			Integer arrayLen = (this.arrayLens == null) ? null : this.arrayLens.get(paramName.value);

			if (arrayLen != null) {
				if (!isArrayParam) {
					throw new IllegalArgumentException("Array parameter is not found (name=" + paramName + ")");
				}
			} else {
				if (isArrayParam) {
					arrayLen = DEFAULT_ARRAY_MAX_LENGTH;
					if (this.arrayLens == null) {
						this.arrayLens = new HashMap<>();
					}
					this.arrayLens.put(paramName.value, arrayLen);
				}
			}

			// Normal parameter?
			if (!isArrayParam) {
				sb.replace(paramIdx, paramEnd.value + 1, "?");
				putIndex(indexesMap, paramName.value, ++index);

				start = paramIdx + 1;
				continue;
			}

			// IN
			if (isInContext) {
				sb.replace(paramIdx, paramEnd.value + 1, "()");

				for (int subIdx = 0; subIdx < arrayLen; subIdx++) {
					if (subIdx == 0) {
						sb.insert(paramIdx + 1, '?');
					} else {
						sb.insert(paramIdx + 1, "?, ");
					}
					putIndex(indexesMap, toParamName(paramName.value, subIdx), ++index);
				}

				start = paramIdx + 1;
				continue;
			}

			// LIKE_ANY
			sb.delete(fieldIdx.value, paramEnd.value + 1);

			for (int subIdx = 0; subIdx < arrayLen; subIdx++) {
				if (subIdx == 0) {
					sb.insert(fieldIdx.value, String.format("%s LIKE ?", fieldName.value));
				} else {
					sb.insert(fieldIdx.value, String.format("%s LIKE ? OR ", fieldName.value));
				}
				putIndex(indexesMap, toParamName(paramName.value, subIdx), ++index);
			}
			start = fieldIdx.value + 1;
		}

		this.translatedSql = sb.toString();
		this.indexesMap = Collections.unmodifiableMap(indexesMap);
	}

	private void putIndex(Map<String, int[]> indexesMap, String paramName, int index) {
		int[] indexes = indexesMap.get(paramName);
		if (indexes == null) {
			indexes = new int[] { index };
		} else {
			indexes = Arrays.copyOf(indexes, indexes.length + 1);
			indexes[indexes.length - 1] = index;
		}
		indexesMap.put(paramName, indexes);
	}

	public String getSql() {
		initialize();
		return this.sql;
	}

	public String getTranslatedSql() {
		initialize();
		return this.translatedSql;
	}

	public int[] getIndexes(String parameterName) {
		initialize();
		int[] indexes = this.indexesMap.get(parameterName);
		if (indexes == null) {
			throw new IllegalArgumentException("Parameter is not found (name=" + parameterName + ")");
		}
		return indexes;
	}

	public int getArrayLen(String parameterName) {
		initialize();
		Integer len = (this.arrayLens != null) ? this.arrayLens.get(parameterName) : null;
		if (len == null) {
			throw new IllegalArgumentException("Array parameter is not found (name=" + parameterName + ")");
		}
		return len;
	}

	private static boolean isContext(StringBuilder sb, int paramIdx, String context, Out<Integer> fieldIdx, Out<String> fieldName) {
		int i = paramIdx - 1;
		while (i >= 0 && Character.isWhitespace(sb.charAt(i))) {
			i--;
		}
		if (i < 0)
			return false;

		int j = i;
		while (j >= 0 && !Character.isWhitespace(sb.charAt(j))) {
			j--;
		}
		if (!sb.substring(j + 1, i + 1).equalsIgnoreCase(context)) {
			return false;
		}

		if (j < 0)
			return false;
		int k = j;
		while (k >= 0 && Character.isWhitespace(sb.charAt(k))) {
			k--;
		}
		if (k < 0)
			return false;

		int h = k;
		while (h >= 0 && !Character.isWhitespace(sb.charAt(h)) && sb.charAt(h) != '(') {
			h--;
		}
		fieldName.value = sb.substring(h + 1, k + 1);
		if (fieldName.value.isEmpty())
			return false;

		fieldIdx.value = h + 1;
		return true;
	}

	private static boolean isParamContext(StringBuilder sb, int paramIdx, Out<Integer> paramEnd, Out<String> paramName) {
		int k = paramIdx + 1;
		while (k < sb.length() && isParamChar(sb.charAt(k))) {
			k++;
		}
		if (k == paramIdx + 1)
			return false;
		paramName.value = sb.substring(paramIdx + 1, k);
		paramEnd.value = k - 1;
		return true;
	}

	public static String toParamName(String parameterName, int subIdx) {
		return parameterName + "__" + subIdx;
	}

	private static boolean isParamChar(char chr) {
		return ('a' <= chr && chr <= 'z') || ('A' <= chr && chr <= 'Z') || ('0' <= chr && chr <= '9') || (chr == '_');
	}

	private static volatile char __paramPrefix;
	private static final Object MUTEX = new Object();

	public static char getParamPrefix() {
		char chr = __paramPrefix;
		if (chr == (char) 0) {
			synchronized (MUTEX) {
				if ((chr = __paramPrefix) == (char) 0) {
					__paramPrefix = chr = ':';
				}
			}
		}
		return chr;
	}

	public static void setParamPrefix(char impl) {
		if (__paramPrefix == (char) 0) {
			synchronized (MUTEX) {
				if (__paramPrefix == (char) 0) {
					__paramPrefix = impl;
					return;
				}
			}
		}
		throw new IllegalStateException("Sql.__paramPrefix must be null.");
	}
}
