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

import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JulAppLoggerManager extends AppLoggerManager {

	@Override
	protected AppLogger createAppLogger(String name) {
		return new JulAppLogger(Logger.getLogger(name));
	}

	static class JulAppLogger implements AppLogger {

		final Logger logger;

		public JulAppLogger(Logger logger) {
			this.logger = logger;
		}

		@Override
		public boolean isLoggable(Level level) {
			return this.logger.isLoggable(toImplLevel(level));
		}

		@Override
		public void log(Level level, String message) {
			java.util.logging.Level l = toImplLevel(level);
			if (this.logger.isLoggable(l)) {
				this.logger.log(l, message);
			}
		}

		@Override
		public void log(Level level, String message, Throwable exception) {
			java.util.logging.Level l = toImplLevel(level);
			if (this.logger.isLoggable(l)) {
				this.logger.log(l, message, exception);
			}
		}

		@Override
		public void log(Level level, Supplier<String> msgSupplier) {
			java.util.logging.Level l = toImplLevel(level);
			if (this.logger.isLoggable(l)) {
				this.logger.log(l, msgSupplier);
			}
		}

		@Override
		public void log(Level level, Supplier<String> msgSupplier, Throwable exception) {
			java.util.logging.Level l = toImplLevel(level);
			if (this.logger.isLoggable(l)) {
				this.logger.log(l, exception, msgSupplier);
			}
		}

		static java.util.logging.Level toImplLevel(Level level) {
			switch (level) {
			case ALL:
				return java.util.logging.Level.ALL;
			case TRACE:
				return java.util.logging.Level.FINER;
			case DEBUG:
				return java.util.logging.Level.FINE;
			case INFO:
				return java.util.logging.Level.INFO;
			case WARN:
				return java.util.logging.Level.WARNING;
			case ERROR:
				return java.util.logging.Level.SEVERE;
			default:
				return java.util.logging.Level.OFF;
			}
		}
	}
}
