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
public class BitArrayTest {

	@Test
	public void test() {

		BitArray bits = new BitArray();
		bits.b1(1, 3, 5);
		bits.b0(0, 2, 4);

		Assert.assertTrue(bits.length() == 6);
		Assert.assertTrue(bits.cardinality() == 3);

		Assert.assertTrue(bits.get(1));
		Assert.assertTrue(bits.get(3));
		Assert.assertTrue(bits.get(5));

		Assert.assertFalse(bits.get(0));
		Assert.assertFalse(bits.get(2));
		Assert.assertFalse(bits.get(4));
	}

	@Test
	public void test_tgl() {

		BitArray bits = new BitArray();
		bits.b1(1, 3, 5);

		bits.tgl(1, 3, 5);

		Assert.assertFalse(bits.get(1));
		Assert.assertFalse(bits.get(3));
		Assert.assertFalse(bits.get(5));
	}

	@Test
	public void test_ctor() {

		BitArray bits = new BitArray();
		bits.b1(1, 3, 5);

		BitArray copy = new BitArray(bits);
		Assert.assertEquals(bits, copy);
	}
}
