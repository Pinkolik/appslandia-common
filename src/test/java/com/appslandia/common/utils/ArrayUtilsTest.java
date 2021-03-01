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
public class ArrayUtilsTest {

	@Test
	public void test_appendArrays2() {
		byte[] arr1 = new byte[] { 1, 2 };
		byte[] arr2 = new byte[] { 3, 4, 5 };
		byte[] result = ArrayUtils.append(arr1, arr2);

		Assert.assertEquals(result.length, 5);
		Assert.assertEquals(result[0], 1);
		Assert.assertEquals(result[2], 3);
	}

	@Test
	public void test_appendArrays3() {
		byte[] arr1 = new byte[] { 1, 2 };
		byte[] arr2 = new byte[] { 3, 4, 5 };
		byte[] arr3 = new byte[] { 6, 7, 8, 9 };
		byte[] result = ArrayUtils.append(arr1, arr2, arr3);

		Assert.assertEquals(result.length, 9);
		Assert.assertEquals(result[0], 1);
		Assert.assertEquals(result[2], 3);
		Assert.assertEquals(result[5], 6);
	}

	@Test
	public void test_copyArrays2() {
		byte[] arr = new byte[] { 1, 2, 3, 4, 5 };
		byte[] arr1 = new byte[2];
		byte[] arr2 = new byte[3];
		ArrayUtils.copy(arr, arr1, arr2);

		Assert.assertEquals(arr1[0], 1);
		Assert.assertEquals(arr1[1], 2);

		Assert.assertEquals(arr2[0], 3);
		Assert.assertEquals(arr2[1], 4);
		Assert.assertEquals(arr2[2], 5);
	}

	@Test
	public void test_copyArrays3() {
		byte[] arr = new byte[] { 1, 2, 3, 4, 5, 6 };
		byte[] arr1 = new byte[1];
		byte[] arr2 = new byte[2];
		byte[] arr3 = new byte[3];
		ArrayUtils.copy(arr, arr1, arr2, arr3);

		Assert.assertEquals(arr1[0], 1);

		Assert.assertEquals(arr2[0], 2);
		Assert.assertEquals(arr2[1], 3);

		Assert.assertEquals(arr3[0], 4);
		Assert.assertEquals(arr3[1], 5);
		Assert.assertEquals(arr3[2], 6);
	}
}
