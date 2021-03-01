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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class Log4JAppLoggerManager extends AppLoggerManager {

	final boolean shutdownOnClose;

	public Log4JAppLoggerManager() {
		this(false);
	}

	public Log4JAppLoggerManager(boolean shutdownOnClose) {
		this.shutdownOnClose = shutdownOnClose;
	}

	@Override
	public void close() {
		if (this.shutdownOnClose) {
			LogManager.shutdown();
		}
	}

	@Override
	protected AppLogger createAppLogger(String name) {
		return new Log4JAppLogger(LogManager.getLogger(name));
	}

	static class Log4JAppLogger implements AppLogger {

		final Logger logger;

		public Log4JAppLogger(Logger logger) {
			this.logger = logger;
		}

		@Override
		public boolean isLoggable(Level level) {
			return isLoggable(toImplLevel(level));
		}

		@Override
		public void log(Level level, String message) {
			org.apache.logging.log4j.Level l = toImplLevel(level);
			if (isLoggable(l)) {
				this.logger.log(l, message);
			}
		}

		@Override
		public void log(Level level, String message, Throwable exception) {
			org.apache.logging.log4j.Level l = toImplLevel(level);
			if (isLoggable(l)) {
				this.logger.log(l, message, exception);
			}
		}

		@Override
		public void log(Level level, Supplier<String> msgSupplier) {
			org.apache.logging.log4j.Level l = toImplLevel(level);
			if (isLoggable(l)) {
				this.logger.log(l, msgSupplier);
			}
		}

		@Override
		public void log(Level level, Supplier<String> msgSupplier, Throwable exception) {
			org.apache.logging.log4j.Level l = toImplLevel(level);
			if (isLoggable(l)) {
				this.logger.log(l, msgSupplier, exception);
			}
		}

		private boolean isLoggable(org.apache.logging.log4j.Level level) {
			if (level == org.apache.logging.log4j.Level.ALL) {
				return true;
			}
			if (level == org.apache.logging.log4j.Level.TRACE) {
				return this.logger.isTraceEnabled();
			}
			if (level == org.apache.logging.log4j.Level.DEBUG) {
				return this.logger.isDebugEnabled();
			}
			if (level == org.apache.logging.log4j.Level.INFO) {
				return this.logger.isInfoEnabled();
			}
			if (level == org.apache.logging.log4j.Level.WARN) {
				return this.logger.isWarnEnabled();
			}
			if (level == org.apache.logging.log4j.Level.ERROR) {
				return this.logger.isErrorEnabled();
			}
			// OFF
			return false;
		}

		static org.apache.logging.log4j.Level toImplLevel(Level level) {
			switch (level) {
			case ALL:
				return org.apache.logging.log4j.Level.ALL;
			case TRACE:
				return org.apache.logging.log4j.Level.TRACE;
			case DEBUG:
				return org.apache.logging.log4j.Level.DEBUG;
			case INFO:
				return org.apache.logging.log4j.Level.INFO;
			case WARN:
				return org.apache.logging.log4j.Level.WARN;
			case ERROR:
				return org.apache.logging.log4j.Level.ERROR;
			default:
				return org.apache.logging.log4j.Level.OFF;
			}
		}
	}
}
