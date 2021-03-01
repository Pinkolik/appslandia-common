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
public class BytesSizeUtilsTest {

	@Test
	public void test_translateToBytes() {
		long size = BytesSizeUtils.translateToBytes("1GB 2MB 3KB 4B", false);
		Assert.assertEquals(size, 1073741824 + 2 * 1_048_576 + 3 * 1024 + 4);

		size = BytesSizeUtils.translateToBytes("1.5GB 4B", false);
		Assert.assertEquals(size, (long) (1.5 * 1073741824) + 4);

		size = BytesSizeUtils.translateToBytes("1gb 2mb", false);
		Assert.assertEquals(size, 1073741824 + 2 * 1_048_576);
	}

	@Test
	public void test_translateToBytes_invalid() {
		try {
			BytesSizeUtils.translateToBytes("1GB 2M", false);
			Assert.fail();
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			BytesSizeUtils.translateToBytes("1GB+2M", false);
			Assert.fail();
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			BytesSizeUtils.translateToBytes("1 GB 2M", false);
			Assert.fail();
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}
	}
}
