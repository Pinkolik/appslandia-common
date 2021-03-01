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

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public interface MapAccessor<K, V> extends Map<K, V> {

	default public int size() {
		throw new UnsupportedOperationException();
	}

	default public boolean isEmpty() {
		throw new UnsupportedOperationException();
	}

	default public boolean containsKey(Object key) {
		throw new UnsupportedOperationException();
	}

	default public boolean containsValue(Object value) {
		throw new UnsupportedOperationException();
	}

	default public V put(K key, V value) {
		throw new UnsupportedOperationException();
	}

	default public V remove(Object key) {
		throw new UnsupportedOperationException();
	}

	default public void putAll(Map<? extends K, ? extends V> m) {
		throw new UnsupportedOperationException();
	}

	default public void clear() {
		throw new UnsupportedOperationException();
	}

	default public Set<K> keySet() {
		throw new UnsupportedOperationException();
	}

	default public Collection<V> values() {
		throw new UnsupportedOperationException();
	}

	default public Set<Entry<K, V>> entrySet() {
		throw new UnsupportedOperationException();
	}
}
