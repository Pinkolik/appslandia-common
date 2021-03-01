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

import com.appslandia.common.base.ConfigMap;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class PropertyUtilsTest {

	@Test
	public void test_initialize_BEAN_PROPERTY_STRATEGY() {
		ConfigMap config = new ConfigMap();
		config.put("boolProp", "true");
		config.put("intProp", "100");
		config.put("stringProp", "str");

		TestJavaBean obj = new TestJavaBean();
		try {
			PropertyUtils.initialize(obj, config, PropertyUtils.BEAN_PROPERTY_STRATEGY);

			Assert.assertEquals(obj.isBoolProp(), true);
			Assert.assertEquals(obj.getIntProp(), 100);
			Assert.assertEquals(obj.getStringProp(), "str");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_initialize_METHOD_PROPERTY_STRATEGY() {
		ConfigMap config = new ConfigMap();
		config.put("boolProp", "true");
		config.put("intProp", "100");
		config.put("stringProp", "str");

		TestObject obj = new TestObject();
		try {
			PropertyUtils.initialize(obj, config, PropertyUtils.METHOD_PROPERTY_STRATEGY);

			Assert.assertEquals(obj.boolProp(), true);
			Assert.assertEquals(obj.intProp(), 100);
			Assert.assertEquals(obj.stringProp(), "str");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	static class TestJavaBean {

		private boolean boolProp;
		private int intProp;
		private String stringProp;

		public boolean isBoolProp() {
			return this.boolProp;
		}

		public void setBoolProp(boolean boolProp) {
			this.boolProp = boolProp;
		}

		public int getIntProp() {
			return this.intProp;
		}

		public void setIntProp(int intProp) {
			this.intProp = intProp;
		}

		public String getStringProp() {
			return this.stringProp;
		}

		public void setStringProp(String stringProp) {
			this.stringProp = stringProp;
		}
	}

	static class TestObject {

		private boolean boolProp;
		private int intProp;
		private String stringProp;

		public boolean boolProp() {
			return this.boolProp;
		}

		public void boolProp(boolean boolProp) {
			this.boolProp = boolProp;
		}

		public int intProp() {
			return this.intProp;
		}

		public void intProp(int intProp) {
			this.intProp = intProp;
		}

		public String stringProp() {
			return this.stringProp;
		}

		public void stringProp(String stringProp) {
			this.stringProp = stringProp;
		}
	}
}
