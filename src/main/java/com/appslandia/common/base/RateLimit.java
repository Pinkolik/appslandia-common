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

import java.io.Serializable;
import java.util.regex.Pattern;

import com.appslandia.common.utils.DateUtils;

/**
 * 
 * 
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class RateLimit implements Serializable {
	private static final long serialVersionUID = 1L;

	final double accesses;
	final long windowsMs;

	public RateLimit(double accesses, long windowsMs) {
		this.accesses = accesses;
		this.windowsMs = windowsMs;
	}

	public double getAccesses() {
		return this.accesses;
	}

	public long getWindowMs() {
		return this.windowsMs;
	}

	public double getRatePerMs() {
		return this.accesses / this.windowsMs;
	}

	@Override
	public String toString() {
		return new ToStringBuilder().toString(this);
	}

	@ToStringBuilder.Exclude
	static final Pattern RATE_LIMIT_PATTERN = Pattern.compile("(\\d+.\\d+|\\d+)\\s*/\\s*\\d+(w|d|h|m|s|ms)", Pattern.CASE_INSENSITIVE);

	public static RateLimit parse(String rateLimit) throws IllegalArgumentException {
		if (!RATE_LIMIT_PATTERN.matcher(rateLimit).matches()) {
			throw new IllegalArgumentException("rateLimit is invalid (value=" + rateLimit + ")");
		}
		int idx = rateLimit.indexOf('/');

		double accesses = Double.parseDouble(rateLimit.substring(0, idx).trim());
		long windowsMs = DateUtils.translateToMs(rateLimit.substring(idx + 1).trim());

		if (accesses == 0.0) {
			throw new IllegalArgumentException("accesses must be positive (rateLimit=" + rateLimit + ")");
		}
		if (windowsMs == 0) {
			throw new IllegalArgumentException("windowsMs must be positive (rateLimit=" + rateLimit + ")");
		}
		return new RateLimit(accesses, windowsMs);
	}
}
