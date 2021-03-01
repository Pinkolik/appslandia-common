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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 
 * 
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class FixedQueue<E> implements Queue<E>, Iterable<E> {

	private final int size;
	private final Queue<E> queue = new LinkedList<E>();

	public FixedQueue(int size) {
		this.size = size;
	}

	private boolean trim() {
		boolean changed = this.queue.size() > this.size;
		while (this.queue.size() > size) {
			this.queue.remove();
		}
		return changed;
	}

	@Override
	public boolean add(E o) {
		boolean changed = this.queue.add(o);
		boolean trimmed = trim();
		return changed || trimmed;
	}

	@Override
	public int size() {
		return this.queue.size();
	}

	@Override
	public boolean isEmpty() {
		return this.queue.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return this.queue.contains(o);
	}

	@Override
	public Iterator<E> iterator() {
		return this.queue.iterator();
	}

	@Override
	public Object[] toArray() {
		return this.queue.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return this.queue.toArray(a);
	}

	@Override
	public boolean remove(Object o) {
		return this.queue.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return this.queue.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean changed = this.queue.addAll(c);
		boolean trimmed = trim();
		return changed || trimmed;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return this.queue.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return this.queue.retainAll(c);
	}

	@Override
	public void clear() {
		this.queue.clear();
	}

	@Override
	public boolean offer(E e) {
		boolean changed = this.queue.offer(e);
		boolean trimmed = trim();
		return changed || trimmed;
	}

	@Override
	public E remove() {
		return this.queue.remove();
	}

	@Override
	public E poll() {
		return this.queue.poll();
	}

	@Override
	public E element() {
		return this.queue.element();
	}

	@Override
	public E peek() {
		return this.queue.peek();
	}
}
