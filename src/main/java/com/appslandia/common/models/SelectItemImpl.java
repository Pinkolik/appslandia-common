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

package com.appslandia.common.models;

import java.util.Objects;

import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class SelectItemImpl implements SelectItem {

	public static final SelectItemImpl EMPTY = new SelectItemImpl(null, StringUtils.EMPTY_STRING);

	final Object value;
	final String displayName;
	final String name;

	public SelectItemImpl(Object value, String displayName) {
		this(value, displayName, null);
	}

	public SelectItemImpl(Object value, String displayName, String name) {
		this.value = value;
		this.displayName = displayName;
		this.name = name;
	}

	public int intValue() {
		return (Integer) AssertUtils.assertNotNull(this.value);
	}

	@Override
	public Object getValue() {
		return this.value;
	}

	@Override
	public String getDisplayName() {
		return this.displayName;
	}

	@Override
	public String getName() {
		return (this.name != null) ? this.name : this.displayName;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof SelectItemImpl)) {
			return false;
		}
		SelectItemImpl another = (SelectItemImpl) obj;
		return Objects.equals(this.value, another.value);
	}
}
