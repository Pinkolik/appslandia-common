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

package com.appslandia.common.record;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.appslandia.common.base.CaseInsensitiveMap;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.ObjectUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class Record extends CaseInsensitiveMap<Object> {
	private static final long serialVersionUID = 1L;

	public Record() {
		super(new LinkedHashMap<>());
	}

	public Record(Map<String, Object> innerMap) {
		super(innerMap);
	}

	public Record set(String name, Object value) {
		this.put(name, value);
		return this;
	}

	public <T> T get(String name) {
		return ObjectUtils.cast(super.get(name));
	}

	public int getInt(String name) throws IllegalStateException {
		Integer val = (Integer) super.get(name);
		AssertUtils.assertStateNotNull(val);
		return val;
	}

	public List<Object> toValues(String[] columnLabels) {
		List<Object> values = new ArrayList<>(columnLabels.length);
		for (String label : columnLabels) {
			values.add(super.get(label));
		}
		return values;
	}
}
