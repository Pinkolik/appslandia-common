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
public class BOMTest {

	@Test
	public void test_parse_UTF8() {
		byte[] bom = new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 0 };
		BOM b = BOM.parse(bom, 3);
		Assert.assertNotNull(b);
		Assert.assertEquals(b, BOM.UTF_8);
	}

	@Test
	public void test_parse_UTF_16BE() {
		byte[] bom = new byte[] { (byte) 0xFE, (byte) 0xFF, 0, 0 };
		BOM b = BOM.parse(bom, 2);
		Assert.assertNotNull(b);
		Assert.assertEquals(b, BOM.UTF_16BE);
	}

	@Test
	public void test_parse_UTF_16LE() {
		byte[] bom = new byte[] { (byte) 0xFF, (byte) 0xFE, 0, 0 };
		BOM b = BOM.parse(bom, 2);
		Assert.assertNotNull(b);
		Assert.assertEquals(b, BOM.UTF_16LE);
	}

	@Test
	public void test_parse_UTF_32BE() {
		byte[] bom = new byte[] { 0, 0, (byte) 0xFE, (byte) 0xFF };
		BOM b = BOM.parse(bom, 4);
		Assert.assertNotNull(b);
		Assert.assertEquals(b, BOM.UTF_32BE);
	}

	@Test
	public void test_parse_UTF_32LE() {
		byte[] bom = new byte[] { (byte) 0xFF, (byte) 0xFE, 0, 0 };
		BOM b = BOM.parse(bom, 4);
		Assert.assertNotNull(b);
		Assert.assertEquals(b, BOM.UTF_32LE);
	}

	@Test
	public void test_others() {
		byte[] bom = new byte[] { (byte) 0xFF, (byte) 0xFE, 0, 0 };
		BOM b = BOM.parse(bom, 2);
		Assert.assertNotNull(b);
		Assert.assertEquals(b, BOM.UTF_16LE);

		b = BOM.parse(bom, 4);
		Assert.assertNotNull(b);
		Assert.assertEquals(b, BOM.UTF_32LE);
	}
}
