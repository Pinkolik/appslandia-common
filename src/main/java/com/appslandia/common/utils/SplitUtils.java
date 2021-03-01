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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class SplitUtils {

	private static final Pattern NEWLINE_SEP_PATTERN = Pattern.compile("(\r?\n)+");

	public static String[] splitByLine(String str) {
		return split(str, NEWLINE_SEP_PATTERN);
	}

	public static String[] split(String str, Pattern separator) {
		if (str == null) {
			return StringUtils.EMPTY_ARRAY;
		}
		String[] items = separator.split(str);
		List<String> list = new ArrayList<>(items.length);

		for (String item : items) {
			item = item.trim();
			if (!item.isEmpty()) {
				list.add(item);
			}
		}
		return list.toArray(new String[list.size()]);
	}

	public static String[] split(String str, char separator) {
		if (str == null) {
			return StringUtils.EMPTY_ARRAY;
		}
		int startIdx = 0;
		int endIdx;
		List<String> list = new ArrayList<>();

		while ((endIdx = str.indexOf(separator, startIdx)) != -1) {
			String item = str.substring(startIdx, endIdx).trim();
			if (!item.isEmpty()) {
				list.add(item);
			}
			startIdx = endIdx + 1;
		}
		if (startIdx < str.length()) {
			String item = str.substring(startIdx).trim();
			if (!item.isEmpty()) {
				list.add(item);
			}
		}
		return list.toArray(new String[list.size()]);
	}
}
