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

package com.appslandia.common.json;

import java.io.StringReader;

import org.junit.Assert;
import org.junit.Test;

import com.appslandia.common.utils.DateUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class GsonProcessorUtilDateTest {

	@Test
	public void test_toString() {
		GsonProcessor gsonProcessor = new GsonProcessor();
		User user = new User();
		user.created = DateUtils.iso8601Date("2000-01-01");

		try {
			String jsonStr = gsonProcessor.toString(user);

			Assert.assertTrue(jsonStr.contains("2000-01-01"));
		} catch (JsonException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_read() {
		GsonProcessor gp = new GsonProcessor();
		try {
			String jsonStr = "{\"created\":\"2000-01-01\"}";
			User user = gp.read(new StringReader(jsonStr), User.class);

			Assert.assertEquals("2000-01-01", DateUtils.iso8601Date(user.created));
		} catch (JsonException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	static class User {
		public java.util.Date created;
	}
}
