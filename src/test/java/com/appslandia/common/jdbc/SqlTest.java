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

package com.appslandia.common.jdbc;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class SqlTest {

	@Test
	public void test() {
		String sqlText = "SELECT * FROM User";
		Sql sql = new Sql().sql(sqlText);
		Assert.assertEquals(sql.getTranslatedSql(), sqlText);
	}

	@Test
	public void test_namedParams() {
		String sqlText = "SELECT * FROM User WHERE userId=:id";
		Sql sql = new Sql().sql(sqlText);

		Assert.assertEquals(sql.getTranslatedSql(), "SELECT * FROM User WHERE userId=?");

		Assert.assertArrayEquals(sql.getIndexes("id"), new int[] { 1 });
	}

	@Test
	public void test_repeatedParams() {
		String sqlText = "SELECT * FROM User WHERE :userName='' OR userName LIKE :userName";
		Sql sql = new Sql().sql(sqlText);

		Assert.assertEquals(sql.getTranslatedSql(), "SELECT * FROM User WHERE ?='' OR userName LIKE ?");
		Assert.assertArrayEquals(sql.getIndexes("userName"), new int[] { 1, 2 });
	}

	@Test
	public void test_IN() {
		String sqlText = "SELECT * FROM User WHERE userId IN :ids";
		Sql sql = new Sql().sql(sqlText).arrayLen("ids", 3);
		Assert.assertEquals(sql.getTranslatedSql(), "SELECT * FROM User WHERE userId IN (?, ?, ?)");

		try {
			int len = sql.getArrayLen("ids");
			Assert.assertEquals(len, 3);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		Assert.assertArrayEquals(sql.getIndexes("ids__0"), new int[] { 1 });
		Assert.assertArrayEquals(sql.getIndexes("ids__1"), new int[] { 2 });
		Assert.assertArrayEquals(sql.getIndexes("ids__2"), new int[] { 3 });

	}

	@Test
	public void test_LIKE_ANY() {
		String sqlText = "SELECT * FROM User WHERE userName LIKE_ANY :names";
		Sql sql = new Sql().sql(sqlText).arrayLen("names", 3);
		Assert.assertEquals(sql.getTranslatedSql(), "SELECT * FROM User WHERE userName LIKE ? OR userName LIKE ? OR userName LIKE ?");

		try {
			int len = sql.getArrayLen("names");
			Assert.assertEquals(len, 3);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		Assert.assertArrayEquals(sql.getIndexes("names__0"), new int[] { 1 });
		Assert.assertArrayEquals(sql.getIndexes("names__1"), new int[] { 2 });
		Assert.assertArrayEquals(sql.getIndexes("names__2"), new int[] { 3 });
	}

	@Test
	public void test_complicated() {
		String sqlText = "SELECT * FROM User WHERE userType=:userType AND userId IN :userIds AND (userName LIKE_ANY :names)";
		Sql sql = new Sql().sql(sqlText).arrayLen("names", 2).arrayLen("userIds", 2);

		Assert.assertEquals(sql.getTranslatedSql(), "SELECT * FROM User WHERE userType=? AND userId IN (?, ?) AND (userName LIKE ? OR userName LIKE ?)");

		try {
			int len = sql.getArrayLen("names");
			Assert.assertEquals(len, 2);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			int len = sql.getArrayLen("userIds");
			Assert.assertEquals(len, 2);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		Assert.assertArrayEquals(sql.getIndexes("userType"), new int[] { 1 });
		Assert.assertArrayEquals(sql.getIndexes("userIds__0"), new int[] { 2 });
		Assert.assertArrayEquals(sql.getIndexes("userIds__1"), new int[] { 3 });

		Assert.assertArrayEquals(sql.getIndexes("names__0"), new int[] { 4 });
		Assert.assertArrayEquals(sql.getIndexes("names__1"), new int[] { 5 });
	}
}
