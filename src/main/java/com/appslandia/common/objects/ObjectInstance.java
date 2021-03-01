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

import java.util.function.Function;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ObjectInstance {

	final ObjectDefinition definition;
	private volatile Object instance;
	final Function<ObjectDefinition, Object> factory;

	final Object mutex = new Object();

	public ObjectInstance(ObjectDefinition definition, Function<ObjectDefinition, Object> factory) {
		this.definition = definition;
		this.factory = factory;
	}

	public ObjectDefinition getDefinition() {
		return this.definition;
	}

	public Object getInstance() {
		// PROTOTYPE
		if (this.definition.getScope() == ObjectScope.PROTOTYPE) {
			return factory.apply(this.definition);
		}

		// SINGLETON
		Object obj = this.instance;
		if (obj == null) {
			synchronized (this.mutex) {
				if ((obj = this.instance) == null) {
					this.instance = obj = this.factory.apply(this.definition);
				}
			}
		}
		return obj;
	}

	public void clearInstance() {
		this.instance = null;
	}
}
