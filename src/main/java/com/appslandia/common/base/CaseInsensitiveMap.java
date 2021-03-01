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
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class CaseInsensitiveMap<V> implements Map<String, V>, Serializable {
	private static final long serialVersionUID = 1L;

	final Map<String, V> map;

	public CaseInsensitiveMap() {
		this(new HashMap<String, V>());
	}

	public CaseInsensitiveMap(Map<String, V> innerMap) {
		this.map = innerMap;
	}

	@Override
	public int size() {
		return this.map.size();
	}

	@Override
	public boolean isEmpty() {
		return this.map.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return this.map.containsKey(toLowerCase((String) key));
	}

	@Override
	public boolean containsValue(Object value) {
		return this.map.containsValue(value);
	}

	@Override
	public V get(Object key) {
		return this.map.get(toLowerCase((String) key));
	}

	@Override
	public V put(String key, V value) {
		return this.map.put(toLowerCase(key), value);
	}

	@Override
	public V remove(Object key) {
		return this.map.remove(toLowerCase((String) key));
	}

	@Override
	public void putAll(Map<? extends String, ? extends V> m) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		this.map.clear();
	}

	@Override
	public Set<String> keySet() {
		return this.map.keySet();
	}

	@Override
	public Collection<V> values() {
		return this.map.values();
	}

	@Override
	public Set<Entry<String, V>> entrySet() {
		return this.map.entrySet();
	}

	static String toLowerCase(String key) {
		return (key != null) ? key.toLowerCase(Locale.ENGLISH) : null;
	}
}
