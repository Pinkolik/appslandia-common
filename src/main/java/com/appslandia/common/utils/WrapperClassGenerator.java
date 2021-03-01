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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.TypeVariable;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

import com.appslandia.common.base.TextBuilder;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class WrapperClassGenerator {

	public static void main(String[] args) {
		MethodComparator comparator = new MethodComparator(new String[] { "execute", "set" }, null);
		System.out.println(generateWrapper(PreparedStatement.class, "this.stat", comparator));
	}

	public static String generateWrapper(Class<?> clazz, String wrappedField, MethodComparator comparator) {
		return generateWrapper(clazz, wrappedField, comparator, null, DEFAULT_SKIPPED);
	}

	public static String generateWrapper(Class<?> clazz, String wrappedField, MethodComparator comparator, Unsupported unsupported) {
		return generateWrapper(clazz, wrappedField, comparator, unsupported, DEFAULT_SKIPPED);
	}

	public static String generateWrapper(Class<?> clazz, String wrappedField, MethodComparator comparator, Unsupported unsupported, Skipped skip) {
		TextBuilder sb = new TextBuilder();
		try {
			generateWrapper(clazz, wrappedField, sb, comparator, unsupported, skip);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return sb.toString();
	}

	private static void generateWrapper(Class<?> clazz, String wrappedField, TextBuilder sb, MethodComparator comparator, Unsupported unsupported,
			Skipped skipped) throws Exception {
		sb.appendln(2);
		sb.append("// " + clazz.getName());

		// declaredMethods
		Method[] declaredMethods = clazz.getDeclaredMethods();
		Arrays.sort(declaredMethods, comparator);

		for (Method method : declaredMethods) {
			if (skipped.apply(method)) {
				continue;
			}
			String returnType = method.getGenericReturnType().getTypeName();
			sb.appendln(2);

			sb.append("@Override");
			sb.appendln();
			sb.append("public ");

			// getTypeParameters
			TypeVariable<?>[] typeVars = method.getTypeParameters();
			if (typeVars.length > 0) {
				boolean first = true;
				sb.append('<');
				for (TypeVariable<?> tv : typeVars) {
					if (first == false) {
						sb.append(',');
					}
					sb.append(tv.toString());
					first = false;
				}
				sb.append("> ");
			}
			sb.append(returnType).append(" ").append(method.getName());

			// Arguments
			sb.append("(");
			Parameter[] params = method.getParameters();
			for (int i = 0; i < params.length; i++) {
				if (i > 0)
					sb.append(", ");
				Parameter parameter = params[i];
				sb.append(parameter.getParameterizedType().getTypeName()).append(" ").append(parameter.getName());

			}
			sb.append(")");

			// Exceptions
			Class<?>[] exceptionTypes = method.getExceptionTypes();
			if (exceptionTypes.length > 0)
				sb.append(" throws ");
			for (int i = 0; i < exceptionTypes.length; i++) {
				sb.append(exceptionTypes[i].getCanonicalName());
				if (i < exceptionTypes.length - 1)
					sb.append(", ");
			}
			sb.append(" {");
			sb.appendln();

			// Unsupported?
			if (unsupported != null && unsupported.apply(method)) {
				sb.appendtab().append("throw new UnsupportedOperationException();");

			} else {
				// Return
				if (method.getReturnType() == void.class) {
					sb.appendtab().append(wrappedField).append(".").append(method.getName());
				} else {
					sb.appendtab().append("return ").append(wrappedField).append(".").append(method.getName());
				}

				// Calling arguments
				sb.append("(");
				for (int i = 0; i < params.length; i++) {
					if (i > 0)
						sb.append(", ");
					Parameter parameter = params[i];
					sb.append(parameter.getName());
				}
				sb.append(");");
			}

			sb.appendln();
			sb.append("}");
		}

		// Super
		if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class) {
			generateWrapper(clazz.getSuperclass(), wrappedField, sb, comparator, unsupported, skipped);
		}

		// Interfaces
		for (Class<?> interfaceClazz : clazz.getInterfaces()) {
			generateWrapper(interfaceClazz, wrappedField, sb, comparator, unsupported, skipped);
		}
	}

	public interface Unsupported {
		boolean apply(Method method);
	}

	public interface Skipped {
		boolean apply(Method method);
	}

	private static final Skipped DEFAULT_SKIPPED = new Skipped() {

		@Override
		public boolean apply(Method method) {
			if (Modifier.isStatic(method.getModifiers())) {
				return true;
			}
			if (Modifier.isPrivate(method.getModifiers())) {
				return true;
			}
			if (Modifier.isFinal(method.getModifiers())) {
				return true;
			}
			return false;
		}
	};

	public static class MethodComparator implements Comparator<Method> {

		final String[] priorities;
		final Map<String, Integer> namePriorityMap;

		public MethodComparator(String[] priorities) {
			this(priorities, null);
		}

		public MethodComparator(String[] priorities, Map<String, Integer> namePriorityMap) {
			this.priorities = priorities;
			this.namePriorityMap = namePriorityMap;
		}

		@Override
		public int compare(Method m1, Method m2) {

			// priority
			for (String priority : this.priorities) {
				if (m1.getName().startsWith(priority)) {
					if (m2.getName().startsWith(priority) == false) {
						return -1;
					}
				}
				if (m1.getName().startsWith(priority) == false) {
					if (m2.getName().startsWith(priority)) {
						return 1;
					}
				}
			}

			// Order
			Integer order1 = findMethodOrder(m1.getName(), this.namePriorityMap);
			Integer order2 = findMethodOrder(m2.getName(), this.namePriorityMap);

			if ((order1 != null) && (order2 != null)) {
				return order1.compareTo(order2);
			}
			if (order1 != null) {
				return -1;
			}
			if (order2 != null) {
				return 1;
			}

			// getName?
			int compare = m1.getName().compareTo(m2.getName());
			if (compare != 0) {
				return compare;
			}

			// getParameterCount
			if (m1.getParameterCount() < m2.getParameterCount()) {
				return -1;
			}
			if (m1.getParameterCount() > m2.getParameterCount()) {
				return 1;
			}
			return m1.toString().compareTo(m2.toString());
		}
	}

	private static Integer findMethodOrder(String methodName, Map<String, Integer> namePriorityMap) {
		if (namePriorityMap == null) {
			return null;
		}
		for (Entry<String, Integer> entry : namePriorityMap.entrySet()) {
			if (methodName.toLowerCase().contains(entry.getKey().toLowerCase())) {
				return entry.getValue();
			}
		}
		return null;
	}
}
