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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

/**
 * 
 * 
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class FluentList<E> implements List<E>, RandomAccess, Cloneable, java.io.Serializable {
	private static final long serialVersionUID = 1L;

	protected final List<E> list;

	public FluentList() {

		this(new ArrayList<E>());
	}

	public FluentList(List<E> list) {
		this.list = list;
	}

	public FluentList<E> first(E e) {
		this.list.add(0, e);
		return this;
	}

	public FluentList<E> last(E e) {
		this.list.add(e);
		return this;
	}

	public FluentList<E> ins(E e) {
		this.list.add(e);
		return this;
	}

	public FluentList<E> ins(int index, E e) {
		this.list.add(index, e);
		return this;
	}

	public FluentList<E> ins(int index, Collection<? extends E> c) {
		this.list.addAll(index, c);
		return this;
	}

	public FluentList<E> ins(Collection<? extends E> c) {
		this.list.addAll(c);
		return this;
	}

	/**
	 *
	 * @throws NoSuchElementException if this list is empty
	 */
	public E first() {
		assertNotEmpty();

		return this.list.get(0);
	}

	/**
	 *
	 * @throws NoSuchElementException if this list is empty
	 */
	public E last() {
		assertNotEmpty();

		return this.list.get(this.list.size() - 1);
	}

	/**
	 *
	 * @throws NoSuchElementException if this list is empty
	 */
	public FluentList<E> removeFirst() {
		assertNotEmpty();

		this.list.remove(0);
		return this;
	}

	/**
	 *
	 * @throws NoSuchElementException if this list is empty
	 */
	public FluentList<E> removeLast() {
		assertNotEmpty();

		this.list.remove(this.list.size() - 1);
		return this;
	}

	void assertNotEmpty() {
		if (this.list.isEmpty()) {
			throw new NoSuchElementException("The list is empty.");
		}
	}

	@Override
	public boolean add(E e) {
		return this.list.add(e);
	}

	@Override
	public void add(int index, E element) {
		this.list.add(index, element);
	}

	@Override
	public boolean addAll(java.util.Collection<? extends E> c) {
		return this.list.addAll(c);
	}

	@Override
	public boolean addAll(int index, java.util.Collection<? extends E> c) {
		return this.list.addAll(index, c);
	}

	@Override
	public void clear() {
		this.list.clear();
	}

	@Override
	public boolean contains(java.lang.Object o) {
		return this.list.contains(o);
	}

	@Override
	public boolean containsAll(java.util.Collection<?> c) {
		return this.list.containsAll(c);
	}

	@Override
	public boolean equals(java.lang.Object o) {
		return this.list.equals(o);
	}

	@Override
	public E get(int index) {
		return this.list.get(index);
	}

	@Override
	public int hashCode() {
		return this.list.hashCode();
	}

	@Override
	public int indexOf(java.lang.Object o) {
		return this.list.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return this.list.isEmpty();
	}

	@Override
	public java.util.Iterator<E> iterator() {
		return this.list.iterator();
	}

	@Override
	public int lastIndexOf(java.lang.Object o) {
		return this.list.lastIndexOf(o);
	}

	@Override
	public java.util.ListIterator<E> listIterator() {
		return this.list.listIterator();
	}

	@Override
	public java.util.ListIterator<E> listIterator(int index) {
		return this.list.listIterator(index);
	}

	@Override
	public boolean remove(java.lang.Object o) {
		return this.list.remove(o);
	}

	@Override
	public E remove(int index) {
		return this.list.remove(index);
	}

	@Override
	public boolean removeAll(java.util.Collection<?> c) {
		return this.list.removeAll(c);
	}

	@Override
	public void replaceAll(java.util.function.UnaryOperator<E> operator) {
		this.list.replaceAll(operator);
	}

	@Override
	public boolean retainAll(java.util.Collection<?> c) {
		return this.list.retainAll(c);
	}

	@Override
	public E set(int index, E element) {
		return this.list.set(index, element);
	}

	@Override
	public int size() {
		return this.list.size();
	}

	@Override
	public void sort(java.util.Comparator<? super E> c) {
		this.list.sort(c);
	}

	@Override
	public java.util.List<E> subList(int fromIndex, int toIndex) {
		return this.list.subList(fromIndex, toIndex);
	}

	@Override
	public java.lang.Object[] toArray() {
		return this.list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return this.list.toArray(a);
	}
}
