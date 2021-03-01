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

import com.appslandia.common.base.FormatProvider;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public interface Formatter {
	public static final String BYTE = "Byte";

	public static final String SHORT = "Short";
	public static final String SHORT_L = "ShortL";

	public static final String INTEGER = "Integer";
	public static final String INTEGER_L = "IntegerL";

	public static final String LONG = "Long";
	public static final String LONG_L = "LongL";

	public static final String FLOAT = "Float";
	public static final String FLOAT_L = "FloatL";

	public static final String DOUBLE = "Double";
	public static final String DOUBLE_L = "DoubleL";

	public static final String BIGDECIMAL = "BigDecimal";
	public static final String BIGDECIMAL_L = "BigDecimalL";

	public static final String TIME = "Time";
	public static final String TIME_M = "TimeM";

	public static final String DATE = "Date";
	public static final String DATE_L = "DateL";

	public static final String TIMESTAMP = "Timestamp";
	public static final String TIMESTAMP_L = "TimestampL";

	public static final String TIMESTAMP_M = "TimestampM";
	public static final String TIMESTAMP_ML = "TimestampML";

	public static final String LOCAL_TIME = "LocalTime";
	public static final String LOCAL_TIME_M = "LocalTimeM";

	public static final String LOCAL_DATE = "LocalDate";
	public static final String LOCAL_DATE_L = "LocalDateL";

	public static final String LOCAL_DATETIME = "LocalDateTime";
	public static final String LOCAL_DATETIME_L = "LocalDateTimeL";

	public static final String LOCAL_DATETIME_M = "LocalDateTimeM";
	public static final String LOCAL_DATETIME_ML = "LocalDateTimeML";

	public static final String OFFSET_TIME = "OffsetTime";
	public static final String OFFSET_TIME_M = "OffsetTimeM";

	public static final String OFFSET_DATETIME = "OffsetDateTime";
	public static final String OFFSET_DATETIME_L = "OffsetDateTimeL";

	public static final String OFFSET_DATETIME_M = "OffsetDateTimeM";
	public static final String OFFSET_DATETIME_ML = "OffsetDateTimeML";

	public static final String YEAR_MONTH = "YearMonth";
	public static final String YEAR_MONTH_L = "YearMonthL";

	public static final String BOOLEAN = "Boolean";
	public static final String STRING = "String";

	public static final String STRING_EUC = "StringEUC";
	public static final String STRING_ELC = "StringELC";

	public static final String TEXT = "Text";
	public static final String TEXT_ML = "TextML";

	public static final String TAG = "Tag";
	public static final String TAGS = "Tags";
	public static final String DB_TAGS = "DbTags";

	public static final String KEYWORDS = "Keywords";

	default String getErrorMsgKey() {
		return getClass().getName() + ".message";
	}

	Class<?> getArgType();

	String format(Object obj, FormatProvider formatProvider);

	Object parse(String str, FormatProvider formatProvider) throws FormatterException;

	default FormatterException toParsingError(String str) {
		return toParsingError(str, getArgType().getName());
	}

	default FormatterException toParsingError(String str, String target) {
		return new FormatterException("An error occurred while parsing '" + str + "' to " + target, getErrorMsgKey());
	}

	default FormatterException toNumberOverflowError(String str) {
		return new FormatterException("An overflow occurred while parsing '" + str + "' to " + getArgType().getName(), getErrorMsgKey());
	}
}
