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

package com.appslandia.common.crypto;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class SecureConfigTest {

	@Test
	public void test() {
		Encryptor encryptor = new PbeEncryptor().setTransformation("AES").setKeySize(16).setPassword("password".toCharArray());
		TextEncryptor textEncryptor = new TextEncryptor().setEncryptor(encryptor);
		SecureConfig config = new SecureConfig(textEncryptor);

		try {
			config.enc("config", "value");
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Assert.assertEquals(config.getString("config"), "value");
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_password() {
		SecureConfig config = new SecureConfig("password".toCharArray());
		try {
			config.enc("config", "value");
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
		try {
			Assert.assertEquals(config.getString("config"), "value");
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_formatted() {
		SecureConfig config = new SecureConfig("password".toCharArray());
		try {
			config.set("database", "db");
			config.set("user", "user");
			config.enc("password", "pwd");
			config.set("url", "database=${database};user=${user};password=${password}");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
		try {
			Assert.assertEquals(config.getFormatted("url"), "database=db;user=user;password=pwd");
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}
}
