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

package com.appslandia.common.json;

import java.io.StringReader;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Test;

import com.appslandia.common.utils.DateUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class GsonProcessorSqlToLocalTimeTest {

	@Test
	public void test_localToSql() {
		GsonProcessor jsonProcessor = new GsonProcessor();

		LocalTimeModel fromM = new LocalTimeModel();
		fromM.time = LocalTime.now();
		fromM.date = LocalDate.now();
		fromM.datetime = LocalDateTime.now();

		SqlTimeModel toM = null;

		try {
			String jsonString = jsonProcessor.toString(fromM);
			toM = jsonProcessor.read(new StringReader(jsonString), SqlTimeModel.class);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		Assert.assertEquals(DateUtils.iso8601LocalTime(fromM.time), DateUtils.iso8601Time(toM.time));
		Assert.assertEquals(DateUtils.iso8601LocalDate(fromM.date), DateUtils.iso8601Date(toM.date));
		Assert.assertEquals(DateUtils.iso8601LocalDateTime(fromM.datetime), DateUtils.iso8601DateTime(toM.datetime));
	}

	@Test
	public void test_sqlToLocal() {
		GsonProcessor jsonProcessor = new GsonProcessor();

		SqlTimeModel fromM = new SqlTimeModel();
		fromM.time = new Time(System.currentTimeMillis());
		fromM.date = new Date(System.currentTimeMillis());
		fromM.datetime = new Timestamp(System.currentTimeMillis());

		LocalTimeModel toM = null;

		try {
			String jsonString = jsonProcessor.toString(fromM);
			toM = jsonProcessor.read(new StringReader(jsonString), LocalTimeModel.class);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		Assert.assertEquals(DateUtils.iso8601LocalTime(toM.time), DateUtils.iso8601Time(fromM.time));
		Assert.assertEquals(DateUtils.iso8601LocalDate(toM.date), DateUtils.iso8601Date(fromM.date));
		Assert.assertEquals(DateUtils.iso8601LocalDateTime(toM.datetime), DateUtils.iso8601DateTime(fromM.datetime));
	}

	static class LocalTimeModel {
		LocalTime time;
		LocalDate date;
		LocalDateTime datetime;
	}

	static class SqlTimeModel {
		java.sql.Time time;
		java.sql.Date date;
		java.sql.Timestamp datetime;
	}
}
