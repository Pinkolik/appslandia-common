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

/**
 * @see java.io.StringWriter
 * 
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class StringWriter extends Writer {

	private StringBuilder buf;

	public StringWriter() {
		this.buf = new StringBuilder();
	}

	public StringWriter(int capacity) {
		this.buf = new StringBuilder(capacity);
	}

	public void write(int c) {
		this.buf.append((char) c);
	}

	public void write(char cbuf[], int off, int len) {
		if ((off < 0) || (off > cbuf.length) || (len < 0) || ((off + len) > cbuf.length) || ((off + len) < 0)) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return;
		}
		this.buf.append(cbuf, off, len);
	}

	public void write(String str) {
		this.buf.append(str);
	}

	public void write(String str, int off, int len) {
		this.buf.append(str.substring(off, off + len));
	}

	public StringWriter append(CharSequence csq) {
		if (csq == null)
			write("null");
		else
			write(csq.toString());
		return this;
	}

	public StringWriter append(CharSequence csq, int start, int end) {
		CharSequence cs = (csq == null ? "null" : csq);
		write(cs.subSequence(start, end).toString());
		return this;
	}

	public StringWriter append(char c) {
		write(c);
		return this;
	}

	public String toString() {
		return this.buf.toString();
	}

	public void reset() {
		this.buf.setLength(0);
	}

	public void flush() {
	}

	public void close() throws IOException {
	}
}
