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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class CaseInsensitiveSet implements Set<String>, Serializable {
	private static final long serialVersionUID = 1L;

	final Set<String> elements;

	public CaseInsensitiveSet() {
		this(new HashSet<String>());
	}

	public CaseInsensitiveSet(Set<String> newSet) {
		this.elements = newSet;
	}

	@Override
	public int size() {
		return this.elements.size();
	}

	@Override
	public boolean isEmpty() {
		return this.elements.isEmpty();
	}

	@Override
	public boolean contains(Object e) {
		return this.elements.contains(toLowerCase((String) e));
	}

	@Override
	public Iterator<String> iterator() {
		return this.elements.iterator();
	}

	@Override
	public Object[] toArray() {
		return this.elements.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return this.elements.toArray(a);
	}

	@Override
	public boolean add(String e) {
		return this.elements.add(toLowerCase(e));
	}

	@Override
	public boolean remove(Object e) {
		return this.elements.remove(toLowerCase((String) e));
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection<? extends String> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		this.elements.clear();
	}

	static String toLowerCase(String key) {
		return (key != null) ? key.toLowerCase(Locale.ENGLISH) : null;
	}
}
