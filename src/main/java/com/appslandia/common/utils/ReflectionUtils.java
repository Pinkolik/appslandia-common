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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Locale;

import com.appslandia.common.base.UncheckedException;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ReflectionUtils {

	public static final Object[] EMPTY_OBJECTS = {};
	public static final Class<?>[] EMPTY_CLASSES = {};
	public static final Annotation[] EMPTY_ANNOTATIONS = {};

	public interface FieldHandler {

		boolean matches(Field field);

		boolean handle(Field field) throws ReflectionException;
	}

	public interface MethodHandler {

		boolean matches(Method m);

		boolean handle(Method m) throws ReflectionException;
	}

	public static Field findField(Class<?> clazz, final String property) throws ReflectionException {
		return traverse(clazz, new FieldHandler() {

			@Override
			public boolean matches(Field f) {
				if (f.getName().equals(property)) {
					return true;
				}
				if (f.getType() == boolean.class) {
					if (f.getName().equals("is" + StringUtils.firstUpperCase(property, Locale.ENGLISH))) {
						return true;
					}
				}
				return false;
			}

			@Override
			public boolean handle(Field field) throws ReflectionException {
				return false;
			}
		});
	}

	public static Method findMethod(Class<?> clazz, final String methodName) throws ReflectionException {
		return traverse(clazz, new MethodHandler() {

			@Override
			public boolean matches(Method m) {
				return m.getName().equals(methodName);
			}

			@Override
			public boolean handle(Method m) throws ReflectionException {
				return false;
			}
		});
	}

	public static Field traverse(Class<?> clazz, FieldHandler handler) throws ReflectionException {
		Field matched = null;
		while (clazz != null) {
			for (Field field : clazz.getDeclaredFields()) {
				if (handler.matches(field)) {

					matched = field;
					if (!handler.handle(field)) {
						return matched;
					}
				}
			}
			clazz = clazz.getSuperclass();
		}
		return matched;
	}

	public static Method traverse(Class<?> clazz, MethodHandler handler) throws ReflectionException {
		Method matched = null;
		while (clazz != null) {
			Method[] methods = (clazz.isInterface() ? clazz.getMethods() : clazz.getDeclaredMethods());

			for (Method m : methods) {
				if (handler.matches(m)) {

					matched = m;
					if (!handler.handle(m)) {
						return matched;
					}
				}
			}
			clazz = clazz.getSuperclass();
		}
		return matched;
	}

	public static <T extends Annotation> T findAnnotation(Class<?> clazz, Class<T> annotationClass) {
		while (clazz != null) {
			T t = clazz.getAnnotation(annotationClass);
			if (t != null) {
				return t;
			}
			for (Class<?> interfaceClass : clazz.getInterfaces()) {
				t = interfaceClass.getAnnotation(annotationClass);
				if (t != null) {
					return t;
				}
			}
			clazz = clazz.getSuperclass();
		}
		return null;
	}

	public static Object invoke(Method m, Object obj, Object... args) throws ReflectionException {
		try {
			return m.invoke(obj, args);
		} catch (ReflectiveOperationException ex) {
			throw new ReflectionException(ex);
		}
	}

	public static void set(Field m, Object obj, Object value) throws ReflectionException {
		try {
			m.set(obj, value);
		} catch (ReflectiveOperationException ex) {
			throw new ReflectionException(ex);
		}
	}

	public static Object get(Field m, Object obj) throws ReflectionException {
		try {
			return m.get(obj);
		} catch (ReflectiveOperationException ex) {
			throw new ReflectionException(ex);
		}
	}

	public static Class<?> getListArgType(Type genericType) {
		if (!(genericType instanceof ParameterizedType)) {
			return null;
		}
		Type[] types = ((ParameterizedType) genericType).getActualTypeArguments();
		if (types.length != 1) {
			return null;
		}
		Type type = types[0];
		if (!(type instanceof Class)) {
			return null;
		}
		return (Class<?>) type;
	}

	public static Class<?>[] getMapArgTypes(Type genericType) {
		if (!(genericType instanceof ParameterizedType)) {
			return null;
		}
		Type[] types = ((ParameterizedType) genericType).getActualTypeArguments();
		if (types.length != 2) {
			return null;
		}
		Type kt = types[0];
		if (!(kt instanceof Class)) {
			return null;
		}
		Type vt = types[1];
		if (!(vt instanceof Class)) {
			return null;
		}
		return new Class<?>[] { (Class<?>) kt, (Class<?>) vt };
	}

	public static <T> T newInstance(Class<T> clazz) throws ReflectionException {
		try {
			return clazz.getDeclaredConstructor(EMPTY_CLASSES).newInstance(EMPTY_OBJECTS);

		} catch (ReflectiveOperationException ex) {
			throw new ReflectionException(ex);
		} catch (SecurityException ex) {
			throw new UncheckedException(ex);
		}
	}

	public static <T> Class<? extends T> loadClass(String className, ClassLoader loader) throws ReflectionException {
		ClassLoader cl = (loader != null) ? loader : getDefaultClassLoader();
		try {
			return ObjectUtils.cast(cl.loadClass(className));
		} catch (ReflectiveOperationException ex) {
			throw new ReflectionException(ex);
		}
	}

	public static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (Exception ex) {
		}
		if (cl == null) {
			cl = ReflectionUtils.class.getClassLoader();
			if (cl == null) {
				try {
					cl = ClassLoader.getSystemClassLoader();
				} catch (Exception ex) {
				}
			}
		}
		return cl;
	}

	public static boolean isImplementOf(Method implMth, Method interfaceMth) {
		return interfaceMth.getDeclaringClass().isAssignableFrom(implMth.getDeclaringClass()) && interfaceMth.getName().equals(implMth.getName())
				&& Arrays.equals(interfaceMth.getParameterTypes(), implMth.getParameterTypes());
	}

	public static Method getDeclaredMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
		try {
			return clazz.getDeclaredMethod(name, parameterTypes);

		} catch (ReflectiveOperationException ex) {
			throw new ReflectionException(ex);
		} catch (SecurityException ex) {
			throw new UncheckedException(ex);
		}
	}

	public static boolean isPublicConst(int modifier) {
		return Modifier.isPublic(modifier) && Modifier.isStatic(modifier) && Modifier.isFinal(modifier);
	}
}
