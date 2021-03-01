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

import java.util.stream.IntStream;

import com.appslandia.common.utils.ArrayUtils;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.StringUtils;

/**
 * 
 * 
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class SqlLikeEscaper {

	final char escapeSignChar;
	final char[] charsToEscape;

	public SqlLikeEscaper(char escapeSignChar, char[] charsToEscape) {
		AssertUtils.assertNotNull(charsToEscape);

		this.escapeSignChar = escapeSignChar;
		this.charsToEscape = charsToEscape.clone();
	}

	public String escape(String value) {
		if (value == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder(value.length() + value.length() / 5);
		sb.append(value);

		int i = -1;
		while (true) {
			i++;
			if (i == sb.length()) {
				break;
			}
			for (char c : this.charsToEscape) {
				if (c == sb.charAt(i)) {
					sb.insert(i, this.escapeSignChar);
					i++;
					break;
				}
			}
		}
		return sb.toString();
	}

	public boolean hasEscapeSignChar(String value) {
		if (value == null) {
			return false;
		}
		return IntStream.range(0, value.length() - 1).anyMatch(index -> value.charAt(index) == getEscapeSignChar());
	}

	private static volatile SqlLikeEscaper __default;
	private static volatile char __escapeSignChar;
	private static volatile char[] __charsToEscape;

	private static final Object MUTEX = new Object();

	public static SqlLikeEscaper getDefault() {
		SqlLikeEscaper obj = __default;
		if (obj == null) {
			synchronized (MUTEX) {
				if ((obj = __default) == null) {
					__default = obj = new SqlLikeEscaper(getEscapeSignChar(), getCharsToEscape());
				}
			}
		}
		return obj;
	}

	public static void setDefault(SqlLikeEscaper impl) {
		if (__default == null) {
			synchronized (MUTEX) {
				if (__default == null) {
					__default = impl;
					return;
				}
			}
		}
		throw new IllegalStateException("SqlLikeEscaper.__default must be null.");
	}

	public static char getEscapeSignChar() {
		char chr = __escapeSignChar;
		if (chr == (char) 0) {
			synchronized (MUTEX) {
				if ((chr = __escapeSignChar) == (char) 0) {
					__escapeSignChar = chr = '\\';
				}
			}
		}
		return chr;
	}

	public static void setEscapeSignChar(char impl) {
		if (__escapeSignChar == (char) 0) {
			synchronized (MUTEX) {
				if (__escapeSignChar == (char) 0) {
					__escapeSignChar = impl;
					return;
				}
			}
		}
		throw new IllegalStateException("SqlLikeEscaper.__escapeSignChar must be null.");
	}

	public static char[] getCharsToEscape() {
		char[] obj = __charsToEscape;
		if (obj == null) {
			synchronized (MUTEX) {
				if ((obj = __charsToEscape) == null) {
					__charsToEscape = obj = new char[] { '%', '_' };
				}
			}
		}
		return obj.clone();
	}

	public static void setCharsToEscape(char[] impl) {
		if (__charsToEscape == null) {
			synchronized (MUTEX) {
				if (__charsToEscape == null) {
					__charsToEscape = ArrayUtils.copy(impl);
					return;
				}
			}
		}
		throw new IllegalStateException("SqlLikeEscaper.__charsToEscape must be null.");
	}

	public static String toLikePattern(String value, LikeType likeType) {
		AssertUtils.assertNotNull(likeType);

		if (StringUtils.isNullOrEmpty(value)) {
			return value;
		}
		if (likeType == LikeType.CONTAINS) {
			return "%" + getDefault().escape(value) + "%";
		}
		if (likeType == LikeType.STARTS_WITH) {
			return getDefault().escape(value) + "%";
		}
		return "%" + getDefault().escape(value);
	}
}
