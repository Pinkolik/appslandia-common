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

import com.appslandia.common.utils.DecimalUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class GeoDms {

	final int degrees;
	final int minutes;
	final double seconds;

	public GeoDms(int degrees, int minutes, double seconds) {
		this.degrees = degrees;
		this.minutes = minutes;
		this.seconds = seconds;
	}

	public GeoDms round(int secondsScale) {
		return new GeoDms(this.degrees, this.minutes, DecimalUtils.round(this.seconds, secondsScale));
	}

	public int getDegrees() {
		return this.degrees;
	}

	public int getMinutes() {
		return this.minutes;
	}

	public double getSeconds() {
		return this.seconds;
	}

	@Override
	public String toString() {
		return String.format("%d° %d' %f\"", this.degrees, this.minutes, this.seconds);
	}

	public String toString(Direction direction) {
		return String.format("%s %s", toString(), direction.symbol());
	}

	public static GeoDms valueOf(double degree) {
		degree = Math.abs(degree);

		int d = (int) degree;
		int m = (int) ((degree - d) * 60);
		double s = degree * 3600 - d * 3600 - m * 60;

		return new GeoDms(d, m, s);
	}
}
