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

import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.DecimalUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class GeoLocation {

	final double latitude;
	final double longitude;

	public GeoLocation(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;

		AssertUtils.assertTrue((this.latitude >= -90.0) && (this.latitude <= 90.0), "latitude is invalid [-90.0, 90.0]");
		AssertUtils.assertTrue((this.longitude >= -180.0) && (this.longitude <= 180.0), "longitude is invalid [-180.0, 180.0]");
	}

	public double getLatitude() {
		return this.latitude;
	}

	public double getLongitude() {
		return this.longitude;
	}

	public GeoLocation round(int scale) {
		return new GeoLocation(DecimalUtils.round(this.latitude, scale), DecimalUtils.round(this.longitude, scale));
	}

	public GeoLocation move(Direction direction, double distance, DistanceUnit unit) {
		AssertUtils.assertNotNull(direction);
		AssertUtils.assertNotNull(unit);

		double perdegLong = 360.0 / GeoUtils.POLAR_CIRCUMFERENCE_MILES;
		double perdegLat = 360.0 / (Math.cos(Math.toRadians(this.latitude)) * GeoUtils.EQUATOR_CIRCUMFERENCE_MILES);

		switch (direction) {
		case NORTH:
			return new GeoLocation(this.latitude + DistanceUnit.MILE.convert(distance, unit) * perdegLong, this.longitude);

		case SOUTH:
			return new GeoLocation(this.latitude - DistanceUnit.MILE.convert(distance, unit) * perdegLong, this.longitude);

		case EAST:
			return new GeoLocation(this.latitude, this.longitude + DistanceUnit.MILE.convert(distance, unit) * perdegLat);

		case WEST:
			return new GeoLocation(this.latitude, this.longitude - DistanceUnit.MILE.convert(distance, unit) * perdegLat);
		default:
			throw new Error();
		}
	}

	public double distanceTo(GeoLocation to, DistanceUnit unit) {
		AssertUtils.assertNotNull(to);
		AssertUtils.assertNotNull(unit);

		double rlt1 = Math.toRadians(this.latitude);
		double rlt2 = Math.toRadians(to.latitude);

		double rlg1 = Math.toRadians(this.longitude);
		double rlg2 = Math.toRadians(to.longitude);

		// https://en.wikipedia.org/wiki/Haversine_formula
		double h = Math.sin((rlt2 - rlt1) / 2) * Math.sin((rlt2 - rlt1) / 2)
				+ Math.cos(rlt1) * Math.cos(rlt2) * Math.sin((rlg2 - rlg1) / 2) * Math.sin((rlg2 - rlg1) / 2);

		double d = 2 * GeoUtils.EARTH_RADIUS_METER * Math.asin(Math.sqrt(h));
		return unit.convert(d, DistanceUnit.METER);
	}

	@Override
	public String toString() {
		return String.format("%.8f, %.8f", this.latitude, this.longitude);
	}
}
