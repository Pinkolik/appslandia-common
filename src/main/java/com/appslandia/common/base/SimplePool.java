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

import com.appslandia.common.utils.ObjectUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class SimplePool<T> {

	final int size;
	final T pool[];
	final Object mutex = new Object();

	private int current = -1;

	public SimplePool() {
		this(16);
	}

	public SimplePool(int size) {
		this.size = size;
		this.pool = ObjectUtils.cast(new Object[size]);
	}

	public void put(T service) {
		synchronized (this.mutex) {
			int idx = this.current;
			if (idx >= this.size - 1) {
				return;
			}
			this.pool[++idx] = service;
			this.current = idx;
		}
	}

	public T get() {
		synchronized (this.mutex) {
			int idx = this.current;
			if (idx < 0) {
				return null;
			}
			T t = this.pool[idx];
			this.current = idx - 1;
			return t;
		}
	}
}
