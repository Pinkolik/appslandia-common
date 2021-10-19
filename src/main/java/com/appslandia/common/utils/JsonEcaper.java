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

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;

import com.appslandia.common.base.StringWriter;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JsonEcaper {

	private static final int HIGHEST_SPECIAL = '\\';

	private static char[][] ESCAPE_JSON = new char[HIGHEST_SPECIAL + 1][];
	static {
		StringBuilder sb = new StringBuilder(6);

		// the control characters (U+0000 through U+001F).
		for (char ch = 0; ch <= 31; ch++) {
			toUnicodeEscaped(ch, sb);

			ESCAPE_JSON[ch] = sb.toString().toCharArray();
		}

		ESCAPE_JSON['"'] = "\\\"".toCharArray();
		ESCAPE_JSON['\\'] = "\\\\".toCharArray();
		ESCAPE_JSON['/'] = "\\/".toCharArray();
		ESCAPE_JSON['\b'] = "\\b".toCharArray();

		ESCAPE_JSON['\f'] = "\\f".toCharArray();
		ESCAPE_JSON['\n'] = "\\n".toCharArray();
		ESCAPE_JSON['\r'] = "\\r".toCharArray();
		ESCAPE_JSON['\t'] = "\\t".toCharArray();
	}

	static void toUnicodeEscaped(char c, StringBuilder out) {
		out.setLength(0);

		String hs = Integer.toHexString(c);
		out.append("\\u");

		for (int i = 0; i < 4 - hs.length(); i++) {
			out.append('0');
		}
		out.append(hs);
	}

	public static String escapeJson(String s) {
		if (s == null) {
			return null;
		}
		try (StringWriter out = new StringWriter((int) (s.length() * 1.25f))) {
			escapeJson(out, s);
			return out.toString();

		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	static void escapeJson(Writer out, String s) throws IOException {
		int start = 0;
		char[] srcChars = s.toCharArray();
		int length = s.length();

		for (int i = 0; i < length; i++) {
			char c = srcChars[i];

			if (c <= HIGHEST_SPECIAL) {
				char[] escaped = ESCAPE_JSON[c];
				if (escaped != null) {
					// add un_escaped portion
					if (start < i) {
						out.write(srcChars, start, i - start);
					}
					// add escaped
					out.write(escaped);
					start = i + 1;
				}
			}
		}
		// add rest of un_escaped portion
		if (start < length) {
			out.write(srcChars, start, length - start);
		}
	}
}
