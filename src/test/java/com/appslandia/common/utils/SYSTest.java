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

import com.appslandia.common.base.Out;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class SYSTest {

	@Test
	public void test_PROP() {
		try {
			System.setProperty("password", "12345");

			Out<Boolean> isExpr = new Out<>();
			String resolvedValue = SYS.resolveExpr("${password}", isExpr);

			Assert.assertNotNull(resolvedValue);
			Assert.assertEquals(resolvedValue, "12345");
			Assert.assertTrue(isExpr.val());

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());

		} finally {
			System.getProperties().remove("password");
		}
	}

	@Test
	public void test_PROP_novalue() {
		try {
			Out<Boolean> isExpr = new Out<>();
			String resolvedValue = SYS.resolveExpr("${password}", isExpr);

			Assert.assertNull(resolvedValue);
			Assert.assertTrue(isExpr.val());

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_PROP_default() {
		try {
			Out<Boolean> isExpr = new Out<>();
			String resolvedValue = SYS.resolveExpr("${password:12345}", isExpr);

			Assert.assertNotNull(resolvedValue);
			Assert.assertEquals(resolvedValue, "12345");
			Assert.assertTrue(isExpr.val());

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_ENV() {
		if (System.getenv("TMP") == null) {
			return;
		}
		try {
			Out<Boolean> isExpr = new Out<>();
			String resolvedValue = SYS.resolveExpr("${env.TMP}", isExpr);

			Assert.assertNotNull(resolvedValue);
			Assert.assertTrue(isExpr.val());

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_ENV_novalue() {
		try {
			Out<Boolean> isExpr = new Out<>();
			String resolvedValue = SYS.resolveExpr("${env.TMP_UPLOAD_XYZT}", isExpr);

			Assert.assertNull(resolvedValue);
			Assert.assertTrue(isExpr.val());

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_ENV_default() {
		try {
			Out<Boolean> isExpr = new Out<>();
			String resolvedValue = SYS.resolveExpr("${env.TMP_UPLOAD_XYZT:/upload}", isExpr);

			Assert.assertNotNull(resolvedValue);
			Assert.assertEquals(resolvedValue, "/upload");
			Assert.assertTrue(isExpr.val());

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_PROP_ENV() {
		if (System.getenv("TMP") == null) {
			return;
		}
		try {
			Out<Boolean> isExpr = new Out<>();
			String resolvedValue = SYS.resolveExpr("${temp_upload,env.TMP}", isExpr);

			Assert.assertNotNull(resolvedValue);
			Assert.assertTrue(isExpr.val());

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}
}
