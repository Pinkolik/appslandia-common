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

package com.appslandia.common.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import com.appslandia.common.base.LruMap;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class TagList implements Iterable<String> {

	final Collection<String> preTags;
	final Set<String> tags;

	public TagList() {
		this(Collections.emptySet());
	}

	public TagList(Collection<String> preTags) {
		this(preTags, 32);
	}

	public TagList(Collection<String> preTags, int maxSize) {
		this.preTags = preTags;
		this.tags = Collections.newSetFromMap(new LruMap<>(maxSize));
	}

	public boolean add(String tag) {
		if (tag == null) {
			return false;
		}
		if (this.preTags.contains(tag)) {
			return false;
		}
		return this.tags.add(tag);
	}

	public boolean remove(String tag) {
		if (tag == null) {
			return false;
		}
		if (this.preTags.contains(tag)) {
			return false;
		}
		return this.tags.remove(tag);
	}

	public boolean isPreTag(String tag) {
		return this.preTags.contains(tag);
	}

	@Override
	public Iterator<String> iterator() {
		return new Iterator<String>() {

			final Iterator<String> it1 = preTags.iterator();
			final Iterator<String> it2 = tags.iterator();

			@Override
			public boolean hasNext() {
				return it1.hasNext() || it2.hasNext();
			}

			@Override
			public String next() {
				if (it1.hasNext())
					return it1.next();

				return it2.next();
			}
		};
	}
}
