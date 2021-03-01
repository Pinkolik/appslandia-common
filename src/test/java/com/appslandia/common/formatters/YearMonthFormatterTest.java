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

import java.time.YearMonth;

import org.junit.Assert;
import org.junit.Test;

import com.appslandia.common.base.FormatProvider;
import com.appslandia.common.base.FormatProviderImpl;
import com.appslandia.common.base.Language;
import com.appslandia.common.utils.DateUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class YearMonthFormatterTest {

	@Test
	public void test_argType() {
		YearMonthFormatter formatter = new YearMonthFormatter(true);
		Assert.assertEquals(formatter.getArgType(), YearMonth.class);
	}

	@Test
	public void test_pattern() {
		YearMonthFormatter formatter = new YearMonthFormatter(true);
		Assert.assertEquals(formatter.getLocalizedPattern(new FormatProviderImpl(Language.EN)), Language.EN.getPatternYearMonth());
		Assert.assertEquals(formatter.getIsoPattern(), DateUtils.PATTERN_YEAR_MONTH);
	}

	@Test
	public void test() {
		YearMonthFormatter formatter = new YearMonthFormatter(true);
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN);
		try {
			YearMonth v = formatter.parse("12/2020", formatProvider);
			Assert.assertNotNull(v);
			Assert.assertEquals(formatter.format(v, formatProvider), "12/2020");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_null() {
		YearMonthFormatter formatter = new YearMonthFormatter(true);
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN);
		try {
			YearMonth v = formatter.parse(null, formatProvider);
			Assert.assertNull(v);

			v = formatter.parse("", formatProvider);
			Assert.assertNull(v);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_invalid() {
		YearMonthFormatter formatter = new YearMonthFormatter(true);
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN);
		try {
			formatter.parse("12-2020", formatProvider);
			Assert.fail();

		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof FormatterException);
		}

		try {
			formatter.parse("13/2020", formatProvider);
			Assert.fail();

		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof FormatterException);
		}
	}
}
