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

package com.appslandia.common.base;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.appslandia.common.utils.AssertUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class GroupFormat {
	private static final Pattern GROUP_SIZE_PATTERN = Pattern.compile("\\{\\s*\\d+\\s*}");

	final String format;

	private Group[] groups;
	private int inputLength;
	private int outputLength;

	public GroupFormat(String format) {
		this.format = AssertUtils.assertNotNull(format, "format is required.");
		this.parseFormat(format);
	}

	private void parseFormat(String format) {
		Matcher matcher = GROUP_SIZE_PATTERN.matcher(format);
		List<Group> list = new ArrayList<>();

		int inputLength = 0;
		int outputLength = 0;
		int lastEnd = 0;

		while (matcher.find()) {
			int start = matcher.start();
			if (start > 0) {
				String text = format.substring(lastEnd, start);
				if (!text.isEmpty()) {
					list.add(new Group(text, 0));
					outputLength += text.length();
				}
			}
			// {d+}
			String sizeGroup = matcher.group();
			int size = Integer.parseInt(sizeGroup.substring(1, sizeGroup.length() - 1).trim());
			if (size == 0) {
				throw new IllegalArgumentException("format is invalid (format=" + format + ")");
			}

			inputLength += size;
			outputLength += size;

			list.add(new Group(null, size));
			lastEnd = matcher.end();
		}
		if (list.isEmpty()) {
			throw new IllegalArgumentException("No group found (format=" + format + ")");
		}
		this.groups = list.toArray(new Group[list.size()]);
		this.inputLength = inputLength;
		this.outputLength = outputLength;
	}

	public int getInputLength() {
		return this.inputLength;
	}

	public String format(String str) {
		if (str == null) {
			return null;
		}
		if (str.length() != this.inputLength) {
			return str;
		}
		StringBuilder sb = new StringBuilder(this.outputLength);
		int pos = 0;
		for (Group group : this.groups) {
			if (group.length > 0) {
				sb.append(str.substring(pos, group.length + pos));
				pos += group.length;
			} else {
				sb.append(group.text);
			}
		}
		return sb.toString();
	}

	private static class Group {

		final String text;
		final int length;

		public Group(String text, int length) {
			this.text = text;
			this.length = length;
		}
	}
}
