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

import com.appslandia.common.base.ExceptionBlock;
import com.appslandia.common.utils.ExceptionUtils;
import com.appslandia.common.utils.StringFormat;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public interface AppLogger {

	boolean isLoggable(Level level);

	void log(Level level, String message);

	void log(Level level, String message, Throwable exception);

	void log(Level level, Supplier<String> msgSupplier);

	void log(Level level, Supplier<String> msgSupplier, Throwable exception);

	default void trace(String message) {
		log(Level.TRACE, message);
	}

	default void debug(String message) {
		log(Level.DEBUG, message);
	}

	default void info(String message) {
		log(Level.INFO, message);
	}

	default void warn(String message) {
		log(Level.WARN, message);
	}

	default void error(String message) {
		log(Level.ERROR, message);
	}

	default void error(String message, Throwable exception) {
		log(Level.ERROR, message, exception);
	}

	default void error(Throwable exception) {
		log(Level.ERROR, ExceptionUtils.buildMessage(exception), exception);
	}

	default void log(Level level, String format, Object... entries) {
		log(level, StringFormat.fmt(format, entries));
	}

	default void trace(String format, Object... entries) {
		log(Level.TRACE, format, entries);
	}

	default void debug(String format, Object... entries) {
		log(Level.DEBUG, format, entries);
	}

	default void info(String format, Object... entries) {
		log(Level.INFO, format, entries);
	}

	default void warn(String format, Object... entries) {
		log(Level.WARN, format, entries);
	}

	default void error(String format, Object... entries) {
		log(Level.ERROR, format, entries);
	}

	default void error(ExceptionBlock block) {
		try {
			block.run();
		} catch (Exception ex) {
			error(ex);
		}
	}

	public enum Level {
		ALL(Integer.MIN_VALUE), // ALL
		TRACE(400), // FINER
		DEBUG(500), // FINEST/FINE/CONFIG
		INFO(800), // INFO
		WARN(900), // WARNING
		ERROR(1000), // SEVERE
		OFF(Integer.MAX_VALUE); // OFF

		final int severity;

		private Level(int severity) {
			this.severity = severity;
		}

		public String getName() {
			return name();
		}

		public int getSeverity() {
			return this.severity;
		}
	}
}
