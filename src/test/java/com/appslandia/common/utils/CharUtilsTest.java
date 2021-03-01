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

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class CharUtilsTest {

	@Test
	public void test_toCharRanges() {
		char[] charRanges = CharUtils.toCharRanges("a-zA-Z");
		Assert.assertNotNull(charRanges);
		Assert.assertTrue(charRanges.length == 52);
	}

	@Test
	public void test_toCharRanges_digits() {
		char[] charRanges = CharUtils.toCharRanges("0-9");
		Assert.assertNotNull(charRanges);
		Assert.assertTrue(charRanges.length == 10);

		charRanges = CharUtils.toCharRanges("1-37-9");
		Assert.assertNotNull(charRanges);
		Assert.assertEquals(new String(charRanges), "123789");
	}

	@Test
	public void test_toCharRanges_notRange() {
		char[] charRanges = CharUtils.toCharRanges("0123");
		Assert.assertNotNull(charRanges);
		Assert.assertEquals(new String(charRanges), "0123");
	}

	@Test
	public void test_toCharRanges_mixed() {
		char[] charRanges = CharUtils.toCharRanges("1-3a-cABC");
		Assert.assertNotNull(charRanges);
		Assert.assertEquals(new String(charRanges), "123abcABC");
	}

	@Test
	public void test_toCharRanges_minus() {
		char[] charRanges = CharUtils.toCharRanges("1-3a-c-");
		Assert.assertNotNull(charRanges);
		Assert.assertEquals(new String(charRanges), "123abc-");
	}
}
