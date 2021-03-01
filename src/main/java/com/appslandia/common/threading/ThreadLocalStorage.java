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

package com.appslandia.common.threading;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ThreadLocalStorage<T> {

	final ThreadLocal<T> holder;

	public ThreadLocalStorage() {
		this(false);
	}

	public ThreadLocalStorage(ThreadLocal<T> holder) {
		this.holder = holder;
	}

	public ThreadLocalStorage(boolean inheritable) {
		if (!inheritable) {
			this.holder = new ThreadLocal<>();
		} else {
			this.holder = new InheritableThreadLocal<>();
		}
	}

	public T get() {
		return this.holder.get();
	}

	public T val() throws IllegalStateException {
		T t = this.holder.get();
		if (t == null) {
			throw new IllegalStateException("No current value found in the current thread.");
		}
		return t;
	}

	public void set(T value) {
		if (value == null) {
			this.holder.remove();
		} else {
			this.holder.set(value);
		}
	}

	public T remove() {
		T t = this.holder.get();
		this.holder.remove();
		return t;
	}

	public boolean hasValue() {
		return this.holder.get() != null;
	}
}
