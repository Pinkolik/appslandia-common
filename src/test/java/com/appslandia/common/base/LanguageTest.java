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

package com.appslandia.common.base;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class LanguageTest {

	@Test
	public void test_language() {
		Language lang = Language.getDefault();
		Assert.assertEquals(lang, Language.EN);
	}

	@Test
	public void test_enLanguage() {
		Language lang = Language.EN;
		Assert.assertEquals(lang.getId(), "en");
		Assert.assertEquals(lang.getPatternDate(), "MM/dd/yyyy");

		Assert.assertEquals(lang.getPatternDateTimeM(), "MM/dd/yyyy HH:mm");
		Assert.assertEquals(lang.getPatternDateTimeMZ(), "MM/dd/yyyy HH:mmXXX");

		Assert.assertEquals(lang.getPatternDateTime(), "MM/dd/yyyy HH:mm:ss.SSS");
		Assert.assertEquals(lang.getPatternDateTimeZ(), "MM/dd/yyyy HH:mm:ss.SSSXXX");

		Assert.assertEquals(lang.getPatternTimeM(), "HH:mm");
		Assert.assertEquals(lang.getPatternTimeMZ(), "HH:mmXXX");

		Assert.assertEquals(lang.getPatternTime(), "HH:mm:ss.SSS");
		Assert.assertEquals(lang.getPatternTimeZ(), "HH:mm:ss.SSSXXX");
	}

	@Test
	public void test_viLanguage() {
		Language lang = Language.VI;
		Assert.assertEquals(lang.getId(), "vi");
		Assert.assertEquals(lang.getPatternDate(), "dd/MM/yyyy");

		Assert.assertEquals(lang.getPatternDateTimeM(), "dd/MM/yyyy HH:mm");
		Assert.assertEquals(lang.getPatternDateTimeMZ(), "dd/MM/yyyy HH:mmXXX");

		Assert.assertEquals(lang.getPatternDateTime(), "dd/MM/yyyy HH:mm:ss.SSS");
		Assert.assertEquals(lang.getPatternDateTimeZ(), "dd/MM/yyyy HH:mm:ss.SSSXXX");

		Assert.assertEquals(lang.getPatternTimeM(), "HH:mm");
		Assert.assertEquals(lang.getPatternTimeMZ(), "HH:mmXXX");

		Assert.assertEquals(lang.getPatternTime(), "HH:mm:ss.SSS");
		Assert.assertEquals(lang.getPatternTimeZ(), "HH:mm:ss.SSSXXX");
	}
}
