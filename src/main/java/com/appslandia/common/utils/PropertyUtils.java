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
import java.util.Locale;

import com.appslandia.common.base.ConfigMap;
import com.appslandia.common.base.FormatProvider;
import com.appslandia.common.base.FormatProviderImpl;
import com.appslandia.common.formatters.Formatter;
import com.appslandia.common.formatters.FormatterException;
import com.appslandia.common.formatters.FormatterProvider;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class PropertyUtils {

	private interface PropertyStrategy {

		Method find(Class<?> clazz, String propertyName);
	}

	public static final PropertyStrategy METHOD_PROPERTY_STRATEGY = new PropertyStrategy() {

		@Override
		public Method find(Class<?> clazz, final String propertyName) {
			return ReflectionUtils.traverse(clazz, new ReflectionUtils.MethodHandler() {

				@Override
				public boolean matches(Method m) {
					if (!Modifier.isPublic(m.getModifiers()) || Modifier.isStatic(m.getModifiers())) {
						return false;
					}
					if (m.getParameterCount() != 1) {
						return false;
					}
					return m.getName().equals(propertyName);
				}

				@Override
				public boolean handle(Method m) throws ReflectionException {
					return false;
				}
			});
		}
	};

	public static final PropertyStrategy BEAN_PROPERTY_STRATEGY = new PropertyStrategy() {

		@Override
		public Method find(Class<?> clazz, final String propertyName) {
			return ReflectionUtils.traverse(clazz, new ReflectionUtils.MethodHandler() {

				@Override
				public boolean matches(Method m) {
					if (!Modifier.isPublic(m.getModifiers()) || Modifier.isStatic(m.getModifiers())) {
						return false;
					}
					if (m.getParameterCount() != 1) {
						return false;
					}
					return m.getName().equals("set" + StringUtils.firstUpperCase(propertyName, Locale.ENGLISH));
				}

				@Override
				public boolean handle(Method m) throws ReflectionException {
					return false;
				}
			});
		}
	};

	public static void initialize(Object obj, ConfigMap config, PropertyStrategy propertyStrategy) throws FormatterException, ReflectionException {
		initialize(obj, config, new FormatProviderImpl(), propertyStrategy);
	}

	public static void initialize(Object obj, ConfigMap config, FormatProvider formatProvider, PropertyStrategy propertyStrategy)
			throws FormatterException, ReflectionException {
		for (String property : config.keySet()) {
			Method m = propertyStrategy.find(obj.getClass(), property);
			if (m == null) {
				continue;
			}
			Class<?> parameterType = m.getParameterTypes()[0];
			Formatter formatter = FormatterProvider.getDefault().findFormatter((String) null, parameterType);
			if (formatter != null) {

				Object value = formatter.parse(config.getString(property), formatProvider);
				ReflectionUtils.invoke(m, obj, value);
			}
		}
	}
}
