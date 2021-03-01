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
import java.util.function.Function;

/**
 * 
 * 
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ProviderMap<K, V> extends HashMap<K, V> {
	private static final long serialVersionUID = 1L;

	final Function<K, V> factory;

	public ProviderMap(Function<K, V> factory) {
		super();
		this.factory = factory;
	}

	public ProviderMap(Function<K, V> factory, int initialCapacity) {
		super(initialCapacity);
		this.factory = factory;
	}

	public ProviderMap(Function<K, V> factory, int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
		this.factory = factory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public V get(Object key) {
		V v = super.get(key);
		if (v != null)
			return v;

		v = this.factory.apply((K) key);
		super.put((K) key, v);
		return v;
	}
}
