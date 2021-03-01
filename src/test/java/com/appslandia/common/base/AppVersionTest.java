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
public class AppVersionTest {

	@Test
	public void test_parse() {
		AppVersion version = AppVersion.parse("1.2.3.4");

		Assert.assertEquals(version.getMajor(), 1);
		Assert.assertEquals(version.getMinor(), 2);

		Assert.assertNotNull(version.getBuild());
		Assert.assertNotNull(version.getRevision());

		Assert.assertEquals(version.getBuild().intValue(), 3);
		Assert.assertEquals(version.getRevision().intValue(), 4);

		Assert.assertEquals(version.toString(), "1.2.3.4");

		version = AppVersion.parse("1.2");

		Assert.assertEquals(version.getMajor(), 1);
		Assert.assertEquals(version.getMinor(), 2);

		Assert.assertNull(version.getBuild());
		Assert.assertNull(version.getRevision());

		Assert.assertEquals(version.toString(), "1.2");
	}

	@Test
	public void test_invalid() {
		try {
			AppVersion.parse("1.-2");
			Assert.fail();
		} catch (Exception ex) {
		}

		try {
			AppVersion.parse("1.02");
			Assert.fail();
		} catch (Exception ex) {
		}
	}

	@Test
	public void test_compare() {
		AppVersion version1 = AppVersion.parse("1.2");
		AppVersion version2 = AppVersion.parse("1.2.1");

		Assert.assertTrue(version1.compareTo(version2) == -1);
	}

	@Test
	public void test_equals() {
		AppVersion version1 = AppVersion.parse("1.2");
		AppVersion version2 = AppVersion.parse("1.2");

		Assert.assertTrue(version1.equals(version2));
	}
}
