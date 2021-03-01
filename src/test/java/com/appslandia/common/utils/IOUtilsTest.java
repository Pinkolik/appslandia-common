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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class IOUtilsTest {

	@Test
	public void test_copyFromIsToOs() {
		byte[] src = RandomUtils.nextBytes(100);
		ByteArrayInputStream is = new ByteArrayInputStream(src);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			IOUtils.copy(is, os);
			Assert.assertArrayEquals(os.toByteArray(), src);
		} catch (IOException ex) {
		}
	}

	@Test
	public void test_toByteArray() {
		byte[] src = RandomUtils.nextBytes(100);
		ByteArrayInputStream is = new ByteArrayInputStream(src);
		try {
			byte[] ba = IOUtils.toByteArray(is);
			Assert.assertArrayEquals(ba, src);
		} catch (IOException ex) {
		}
	}

	@Test
	public void test_copyWithLength() {
		byte[] src = MathUtils.toByteArray(1, 100);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			IOUtils.copy(src, 50, os);
			Assert.assertArrayEquals(os.toByteArray(), MathUtils.toByteArray(1, 50));
		} catch (IOException ex) {
		}
	}
}
