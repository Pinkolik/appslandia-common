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

import java.lang.reflect.Array;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * 
 * 
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class URLUtils {

	public static String toQueryParams(Map<String, Object> parameterMap) {
		StringBuilder sb = new StringBuilder(parameterMap.size() * 16);
		for (Map.Entry<String, Object> param : parameterMap.entrySet()) {
			if (sb.length() > 0) {
				sb.append('&');
			}
			addQueryParam(sb, param.getKey(), param.getValue());
		}
		return sb.toString();
	}

	public static void addQueryParam(StringBuilder sb, String name, Object value) {
		if (value == null) {
			sb.append(URLEncoding.encodeParam(name)).append('=');
			return;
		}

		// Simple types
		if (!value.getClass().isArray()) {
			sb.append(URLEncoding.encodeParam(name)).append('=').append(URLEncoding.encodeParam(value.toString()));
			return;
		}

		// Array
		int len = Array.getLength(value);
		for (int i = 0; i < len; i++) {
			if (i > 0) {
				sb.append('&');
			}
			sb.append(URLEncoding.encodeParam(name)).append('=');
			Object subVal = Array.get(value, i);
			if (subVal != null) {
				sb.append(URLEncoding.encodeParam(subVal.toString()));
			}
		}
	}

	public static String toUrl(String url, Map<String, Object> moreParameters) {
		try {
			URI oldUri = new URI(url);
			String newQuery = oldUri.getQuery();

			if (newQuery == null) {
				newQuery = toQueryParams(moreParameters);
			} else {
				newQuery += '&' + toQueryParams(moreParameters);
			}

			URI newUri = new URI(oldUri.getScheme(), oldUri.getAuthority(), oldUri.getPath(), newQuery, oldUri.getFragment());
			return newUri.toString();

		} catch (URISyntaxException ex) {
			throw new IllegalArgumentException(ex);
		}
	}

	public static Map<String, Object> parseParams(String queryString, Map<String, Object> params) throws IllegalArgumentException {
		return parseParams(queryString, params, true);
	}

	public static Map<String, Object> parseParams(String queryString, Map<String, Object> params, boolean parseArray) throws IllegalArgumentException {
		if (queryString == null) {
			return params;
		}
		int startIdx = 0;
		int endIdx;

		while ((endIdx = queryString.indexOf('&', startIdx)) != -1) {
			String pair = queryString.substring(startIdx, endIdx);

			if (!parsePair(pair, params, parseArray)) {
				throw new IllegalArgumentException(StringFormat.fmt("queryString is invalid: {}", queryString));
			}
			startIdx = endIdx + 1;
		}

		if (startIdx < queryString.length()) {
			String pair = queryString.substring(startIdx);

			if (!parsePair(pair, params, parseArray)) {
				throw new IllegalArgumentException(StringFormat.fmt("queryString is invalid: {}", queryString));
			}
		}
		return params;
	}

	private static boolean parsePair(String pair, Map<String, Object> params, boolean parseArray) {
		int idx = pair.indexOf('=');
		if (idx <= 0) {
			return false;
		}
		String name = URLEncoding.decodeParam(pair.substring(0, idx));
		String value = pair.substring(idx + 1);
		String decodedVal = !value.isEmpty() ? URLEncoding.decodeParam(value) : null;

		// String Value?
		if (!parseArray) {
			params.putIfAbsent(name, decodedVal);
			return true;
		}

		// String[] Value
		if (!params.containsKey(name)) {
			params.put(name, decodedVal);

		} else {
			Object addedValue = params.get(name);

			if ((addedValue == null) || (addedValue.getClass() == String.class)) {
				params.put(name, new String[] { (String) addedValue, decodedVal });
			} else {
				params.put(name, ArrayUtils.append((String[]) addedValue, new String[] { decodedVal }));
			}
		}
		return true;
	}
}
