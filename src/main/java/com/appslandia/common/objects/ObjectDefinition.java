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
import java.util.Arrays;

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.ReflectionUtils;
import com.appslandia.common.utils.ValueUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ObjectDefinition extends InitializeObject {

	private Class<?>[] types;
	private Annotation[] qualifiers;
	private ObjectScope scope;

	private Class<?> implClass;
	private ObjectProducer<?> producer;

	@Override
	protected void init() throws Exception {
		AssertUtils.assertNotNull(this.types);
		AssertUtils.assertNotNull(this.scope);
		AssertUtils.assertTrue((this.implClass != null) || (this.producer != null));

		this.qualifiers = ValueUtils.valueOrAlt(this.qualifiers, ReflectionUtils.EMPTY_ANNOTATIONS);
	}

	public boolean hasType(Class<?> type) {
		initialize();
		return Arrays.stream(this.types).anyMatch(t -> t == type);
	}

	public Class<?>[] getTypes() {
		initialize();
		return this.types.clone();
	}

	public ObjectDefinition setTypes(Class<?>[] types) {
		assertNotInitialized();
		this.types = types;
		return this;
	}

	public Annotation[] getQualifiers() {
		initialize();
		return this.qualifiers;
	}

	public ObjectDefinition setQualifiers(Annotation... qualifiers) {
		assertNotInitialized();
		this.qualifiers = qualifiers;
		return this;
	}

	public ObjectScope getScope() {
		initialize();
		return this.scope;
	}

	public ObjectDefinition setScope(ObjectScope scope) {
		assertNotInitialized();
		this.scope = scope;
		return this;
	}

	public Class<?> getImplClass() {
		initialize();
		return this.implClass;
	}

	public ObjectDefinition setImplClass(Class<?> implClass) {
		assertNotInitialized();
		this.implClass = implClass;
		return this;
	}

	public ObjectProducer<?> getProducer() {
		initialize();
		return this.producer;
	}

	public ObjectDefinition setProducer(ObjectProducer<?> producer) {
		assertNotInitialized();
		this.producer = producer;
		return this;
	}
}
