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

package com.appslandia.common.formatters;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.TypeUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class FormatterProvider extends InitializeObject {

	private Map<String, Formatter> formatterMap = new HashMap<>();

	@Override
	protected void init() throws Exception {
		addDefault(Formatter.BYTE, new ByteFormatter());

		addDefault(Formatter.SHORT, new ShortFormatter());
		addDefault(Formatter.SHORT_L, new ShortFormatter(true));

		addDefault(Formatter.INTEGER, new IntegerFormatter());
		addDefault(Formatter.INTEGER_L, new IntegerFormatter(true));

		addDefault(Formatter.LONG, new LongFormatter());
		addDefault(Formatter.LONG_L, new LongFormatter(true));

		addDefault(Formatter.FLOAT, new FloatFormatter());
		addDefault(Formatter.FLOAT_L, new FloatFormatter(true));

		addDefault(Formatter.DOUBLE, new DoubleFormatter());
		addDefault(Formatter.DOUBLE_L, new DoubleFormatter(true));

		addDefault(Formatter.BIGDECIMAL, new BigDecimalFormatter());
		addDefault(Formatter.BIGDECIMAL_L, new BigDecimalFormatter(true));

		addDefault(Formatter.TIME, new SqlTimeFormatter());
		addDefault(Formatter.TIME_M, new SqlTimeMFormatter());

		addDefault(Formatter.DATE, new SqlDateFormatter());
		addDefault(Formatter.DATE_L, new SqlDateFormatter(true));

		addDefault(Formatter.TIMESTAMP, new SqlTimestampFormatter());
		addDefault(Formatter.TIMESTAMP_L, new SqlTimestampFormatter(true));

		addDefault(Formatter.TIMESTAMP_M, new SqlTimestampMFormatter());
		addDefault(Formatter.TIMESTAMP_ML, new SqlTimestampMFormatter(true));

		addDefault(Formatter.BOOLEAN, new BooleanFormatter());
		addDefault(Formatter.STRING, new StringFormatter());

		addDefault(Formatter.STRING_EUC, new LUCStringFormatter(false, Locale.ENGLISH));
		addDefault(Formatter.STRING_ELC, new LUCStringFormatter(true, Locale.ENGLISH));

		addDefault(Formatter.TEXT, new TextFormatter());
		addDefault(Formatter.TEXT_ML, new TextMLFormatter());

		addDefault(Formatter.TAG, new TagFormatter());
		addDefault(Formatter.TAGS, new TagsFormatter());
		addDefault(Formatter.DB_TAGS, new DbTagsFormatter());

		addDefault(Formatter.KEYWORDS, new KeywordsFormatter());

		// Java8 Date/Time
		addDefault(Formatter.LOCAL_TIME, new LocalTimeFormatter());
		addDefault(Formatter.LOCAL_TIME_M, new LocalTimeMFormatter());

		addDefault(Formatter.LOCAL_DATE, new LocalDateFormatter());
		addDefault(Formatter.LOCAL_DATE_L, new LocalDateFormatter(true));

		addDefault(Formatter.LOCAL_DATETIME, new LocalDateTimeFormatter());
		addDefault(Formatter.LOCAL_DATETIME_L, new LocalDateTimeFormatter(true));

		addDefault(Formatter.LOCAL_DATETIME_M, new LocalDateTimeMFormatter());
		addDefault(Formatter.LOCAL_DATETIME_ML, new LocalDateTimeMFormatter(true));

		addDefault(Formatter.OFFSET_TIME, new OffsetTimeFormatter());
		addDefault(Formatter.OFFSET_TIME_M, new OffsetTimeMFormatter());

		addDefault(Formatter.OFFSET_DATETIME, new OffsetDateTimeFormatter());
		addDefault(Formatter.OFFSET_DATETIME_L, new OffsetDateTimeFormatter(true));

		addDefault(Formatter.OFFSET_DATETIME_M, new OffsetDateTimeMFormatter());
		addDefault(Formatter.OFFSET_DATETIME_ML, new OffsetDateTimeMFormatter(true));

		addDefault(Formatter.YEAR_MONTH, new YearMonthFormatter());
		addDefault(Formatter.YEAR_MONTH_L, new YearMonthFormatter(true));

		this.formatterMap = Collections.unmodifiableMap(this.formatterMap);
	}

	protected void addDefault(String formatterId, Formatter formatter) {
		if (!this.formatterMap.containsKey(formatterId)) {
			this.formatterMap.put(formatterId, formatter);
		}
	}

	public void addFormatter(String formatterId, Formatter formatter) {
		this.assertNotInitialized();
		this.formatterMap.put(formatterId, formatter);
	}

	public Formatter findFormatter(String formatterId, Class<?> targetType) {
		this.initialize();
		if (formatterId != null) {
			return getFormatter(formatterId);
		} else {
			return this.formatterMap.get(TypeUtils.wrap(targetType).getSimpleName());
		}
	}

	public Formatter getFormatter(Class<?> targetType) throws IllegalArgumentException {
		this.initialize();
		Formatter formatter = this.formatterMap.get(TypeUtils.wrap(targetType).getSimpleName());
		if (formatter == null) {
			throw new IllegalArgumentException("Formatter is not found (targetType=" + targetType + ")");
		}
		return formatter;
	}

	public Formatter getFormatter(String formatterId) throws IllegalArgumentException {
		this.initialize();
		Formatter formatter = this.formatterMap.get(formatterId);
		if (formatter == null) {
			throw new IllegalArgumentException("Formatter is not found (formatterId=" + formatterId + ")");
		}
		return formatter;
	}

	private static volatile FormatterProvider __default;
	private static final Object MUTEX = new Object();

	public static FormatterProvider getDefault() {
		FormatterProvider obj = __default;
		if (obj == null) {
			synchronized (MUTEX) {
				if ((obj = __default) == null) {
					__default = obj = initFormatterProvider();
				}
			}
		}
		return obj;
	}

	public static void setDefault(FormatterProvider impl) {
		if (__default == null) {
			synchronized (MUTEX) {
				if (__default == null) {
					__default = impl;
					return;
				}
			}
		}
		throw new IllegalStateException("FormatterProvider.__default must be null.");
	}

	private static Supplier<FormatterProvider> __provider;

	public static void setProvider(Supplier<FormatterProvider> impl) {
		if (__default == null) {
			synchronized (MUTEX) {
				if (__default == null) {
					__provider = impl;
					return;
				}
			}
		}
		throw new IllegalStateException("FormatterProvider.__default must be null.");
	}

	private static FormatterProvider initFormatterProvider() {
		if (__provider != null) {
			return __provider.get();
		}
		return new FormatterProvider();
	}
}
