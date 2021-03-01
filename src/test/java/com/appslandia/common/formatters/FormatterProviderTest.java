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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class FormatterProviderTest {

	@Test
	public void test() {
		FormatterProvider provider = new FormatterProvider();
		try {
			Formatter formatter = provider.getFormatter(String.class);
			Assert.assertTrue(formatter.getClass() == StringFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Byte.class);
			Assert.assertTrue(formatter.getClass() == ByteFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Short.class);
			Assert.assertTrue(formatter.getClass() == ShortFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Integer.class);
			Assert.assertTrue(formatter.getClass() == IntegerFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Long.class);
			Assert.assertTrue(formatter.getClass() == LongFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Float.class);
			Assert.assertTrue(formatter.getClass() == FloatFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Double.class);
			Assert.assertTrue(formatter.getClass() == DoubleFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(BigDecimal.class);
			Assert.assertTrue(formatter.getClass() == BigDecimalFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Boolean.class);
			Assert.assertTrue(formatter.getClass() == BooleanFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(java.sql.Date.class);
			Assert.assertTrue(formatter.getClass() == SqlDateFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(java.sql.Time.class);
			Assert.assertTrue(formatter.getClass() == SqlTimeFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(java.sql.Timestamp.class);
			Assert.assertTrue(formatter.getClass() == SqlTimestampFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_formatterIds() {
		FormatterProvider provider = new FormatterProvider();
		try {
			Formatter formatter = provider.getFormatter(Formatter.STRING);
			Assert.assertTrue(formatter.getClass() == StringFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.STRING_EUC);
			Assert.assertTrue(formatter.getClass() == LUCStringFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.STRING_ELC);
			Assert.assertTrue(formatter.getClass() == LUCStringFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.BYTE);
			Assert.assertTrue(formatter.getClass() == ByteFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.SHORT);
			Assert.assertTrue(formatter.getClass() == ShortFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.INTEGER);
			Assert.assertTrue(formatter.getClass() == IntegerFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.LONG);
			Assert.assertTrue(formatter.getClass() == LongFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.FLOAT);
			Assert.assertTrue(formatter.getClass() == FloatFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.DOUBLE);
			Assert.assertTrue(formatter.getClass() == DoubleFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.BIGDECIMAL);
			Assert.assertTrue(formatter.getClass() == BigDecimalFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.SHORT_L);
			Assert.assertTrue(formatter.getClass() == ShortFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.INTEGER_L);
			Assert.assertTrue(formatter.getClass() == IntegerFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.LONG_L);
			Assert.assertTrue(formatter.getClass() == LongFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.FLOAT_L);
			Assert.assertTrue(formatter.getClass() == FloatFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.DOUBLE_L);
			Assert.assertTrue(formatter.getClass() == DoubleFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.BIGDECIMAL_L);
			Assert.assertTrue(formatter.getClass() == BigDecimalFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.BOOLEAN);
			Assert.assertTrue(formatter.getClass() == BooleanFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.DATE);
			Assert.assertTrue(formatter.getClass() == SqlDateFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.TIME);
			Assert.assertTrue(formatter.getClass() == SqlTimeFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.TIMESTAMP);
			Assert.assertTrue(formatter.getClass() == SqlTimestampFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.TIME_M);
			Assert.assertTrue(formatter.getClass() == SqlTimeMFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.TIMESTAMP_M);
			Assert.assertTrue(formatter.getClass() == SqlTimestampMFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.DATE_L);
			Assert.assertTrue(formatter.getClass() == SqlDateFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.TIMESTAMP_L);
			Assert.assertTrue(formatter.getClass() == SqlTimestampFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.TIMESTAMP_ML);
			Assert.assertTrue(formatter.getClass() == SqlTimestampMFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_java8DateTime() {
		FormatterProvider provider = new FormatterProvider();

		try {
			Formatter formatter = provider.getFormatter(LocalDate.class);
			Assert.assertTrue(formatter.getClass() == LocalDateFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(LocalTime.class);
			Assert.assertTrue(formatter.getClass() == LocalTimeFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(LocalDateTime.class);
			Assert.assertTrue(formatter.getClass() == LocalDateTimeFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(OffsetTime.class);
			Assert.assertTrue(formatter.getClass() == OffsetTimeFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(OffsetDateTime.class);
			Assert.assertTrue(formatter.getClass() == OffsetDateTimeFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_java8DateTime_formatterIds() {
		FormatterProvider provider = new FormatterProvider();

		try {
			Formatter formatter = provider.getFormatter(Formatter.LOCAL_DATE);
			Assert.assertTrue(formatter.getClass() == LocalDateFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.LOCAL_TIME);
			Assert.assertTrue(formatter.getClass() == LocalTimeFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.LOCAL_DATETIME);
			Assert.assertTrue(formatter.getClass() == LocalDateTimeFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.LOCAL_TIME_M);
			Assert.assertTrue(formatter.getClass() == LocalTimeMFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.LOCAL_DATETIME_M);
			Assert.assertTrue(formatter.getClass() == LocalDateTimeMFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.OFFSET_TIME);
			Assert.assertTrue(formatter.getClass() == OffsetTimeFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.OFFSET_DATETIME);
			Assert.assertTrue(formatter.getClass() == OffsetDateTimeFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.OFFSET_TIME_M);
			Assert.assertTrue(formatter.getClass() == OffsetTimeMFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.OFFSET_DATETIME_M);
			Assert.assertTrue(formatter.getClass() == OffsetDateTimeMFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.LOCAL_DATE_L);
			Assert.assertTrue(formatter.getClass() == LocalDateFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.LOCAL_DATETIME_L);
			Assert.assertTrue(formatter.getClass() == LocalDateTimeFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.LOCAL_DATETIME_ML);
			Assert.assertTrue(formatter.getClass() == LocalDateTimeMFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.OFFSET_DATETIME_L);
			Assert.assertTrue(formatter.getClass() == OffsetDateTimeFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.OFFSET_DATETIME_ML);
			Assert.assertTrue(formatter.getClass() == OffsetDateTimeMFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}
}
