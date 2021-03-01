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

package com.appslandia.common.logging;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class AppLoggerManager {

	final Map<String, AppLogger> loggers = new HashMap<>();
	final Object mutex = new Object();

	public AppLogger getAppLogger(String name) {
		AppLogger logger = this.loggers.get(name);
		if (logger == null) {
			synchronized (this.mutex) {
				if ((logger = this.loggers.get(name)) == null) {

					logger = createAppLogger(name);
					this.loggers.put(name, logger);
				}
			}
		}
		return logger;
	}

	public AppLogger getAppLogger(Class<?> clazz) {
		return getAppLogger(clazz.getName());
	}

	protected abstract AppLogger createAppLogger(String name);

	public void close() {
	}
}
