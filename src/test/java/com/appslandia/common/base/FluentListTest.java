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

import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class FluentListTest {

	@Test
	public void test_first() {
		FluentList<Integer> list = new FluentList<Integer>();
		try {
			list.first();
			Assert.fail();

		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof NoSuchElementException);
		}
		try {
			list.first(1);
			list.add(2);

			Assert.assertTrue(list.size() == 2);
			Assert.assertTrue(list.first() == 1);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_last() {
		FluentList<Integer> list = new FluentList<Integer>();
		try {
			list.last();
			Assert.fail();

		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof NoSuchElementException);
		}

		try {
			list.add(1);
			list.last(2);

			Assert.assertTrue(list.size() == 2);
			Assert.assertTrue(list.last() == 2);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_removeFirst() {
		FluentList<Integer> list = new FluentList<Integer>();
		try {
			list.removeFirst();
			Assert.fail();

		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof NoSuchElementException);
		}

		try {
			list.add(1);
			list.add(2);

			list.removeFirst();

			Assert.assertTrue(list.size() == 1);
			Assert.assertTrue(list.first() == 2);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_removeLast() {
		FluentList<Integer> list = new FluentList<Integer>();
		try {
			list.removeLast();
			Assert.fail();

		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof NoSuchElementException);
		}

		try {
			list.add(1);
			list.add(2);

			list.removeLast();

			Assert.assertTrue(list.size() == 1);
			Assert.assertTrue(list.first() == 1);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_ins() {
		FluentList<Integer> list = new FluentList<Integer>();

		try {
			list.ins(1, 1);
			Assert.fail();

		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IndexOutOfBoundsException);
		}

		try {
			list.ins(0, 1);

			Assert.assertTrue(list.size() == 1);
			Assert.assertTrue(list.first() == 1);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}
}
