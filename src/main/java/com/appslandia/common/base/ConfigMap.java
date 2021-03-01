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

import java.util.HashMap;
import java.util.Map;

import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ConfigMap extends MapWrapper<String, String> implements Config {
	private static final long serialVersionUID = 1L;

	public ConfigMap() {
		super(new HashMap<String, String>());
	}

	public ConfigMap(Map<String, String> newMap) {
		super(newMap);
	}

	@Override
	public String getString(String key) {
		return this.map.get(key);
	}

	@Override
	public String get(Object key) {
		return getString((String) key);
	}

	@Override
	public void putAll(Map<? extends String, ? extends String> m) {
		for (Entry<? extends String, ? extends String> config : m.entrySet()) {
			this.map.put(config.getKey(), StringUtils.trimToNull(config.getValue()));
		}
	}

	@Override
	public String put(String key, String value) {
		return this.map.put(key, StringUtils.trimToNull(value));
	}

	public ConfigMap set(String key, String value) {
		put(key, value);
		return this;
	}

	public ConfigMap set(String key, boolean value) {
		this.map.put(key, Boolean.toString(value));
		return this;
	}

	public ConfigMap set(String key, int value) {
		this.map.put(key, Integer.toString(value));
		return this;
	}

	public ConfigMap set(String key, long value) {
		this.map.put(key, Long.toString(value));
		return this;
	}

	public ConfigMap set(String key, double value) {
		this.map.put(key, Double.toString(value));
		return this;
	}
}
