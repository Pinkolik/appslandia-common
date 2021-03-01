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

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.FilterReader;
import java.io.FilterWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import com.appslandia.common.base.StringWriter;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class IOUtils {

	private static final int DEFAULT_BUFFER_SIZE = 4096;

	public static int copy(InputStream is, OutputStream os) throws IOException {
		int count = 0;
		byte[] buf = new byte[DEFAULT_BUFFER_SIZE];
		int c = -1;
		while ((c = is.read(buf, 0, buf.length)) != -1) {
			os.write(buf, 0, c);
			count += c;
		}
		return count;
	}

	public static void copy(byte[] src, int copyLength, OutputStream os) throws IOException {
		int count = 0;
		byte[] buf = new byte[DEFAULT_BUFFER_SIZE];
		while (count < copyLength) {
			int left = copyLength - count;
			int c = (left >= buf.length) ? buf.length : left;

			System.arraycopy(src, count, buf, 0, c);
			count += c;
			os.write(buf, 0, c);
		}
	}

	public static int copy(Reader r, Writer w) throws IOException {
		int count = 0;
		char[] buf = new char[DEFAULT_BUFFER_SIZE / 2];
		int c = -1;
		while ((c = r.read(buf, 0, buf.length)) != -1) {
			w.write(buf, 0, c);
			count += c;
		}
		return count;
	}

	public static byte[] toByteArray(InputStream is) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream(DEFAULT_BUFFER_SIZE);
		copy(is, os);
		return os.toByteArray();
	}

	public static String toString(Reader r) throws IOException {
		StringWriter os = new StringWriter();
		copy(r, os);
		return os.toString();
	}

	public static String toString(InputStream is, String charsetName) throws IOException {
		byte[] b = toByteArray(is);
		return new String(b, charsetName);
	}

	public static void closeQuietly(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException ignore) {
			}
		}
	}

	public static InputStream nonClosing(InputStream in) {
		return new FilterInputStream(in) {

			@Override
			public void close() throws IOException {
			}
		};
	}

	public static OutputStream nonClosing(OutputStream out) {
		return new FilterOutputStream(out) {

			@Override
			public void close() throws IOException {
			}
		};
	}

	public static Reader nonClosing(Reader r) {
		return new FilterReader(r) {

			@Override
			public void close() throws IOException {
			}
		};
	}

	public static Writer nonClosing(Writer w) {
		return new FilterWriter(w) {

			@Override
			public void close() throws IOException {
			}
		};
	}
}
