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

import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

import com.appslandia.common.utils.AssertUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public enum Direction {

	EAST("E"), NORTH("N"), SOUTH("S"), WEST("W");

	final String symbol;

	private Direction(String symbol) {
		this.symbol = symbol;
	}

	public String symbol() {
		return this.symbol;
	}

	public boolean isHorizontal() {
		return this == EAST || this == WEST;
	}

	public boolean isVertical() {
		return this == NORTH || this == SOUTH;
	}

	public Direction reverse() {
		return left(2);
	}

	/**
	 * The direction 90*n-degrees to the right or left.
	 * 
	 * @param n
	 * @return
	 */
	public Direction move(int n) {
		if (n == 0)
			return this;
		if (n > 0)
			return right(n);
		return left(-n);
	}

	/**
	 * The direction 90-degrees to the right.
	 * 
	 * @return
	 */
	public Direction right() {
		switch (this) {
		case EAST:
			return SOUTH;
		case SOUTH:
			return WEST;
		case WEST:
			return NORTH;
		case NORTH:
			return EAST;
		default:
			throw new Error();
		}
	}

	/**
	 * The direction 90*n-degrees to the right.
	 * 
	 * @param n
	 * @return
	 */
	public Direction right(int n) {
		AssertUtils.assertNonNegative(n);
		if (n == 0)
			return this;

		int m = n % 4;
		Direction dir = this;
		while (m-- > 0)
			dir = dir.right();
		return dir;
	}

	/**
	 * The direction 90-degrees to the left.
	 * 
	 * @return
	 */
	public Direction left() {
		switch (this) {
		case EAST:
			return NORTH;
		case NORTH:
			return WEST;
		case WEST:
			return SOUTH;
		case SOUTH:
			return EAST;
		default:
			throw new Error();
		}
	}

	/**
	 * The direction 90*n-degrees to the left.
	 * 
	 * @param n
	 * @return
	 */
	public Direction left(int n) {
		AssertUtils.assertNonNegative(n);
		if (n == 0)
			return this;

		int m = n % 4;
		Direction dir = this;
		while (m-- > 0)
			dir = dir.left();
		return dir;
	}

	public static Direction parseValue(String symbol) {
		AssertUtils.assertNotNull(symbol);
		symbol = symbol.toUpperCase(Locale.ENGLISH);

		switch (symbol) {
		case "E":
			return EAST;
		case "N":
			return NORTH;
		case "S":
			return SOUTH;
		case "W":
			return WEST;
		default:
			break;
		}
		throw new IllegalArgumentException("symbol is invalid (value=" + symbol + ")");
	}

	public static Direction random() {
		int ordinial = ThreadLocalRandom.current().nextInt(4);
		return Direction.values()[ordinial];
	}
}
