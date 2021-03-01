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

import com.appslandia.common.json.JsonIgnore;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class InitializeObject implements InitializeSupport {

	private volatile boolean initialized = false;

	@JsonIgnore
	protected final Object mutex = new Object();

	@Override
	public InitializeObject initialize() throws InitializeException {
		if (this.initialized) {
			return this;
		}
		synchronized (this.mutex) {
			if (this.initialized) {
				return this;
			}
			try {
				this.init();
				this.initialized = true;
			} catch (Exception ex) {
				throw new InitializeException(ex);
			}
		}
		return this;
	}

	protected abstract void init() throws Exception;

	@Override
	public void destroy() throws DestroyException {
	}

	protected void assertNotInitialized() {
		if (this.initialized) {
			throw new IllegalStateException("initialized.");
		}
	}
}
