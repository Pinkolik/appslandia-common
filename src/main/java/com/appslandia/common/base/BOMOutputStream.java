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
import java.io.OutputStream;

/**
 * 
 * 
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class BOMOutputStream extends OutputStream {

	final OutputStream os;
	final BOM bom;

	private boolean wroteBom;

	public BOMOutputStream(OutputStream os, String encoding) {
		this(os, BOM.parse(encoding));
	}

	public BOMOutputStream(OutputStream os, BOM bom) {
		this.os = os;
		this.bom = bom;
	}

	public BOM getBOM() {
		return this.bom;
	}

	protected synchronized void writeBOM() throws IOException {
		if (this.bom == null) {
			return;
		}
		if (!this.wroteBom) {
			this.os.write(this.bom.getBytes());
			this.wroteBom = true;
		}
	}

	@Override
	public void write(int b) throws IOException {
		this.writeBOM();
		this.os.write(b);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		this.writeBOM();
		this.os.write(b, off, len);
	}

	@Override
	public void write(byte[] b) throws IOException {
		this.writeBOM();
		this.os.write(b, 0, b.length);
	}

	@Override
	public void flush() throws IOException {
		this.os.flush();
	}

	@Override
	public void close() throws IOException {
		this.os.close();
	}
}
