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

import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

import com.appslandia.common.base.FormatProviderImpl;
import com.appslandia.common.base.Language;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class LUCStringFormatterTest {

	@Test
	public void test_argType() {
		LUCStringFormatter formatter = new LUCStringFormatter(true, null);
		Assert.assertEquals(formatter.getArgType(), String.class);
	}

	@Test
	public void test_lower() {
		LUCStringFormatter formatter = new LUCStringFormatter(true, null);
		try {
			String value = "JavaEE-7";
			String str = formatter.parse(value, new FormatProviderImpl(Language.EN));

			Assert.assertEquals(str, "javaee-7");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_upper() {
		LUCStringFormatter formatter = new LUCStringFormatter(false, null);
		try {
			String value = "JavaEE-7";
			String str = formatter.parse(value, new FormatProviderImpl(Language.EN));

			Assert.assertEquals(str, "JAVAEE-7");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_en_lower() {
		LUCStringFormatter formatter = new LUCStringFormatter(true, Locale.ENGLISH);
		try {
			String value = "JavaEE-7";
			String str = formatter.parse(value, null);

			Assert.assertEquals(str, "javaee-7");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_en_upper() {
		LUCStringFormatter formatter = new LUCStringFormatter(false, Locale.ENGLISH);
		try {
			String value = "JavaEE-7";
			String str = formatter.parse(value, null);

			Assert.assertEquals(str, "JAVAEE-7");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_invariant_lower() {
		LUCStringFormatter formatter = new LUCStringFormatter(true, Locale.ROOT);
		try {
			String value = "JavaEE-7";
			String str = formatter.parse(value, null);

			Assert.assertEquals(str, "javaee-7");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_invariant_upper() {
		LUCStringFormatter formatter = new LUCStringFormatter(false, Locale.ROOT);
		try {
			String value = "JavaEE-7";
			String str = formatter.parse(value, null);

			Assert.assertEquals(str, "JAVAEE-7");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_null() {
		LUCStringFormatter formatter = new LUCStringFormatter(true, null);
		try {
			String value = null;
			String str = formatter.parse(value, new FormatProviderImpl(Language.EN));

			Assert.assertNull(str, null);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}
}
