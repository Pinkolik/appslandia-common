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

import org.junit.Assert;
import org.junit.Test;

import com.appslandia.common.utils.DateUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class GsonProcessorSqlTimeTest {

	@Test
	public void test() {
		GsonProcessor jsonProcessor = new GsonProcessor();

		TestModel fromM = new TestModel();
		fromM.date = DateUtils.todaySqlDate();
		fromM.time = DateUtils.iso8601Time("09:34:00.000");
		fromM.datetime = DateUtils.nowTimestamp();

		TestModel toM = null;
		try {
			String jsonString = jsonProcessor.toString(fromM);
			toM = jsonProcessor.read(new StringReader(jsonString), TestModel.class);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		Assert.assertEquals(toM.date, fromM.date);
		Assert.assertEquals(toM.time, fromM.time);
		Assert.assertEquals(DateUtils.iso8601DateTime(toM.datetime), DateUtils.iso8601DateTime(fromM.datetime));
	}

	static class TestModel {
		java.sql.Date date;
		java.sql.Time time;
		java.sql.Timestamp datetime;
	}
}
