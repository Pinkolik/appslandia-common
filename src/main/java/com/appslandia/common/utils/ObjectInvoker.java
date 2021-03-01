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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ObjectInvoker {

	final Object obj;

	public ObjectInvoker(Object obj) {
		this.obj = AssertUtils.assertNotNull(obj);
	}

	protected Object invokeMethod(String method, Class<?>[] parameterTypes, Object[] args) throws Exception {
		try {
			Method m = this.obj.getClass().getMethod(method, parameterTypes);
			return m.invoke(this.obj, args);
		} catch (InvocationTargetException ex) {
			throw ExceptionUtils.tryUnwrap(ex);
		}
	}

	public ObjectInvoker invoke(String method) throws Exception {
		invokeMethod(method, ReflectionUtils.EMPTY_CLASSES, ReflectionUtils.EMPTY_OBJECTS);
		return this;
	}

	public ObjectInvoker invoke(String method, Class<?> parameterType, Object arg) throws Exception {
		invokeMethod(method, new Class<?>[] { parameterType }, new Object[] { arg });
		return this;
	}

	public ObjectInvoker invoke(String method, Class<?>[] parameterTypes, Object[] args) throws Exception {
		invokeMethod(method, parameterTypes, args);
		return this;
	}

	public Object invokeResult(String method) throws Exception {
		return invokeMethod(method, ReflectionUtils.EMPTY_CLASSES, ReflectionUtils.EMPTY_OBJECTS);
	}

	public Object invokeResult(String method, Class<?> parameterType, Object arg) throws Exception {
		return invokeMethod(method, new Class<?>[] { parameterType }, new Object[] { arg });
	}

	public Object invokeResult(String method, Class<?>[] parameterTypes, Object[] args) throws Exception {
		return invokeMethod(method, parameterTypes, args);
	}
}
