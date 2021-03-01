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

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

/**
 * @see java.io.CharArrayWriter
 * 
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class CharArrayWriter extends Writer {

	protected char buf[];
	protected int count;

	public CharArrayWriter() {
		this(32);
	}

	public CharArrayWriter(int initialSize) {
		if (initialSize < 0) {
			throw new IllegalArgumentException("initialSize");
		}
		buf = new char[initialSize];
	}

	public void write(int c) {
		int newcount = count + 1;
		if (newcount > buf.length) {
			buf = Arrays.copyOf(buf, Math.max(buf.length << 1, newcount));
		}
		buf[count] = (char) c;
		count = newcount;
	}

	public void write(char c[], int off, int len) {
		if ((off < 0) || (off > c.length) || (len < 0) || ((off + len) > c.length) || ((off + len) < 0)) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return;
		}
		int newcount = count + len;
		if (newcount > buf.length) {
			buf = Arrays.copyOf(buf, Math.max(buf.length << 1, newcount));
		}
		System.arraycopy(c, off, buf, count, len);
		count = newcount;
	}

	public void write(String str, int off, int len) {
		int newcount = count + len;
		if (newcount > buf.length) {
			buf = Arrays.copyOf(buf, Math.max(buf.length << 1, newcount));
		}
		str.getChars(off, off + len, buf, count);
		count = newcount;
	}

	public void writeTo(Writer out) throws IOException {
		out.write(buf, 0, count);
	}

	public CharArrayWriter append(CharSequence csq) {
		String s = (csq == null ? "null" : csq.toString());
		write(s, 0, s.length());
		return this;
	}

	public CharArrayWriter append(CharSequence csq, int start, int end) {
		String s = (csq == null ? "null" : csq).subSequence(start, end).toString();
		write(s, 0, s.length());
		return this;
	}

	public CharArrayWriter append(char c) {
		write(c);
		return this;
	}

	public void reset() {
		count = 0;
	}

	public char toCharArray()[] {
		return Arrays.copyOf(buf, count);
	}

	public int size() {
		return count;
	}

	public String toString() {
		return new String(buf, 0, count);
	}

	public void flush() {
	}

	public void close() {
	}
}
