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

import java.io.Serializable;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class TextBuilder implements Serializable, CharSequence {
	private static final long serialVersionUID = 1L;

	final StringBuilder sb;

	public TextBuilder() {
		this.sb = new StringBuilder();
	}

	public TextBuilder(int capacity) {
		this.sb = new StringBuilder(capacity);
	}

	public TextBuilder appendlnIfNotEmpty() {
		if (this.sb.length() > 0) {
			return appendln(1);
		}
		return this;
	}

	public TextBuilder appendln() {
		return appendln(1);
	}

	public TextBuilder appendln(int num) {
		for (int i = 0; i < num; i++) {
			this.sb.append(System.lineSeparator());
		}
		return this;
	}

	public TextBuilder appendtab() {
		return appendtab(1);
	}

	public TextBuilder appendtab(int num) {
		return append(num, '\t');
	}

	public TextBuilder appendsp() {
		return appendsp(1);
	}

	public TextBuilder appendsp(int num) {
		return append(num, ' ');
	}

	private TextBuilder append(int num, char chr) {
		for (int i = 0; i < num; i++) {
			this.sb.append(chr);
		}
		return this;
	}

	public TextBuilder append(Object val) {
		this.sb.append(val);
		return this;
	}

	public TextBuilder appendln(Object val) {
		this.sb.append(val);
		appendln(1);
		return this;
	}

	public int length() {
		return this.sb.length();
	}

	public void clear() {
		this.sb.setLength(0);
	}

	@Override
	public char charAt(int index) {
		return this.sb.charAt(index);
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return this.sb.subSequence(start, end);
	}

	@Override
	public String toString() {
		return this.sb.toString();
	}
}
