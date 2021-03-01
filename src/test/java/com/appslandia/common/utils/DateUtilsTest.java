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

package com.appslandia.common.utils;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class DateUtilsTest {

	@Test
	public void test_todaySqlDate() {
		java.sql.Date today = DateUtils.todaySqlDate();
		Assert.assertEquals(DateUtils.iso8601DateTime(today), DateUtils.iso8601DateTime(DateUtils.clearTime(today)));
	}

	@Test
	public void test_todayAsLong() {
		long today = DateUtils.todayAsLong();
		Assert.assertEquals(DateUtils.iso8601DateTime(new Date(today)), DateUtils.iso8601DateTime(new Date(DateUtils.clearTime(today))));
	}

	@Test
	public void test_getCalendar() {
		Calendar cal = DateUtils.getCalendar(3, 9, 30);

		Assert.assertEquals(cal.get(Calendar.DAY_OF_WEEK), 3);
		Assert.assertEquals(cal.get(Calendar.HOUR_OF_DAY), 9);
		Assert.assertEquals(cal.get(Calendar.MINUTE), 30);

		Assert.assertEquals(cal.get(Calendar.SECOND), 0);
		Assert.assertEquals(cal.get(Calendar.MILLISECOND), 0);
	}

	@Test
	public void test_copyTime() {
		Date d1 = DateUtils.iso8601DateTime("2017-11-02T09:45:00.000");
		Date d2 = DateUtils.iso8601DateTime("2017-11-02T09:45:00.999");
		d2 = DateUtils.copyTime(d2, d1);
		Assert.assertEquals(DateUtils.iso8601Time(d2), "09:45:00.000");
	}

	@Test
	public void test_clearTime_dt() {
		Date time = DateUtils.iso8601DateTime("2017-11-02T09:45:00.999");
		Date noTime = DateUtils.clearTime(time);
		Assert.assertEquals(DateUtils.iso8601DateTime(noTime), "2017-11-02T00:00:00.000");
	}

	@Test
	public void test_clearTime_long() {
		long time = DateUtils.iso8601DateTime("2017-11-02T09:45:00.999").getTime();
		long noTime = DateUtils.clearTime(time);
		Assert.assertEquals(DateUtils.iso8601DateTime(new Date(noTime)), "2017-11-02T00:00:00.000");
	}

	@Test
	public void test_clearMs_dt() {
		Date time = DateUtils.iso8601DateTime("2017-11-02T09:45:00.999");
		Date noMs = DateUtils.clearMs(time);
		Assert.assertEquals(DateUtils.iso8601DateTime(noMs), "2017-11-02T09:45:00.000");
	}

	@Test
	public void test_clearMs_long() {
		long time = DateUtils.iso8601DateTime("2017-11-02T09:45:00.999").getTime();
		long noMs = DateUtils.clearMs(time);
		Assert.assertEquals(DateUtils.iso8601DateTime(new Date(noMs)), "2017-11-02T09:45:00.000");
	}

	@Test
	public void test_translateToMs() {
		long ms = DateUtils.translateToMs("1d 4h 8m");
		Assert.assertEquals(ms, TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS) + TimeUnit.MILLISECONDS.convert(4, TimeUnit.HOURS)
				+ TimeUnit.MILLISECONDS.convert(8, TimeUnit.MINUTES));

		ms = DateUtils.translateToMs("1D 4H 8M");
		Assert.assertEquals(ms, TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS) + TimeUnit.MILLISECONDS.convert(4, TimeUnit.HOURS)
				+ TimeUnit.MILLISECONDS.convert(8, TimeUnit.MINUTES));
	}

	@Test
	public void test_translateToMs_failed() {
		try {
			DateUtils.translateToMs("1d 4hr 8min");
			Assert.fail();
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}
	}

	@Test
	public void test_parse_date() {
		try {
			Date t = DateUtils.iso8601Date("2017-11-02");
			String s = DateUtils.iso8601Date(t);

			Assert.assertEquals(s, "2017-11-02");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_parse_time() {
		try {
			Time t = DateUtils.iso8601Time("09:45:00.999");
			String s = DateUtils.iso8601Time(t);

			Assert.assertEquals(s, "09:45:00.999");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_parse_dateTime() {
		try {
			Timestamp t = DateUtils.iso8601DateTime("2017-11-02T09:45:00.999");
			String s = DateUtils.iso8601DateTime(t);

			Assert.assertEquals(s, "2017-11-02T09:45:00.999");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	// Java8 Date/Time

	@Test
	public void test_parse_localDate() {
		try {
			LocalDate t = DateUtils.iso8601LocalDate("2017-11-02");
			String s = DateUtils.iso8601LocalDate(t);

			Assert.assertEquals(s, "2017-11-02");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_parse_localTime() {
		try {
			LocalTime t = DateUtils.iso8601LocalTime("09:45:00.999");
			String s = DateUtils.iso8601LocalTime(t);

			Assert.assertEquals(s, "09:45:00.999");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_parse_localDateTime() {
		try {
			LocalDateTime t = DateUtils.iso8601LocalDateTime("2017-11-02T09:45:00.999");
			String s = DateUtils.iso8601LocalDateTime(t);

			Assert.assertEquals(s, "2017-11-02T09:45:00.999");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_parse_offsetTime() {
		try {
			OffsetTime t = DateUtils.iso8601OffsetTime("09:45:00.999-05:00");
			String s = DateUtils.iso8601OffsetTime(t);

			Assert.assertEquals(s, "09:45:00.999-05:00");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_parse_offsetDateTime() {
		try {
			OffsetDateTime t = DateUtils.iso8601OffsetDateTime("2017-11-02T09:45:00.999-05:00");
			String s = DateUtils.iso8601OffsetDateTime(t);

			Assert.assertEquals(s, "2017-11-02T09:45:00.999-05:00");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}
}
