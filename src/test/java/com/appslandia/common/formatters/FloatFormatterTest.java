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

import org.junit.Assert;
import org.junit.Test;

import com.appslandia.common.base.FormatProvider;
import com.appslandia.common.base.FormatProviderImpl;
import com.appslandia.common.base.Language;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class FloatFormatterTest {

	@Test
	public void test_argType() {
		FloatFormatter formatter = new FloatFormatter(true);
		Assert.assertEquals(formatter.getArgType(), Float.class);
	}

	@Test
	public void test() {
		FloatFormatter formatter = new FloatFormatter(true);
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN);
		try {
			Float v = formatter.parse("12.345", formatProvider);
			Assert.assertEquals(v.floatValue(), 12.345, 0.001);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_group() {
		FloatFormatter formatter = new FloatFormatter(true);
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN);
		try {
			Float v = formatter.parse("12,345.67", formatProvider);
			Assert.assertEquals(v.floatValue(), 12345.67, 0.001);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_null() {
		FloatFormatter formatter = new FloatFormatter(true);
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN);
		try {
			Float val = formatter.parse(null, formatProvider);
			Assert.assertNull(val);

			val = formatter.parse("", formatProvider);
			Assert.assertNull(val);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_invalid() {
		FloatFormatter formatter = new FloatFormatter(true);
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN);
		try {
			formatter.parse("12-345", formatProvider);
			Assert.fail();
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof FormatterException);
		}
	}

	@Test
	public void test_invalid_format() {
		FloatFormatter formatter = new FloatFormatter(true);
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN);
		try {
			formatter.parse("12.345,678", formatProvider);
			Assert.fail();
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof FormatterException);
		}
	}
}
