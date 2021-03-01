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

import com.appslandia.common.utils.AssertUtils;

/**
 * 
 * 
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class AppVersion implements Comparable<AppVersion>, Serializable {
	private static final long serialVersionUID = 1L;

	private int major;
	private int minor;

	private Integer build;
	private Integer revision;

	static final Pattern VERSION_PATTERN = Pattern.compile("[1-9]\\d*(\\.(0|[1-9]\\d*)){1,3}");

	public static AppVersion parse(String version) {
		AssertUtils.assertNotNull(version);

		if (!VERSION_PATTERN.matcher(version).matches()) {
			throw new IllegalArgumentException("version is invalid (value=" + version + ")");
		}

		String[] numbers = version.split("\\.");
		AppVersion appVersion = new AppVersion();

		appVersion.major = Integer.parseInt(numbers[0]);
		appVersion.minor = Integer.parseInt(numbers[1]);

		if (numbers.length >= 3) {
			appVersion.build = Integer.parseInt(numbers[2]);
		}

		if (numbers.length == 4) {
			appVersion.revision = Integer.parseInt(numbers[3]);
		}
		return appVersion;
	}

	@Override
	public int compareTo(AppVersion another) {
		AssertUtils.assertNotNull(another);

		int compare = Integer.compare(this.major, another.major);
		if (compare != 0) {
			return compare;
		}
		compare = Integer.compare(this.minor, another.minor);
		if (compare != 0) {
			return compare;
		}

		compare = Integer.compare(this.build != null ? this.build : 0, another.build != null ? another.build : 0);
		if (compare != 0) {
			return compare;
		}
		return Integer.compare(this.revision != null ? this.revision : 0, another.revision != null ? another.revision : 0);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AppVersion)) {
			return false;
		}
		AppVersion another = (AppVersion) obj;
		return this.compareTo(another) == 0;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.major);
		sb.append('.').append(this.minor);

		if (this.build != null) {
			sb.append(".").append(this.build);

			if (this.revision != null) {
				sb.append('.').append(this.revision);
			}
		}
		return sb.toString();
	}

	public int getMajor() {
		return this.major;
	}

	public int getMinor() {
		return this.minor;
	}

	public Integer getBuild() {
		return this.build;
	}

	public Integer getRevision() {
		return this.revision;
	}
}
