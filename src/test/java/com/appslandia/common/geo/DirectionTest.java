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

package com.appslandia.common.geo;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class DirectionTest {

	@Test
	public void test_symbol() {
		Assert.assertEquals(Direction.EAST.symbol(), "E");
		Assert.assertEquals(Direction.WEST.symbol(), "W");
		Assert.assertEquals(Direction.SOUTH.symbol(), "S");
		Assert.assertEquals(Direction.NORTH.symbol(), "N");
	}

	@Test
	public void test_parseValue() {
		Assert.assertEquals(Direction.parseValue("E"), Direction.EAST);
		Assert.assertEquals(Direction.parseValue("W"), Direction.WEST);
		Assert.assertEquals(Direction.parseValue("S"), Direction.SOUTH);
		Assert.assertEquals(Direction.parseValue("N"), Direction.NORTH);
	}

	@Test
	public void test_ver_hor() {
		Assert.assertTrue(Direction.EAST.isHorizontal());
		Assert.assertTrue(Direction.WEST.isHorizontal());

		Assert.assertTrue(Direction.SOUTH.isVertical());
		Assert.assertTrue(Direction.NORTH.isVertical());
	}

	@Test
	public void test_reverse() {
		Assert.assertEquals(Direction.EAST.reverse(), Direction.WEST);
		Assert.assertEquals(Direction.WEST.reverse(), Direction.EAST);
		Assert.assertEquals(Direction.SOUTH.reverse(), Direction.NORTH);
		Assert.assertEquals(Direction.NORTH.reverse(), Direction.SOUTH);
	}

	@Test
	public void test_right() {
		Assert.assertEquals(Direction.EAST.right(), Direction.SOUTH);
		Assert.assertEquals(Direction.SOUTH.right(), Direction.WEST);
		Assert.assertEquals(Direction.WEST.right(), Direction.NORTH);
		Assert.assertEquals(Direction.NORTH.right(), Direction.EAST);
	}

	@Test
	public void test_right_n() {
		Assert.assertEquals(Direction.EAST.right(0), Direction.EAST);
		Assert.assertEquals(Direction.SOUTH.right(0), Direction.SOUTH);
		Assert.assertEquals(Direction.WEST.right(0), Direction.WEST);
		Assert.assertEquals(Direction.NORTH.right(0), Direction.NORTH);

		Assert.assertEquals(Direction.EAST.right(4), Direction.EAST);
		Assert.assertEquals(Direction.SOUTH.right(4), Direction.SOUTH);
		Assert.assertEquals(Direction.WEST.right(4), Direction.WEST);
		Assert.assertEquals(Direction.NORTH.right(4), Direction.NORTH);

		Assert.assertEquals(Direction.EAST.right(1), Direction.SOUTH);
		Assert.assertEquals(Direction.SOUTH.right(1), Direction.WEST);
		Assert.assertEquals(Direction.WEST.right(1), Direction.NORTH);
		Assert.assertEquals(Direction.NORTH.right(1), Direction.EAST);
	}

	@Test
	public void test_left() {
		Assert.assertEquals(Direction.EAST.left(), Direction.NORTH);
		Assert.assertEquals(Direction.NORTH.left(), Direction.WEST);
		Assert.assertEquals(Direction.WEST.left(), Direction.SOUTH);
		Assert.assertEquals(Direction.SOUTH.left(), Direction.EAST);
	}

	@Test
	public void test_left_n() {
		Assert.assertEquals(Direction.EAST.left(0), Direction.EAST);
		Assert.assertEquals(Direction.SOUTH.left(0), Direction.SOUTH);
		Assert.assertEquals(Direction.WEST.left(0), Direction.WEST);
		Assert.assertEquals(Direction.NORTH.left(0), Direction.NORTH);

		Assert.assertEquals(Direction.EAST.left(4), Direction.EAST);
		Assert.assertEquals(Direction.SOUTH.left(4), Direction.SOUTH);
		Assert.assertEquals(Direction.WEST.left(4), Direction.WEST);
		Assert.assertEquals(Direction.NORTH.left(4), Direction.NORTH);

		Assert.assertEquals(Direction.EAST.left(1), Direction.NORTH);
		Assert.assertEquals(Direction.NORTH.left(1), Direction.WEST);
		Assert.assertEquals(Direction.WEST.left(1), Direction.SOUTH);
		Assert.assertEquals(Direction.SOUTH.left(1), Direction.EAST);
	}
}
