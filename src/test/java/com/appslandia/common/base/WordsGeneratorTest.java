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
public class WordsGeneratorTest {

	@Test
	public void test() {
		for (int i = 1; i <= 1000; i++) {
			WordsGenerator impl = new WordsGenerator().setLength(i);
			String str = impl.generate();

			Assert.assertEquals(str.length(), i);
			Assert.assertTrue(impl.verify(str));
		}
	}

	@Test
	public void test_DIGITS() {
		for (int i = 1; i <= 1000; i++) {

			WordsGenerator impl = new WordsGenerator().setAlphabet(WordsGenerator.Alphabet.DIGITS).setLength(i);
			String str = impl.generate();

			Assert.assertEquals(str.length(), i);
			Assert.assertTrue(impl.verify(str));
		}
	}

	@Test
	public void test_DIGITS_AZ() {
		for (int i = 1; i <= 1000; i++) {

			WordsGenerator impl = new WordsGenerator().setAlphabet(WordsGenerator.Alphabet.DIGITS_AZ).setLength(i);
			String str = impl.generate();

			Assert.assertEquals(str.length(), i);
			Assert.assertTrue(impl.verify(str));
		}
	}

	@Test
	public void test_DIGITS_AZ_UPPER() {
		for (int i = 1; i <= 1000; i++) {

			WordsGenerator impl = new WordsGenerator().setAlphabet(WordsGenerator.Alphabet.DIGITS_AZ_UPPER).setLength(i);
			String str = impl.generate();

			Assert.assertEquals(str.length(), i);
			Assert.assertTrue(impl.verify(str));
		}
	}

	@Test
	public void test_DIGITS_AZ_LOWER() {
		for (int i = 1; i <= 1000; i++) {

			WordsGenerator impl = new WordsGenerator().setAlphabet(WordsGenerator.Alphabet.DIGITS_AZ_LOWER).setLength(i);
			String str = impl.generate();

			Assert.assertEquals(str.length(), i);
			Assert.assertTrue(impl.verify(str));
		}
	}
}
