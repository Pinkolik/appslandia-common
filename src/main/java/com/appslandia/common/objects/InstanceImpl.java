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

package com.appslandia.common.objects;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.inject.Instance;
import javax.enterprise.util.TypeLiteral;

import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.CollectionUtils;
import com.appslandia.common.utils.ObjectUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class InstanceImpl<T> implements Instance<T> {

	final Class<?> type;
	final Annotation[] qualifiers;
	final List<ObjectInstance> instances;

	public InstanceImpl(Class<?> type, Annotation[] qualifiers, List<ObjectInstance> instances) {
		this.type = type;
		this.qualifiers = qualifiers;
		this.instances = instances;
	}

	public int getCount() {
		return this.instances.size();
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			int index = -1;

			@Override
			public T next() {
				ObjectInstance objInst = instances.get(++this.index);
				return ObjectUtils.cast(objInst.getInstance());
			}

			@Override
			public boolean hasNext() {
				return this.index < instances.size() - 1;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public T get() {
		if (isUnsatisfied()) {
			throw new ObjectException("Unsatisfied dependency (type=" + this.type + ", qualifiers=" + Arrays.toString(this.qualifiers));
		}
		if (isAmbiguous()) {
			throw new ObjectException("Ambiguous dependency (type=" + this.type + ", qualifiers=" + Arrays.toString(this.qualifiers));
		}
		return ObjectUtils.cast(this.instances.get(0).getInstance());
	}

	@Override
	public Instance<T> select(Annotation... qualifiers) {
		if (qualifiers.length == 0) {
			return this;
		}
		Annotation[] childQualifiers = getChildQualifiers(qualifiers);
		List<ObjectInstance> sub = new ArrayList<>();

		for (ObjectInstance objInst : this.instances) {
			if (AnnotationUtils.hasAnnotations(objInst.definition.getQualifiers(), childQualifiers)) {
				sub.add(objInst);
			}
		}
		return new InstanceImpl<>(this.type, childQualifiers, sub);
	}

	@Override
	public <U extends T> Instance<U> select(Class<U> subtype, Annotation... qualifiers) {
		AssertUtils.assertNotNull(subtype);

		if ((this.type == subtype) && (qualifiers.length == 0)) {
			return ObjectUtils.cast(this);
		}
		Annotation[] childQualifiers = getChildQualifiers(qualifiers);
		List<ObjectInstance> sub = new ArrayList<>();

		for (ObjectInstance objInst : this.instances) {
			if (AnnotationUtils.hasAnnotations(objInst.definition.getQualifiers(), childQualifiers)) {
				if (subtype.isInstance(objInst.getInstance())) {
					sub.add(objInst);
				}
			}
		}
		return new InstanceImpl<>(subtype, childQualifiers, sub);
	}

	@Override
	public <U extends T> Instance<U> select(TypeLiteral<U> subtype, Annotation... qualifiers) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isUnsatisfied() {
		return this.instances.isEmpty();
	}

	@Override
	public boolean isAmbiguous() {
		return this.instances.size() > 1;
	}

	@Override
	public void destroy(T instance) {
		AssertUtils.assertNotNull(instance);
		ObjectFactoryUtils.destroy(instance);
	}

	private Annotation[] getChildQualifiers(Annotation[] qualifiers) {
		Set<Annotation> s = CollectionUtils.toSet(new LinkedHashSet<>(), this.qualifiers);
		CollectionUtils.toSet(s, qualifiers);
		return s.toArray(new Annotation[s.size()]);
	}
}
