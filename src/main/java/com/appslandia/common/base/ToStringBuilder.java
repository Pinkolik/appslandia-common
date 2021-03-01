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

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.nio.Buffer;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.security.ProtectionDomain;
import java.time.temporal.Temporal;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.logging.Logger;

import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.ExceptionUtils;
import com.appslandia.common.utils.ObjectUtils;
import com.appslandia.common.utils.ReflectionUtils;
import com.appslandia.common.utils.TypeUtils;
import com.appslandia.common.utils.ValueUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ToStringBuilder {

	@Target({ ElementType.TYPE, ElementType.FIELD })
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface HashInfo {
	}

	@Target({ ElementType.TYPE, ElementType.FIELD })
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface ToString {
	}

	@Target({ ElementType.FIELD })
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface Exclude {
	}

	public static class FieldDecision {

		public boolean toHashInfo(Object value, Field field) {
			boolean b = checkAnnotation(value, field, HashInfo.class);
			return b ? b : useHashInfo(value, field);
		}

		public boolean invokeToString(Object value, Field field) {
			boolean b = checkAnnotation(value, field, ToString.class);
			return b ? b : useToString(value, field);
		}

		public boolean exclude(Field field) {
			return false;
		}

		protected boolean useHashInfo(Object value, Field field) {
			return false;
		}

		protected boolean useToString(Object value, Field field) {
			return false;
		}

		protected boolean checkAnnotation(Object value, Field field, Class<? extends Annotation> annotationType) {
			if (field != null) {
				if (field.getAnnotation(annotationType) != null) {
					return true;
				}
			}
			// Not java.*?
			Package pkg = value.getClass().getPackage();
			if ((pkg != null) && !pkg.getName().startsWith("java.")) {

				if (ReflectionUtils.findAnnotation(value.getClass(), annotationType) != null) {
					return true;
				}
			}
			return false;
		}
	}

	public static final FieldDecision DEFAULT_DECISION = new FieldDecision();

	private int level;
	private FieldDecision fieldDecision = DEFAULT_DECISION;

	private int identTabs;

	public ToStringBuilder() {
		this(2);
	}

	public ToStringBuilder(int level) {
		setLevel(level);
	}

	public ToStringBuilder fieldDecision(FieldDecision fieldDecision) {
		this.fieldDecision = fieldDecision;
		return this;
	}

	public String toString(Object obj) {
		TextBuilder builder = new TextBuilder();
		appendtab(builder, this.identTabs, false);
		if (obj == null) {
			return builder.append("null").toString();
		}
		this.toStringObject(obj, 1, builder);
		return builder.toString();
	}

	public String toStringFields(Object obj) {
		TextBuilder builder = new TextBuilder();
		appendtab(builder, this.identTabs, false);
		if (obj == null) {
			return builder.append("null").toString();
		}
		this.toStringFields(obj, 1, builder);
		return builder.toString();
	}

	private void toStringObject(Object obj, int level, TextBuilder builder) {
		if (obj == null) {
			builder.append("null");
			return;
		}

		// Basic Types
		if (isBasicType(obj.getClass())) {

			if (obj.getClass() == Character.class) {
				builder.append("'").append(obj).append("'");
				return;
			}
			if (obj.getClass() == String.class) {
				builder.append("\"").append(obj).append("\"");
				return;
			}

			if (obj.getClass() == Long.class) {
				builder.append(obj).append("L");
				return;
			}
			if (obj.getClass() == Float.class) {
				builder.append(obj).append("f");
				return;
			}
			if (obj.getClass() == Double.class) {
				builder.append(obj).append("d");
				return;
			}
			if (obj.getClass() == BigDecimal.class) {
				builder.append(obj).append("m");
				return;
			}

			// Others
			builder.append(obj);
			return;
		}

		if (obj instanceof Iterable) {
			toStringIterator(obj, new IteratorIterator(((Iterable<?>) obj).iterator()), level, builder);
			return;
		}
		if (obj instanceof Iterator) {
			toStringIterator(obj, new IteratorIterator((Iterator<?>) obj), level, builder);
			return;
		}
		if (obj instanceof Enumeration) {
			toStringIterator(obj, new EnumerationIterator((Enumeration<?>) obj), level, builder);
			return;
		}
		if (obj.getClass().isArray()) {
			toStringIterator(obj, new ArrayIterator(obj), level, builder);
			return;
		}
		if (obj instanceof Buffer) {
			toStringIterator(obj, new ArrayIterator(((Buffer) obj).array()), level, builder);
			return;
		}
		if (obj instanceof Map) {
			toStringMap((Map<?, ?>) obj, level, builder);
			return;
		}
		if (obj instanceof Throwable) {
			builder.append(ExceptionUtils.toStackTrace((Throwable) obj));
			return;
		}

		if (otherToString(obj.getClass())) {
			builder.append(obj);
			return;
		}

		// Fields
		toStringFields(obj, level, builder);
	}

	private void toStringFields(Object obj, int level, TextBuilder builder) {
		builder.append(ObjectUtils.toHashInfo(obj));
		if (level > this.level) {
			return;
		}
		builder.append("[");
		boolean isFirst = true;

		Class<?> clazz = obj.getClass();
		while (clazz != null) {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				if (field.getName().equals("serialVersionUID")) {
					continue;
				}
				if (field.getAnnotation(Exclude.class) != null) {
					continue;
				}
				if (this.fieldDecision.exclude(field)) {
					continue;
				}

				if (!isFirst) {
					builder.append(",");
				} else {
					isFirst = false;
				}

				appendln(builder, false);
				appendtab(builder, level + this.identTabs, false);
				builder.append(field.getName()).append(": ");

				try {
					field.setAccessible(true);
					Object fieldVal = field.get(obj);

					if (fieldVal == null) {
						builder.append("null");
					} else {
						if (this.fieldDecision.toHashInfo(fieldVal, field)) {
							builder.append(ObjectUtils.toHashInfo(fieldVal));

						} else if (this.fieldDecision.invokeToString(fieldVal, field)) {
							builder.append(fieldVal);
						} else {
							this.toStringObject(fieldVal, level + 1, builder);
						}
					}

				} catch (Exception ex) {
					builder.append("error=").append(ExceptionUtils.buildMessage(ex));
				}
			}
			clazz = clazz.getSuperclass();
		}

		if (isFirst) {
			builder.append(" no fields ]");
		} else {
			appendln(builder, false);
			appendtab(builder, level - 1 + this.identTabs, false).append("]");
		}
	}

	private void toStringIterator(Object obj, ElementIterator iterator, int level, TextBuilder builder) {
		builder.append(ObjectUtils.toHashInfo(obj));
		if (level > this.level) {
			return;
		}
		builder.append("[");
		boolean isFirst = true;

		while (iterator.hasNext()) {
			Object element = iterator.next();

			if (element == MORE_ELEMENTS) {
				builder.append(", AND MORE ...");
				break;
			}

			if (!isFirst) {
				builder.append(",");
			} else {
				isFirst = false;
			}
			appendln(builder, iterator.isCompact());
			appendtab(builder, level + this.identTabs, iterator.isCompact());

			if (element == null) {
				builder.append("null");
			} else {
				if (this.fieldDecision.toHashInfo(element, null)) {
					builder.append(ObjectUtils.toHashInfo(element));

				} else if (this.fieldDecision.invokeToString(element, null)) {
					builder.append(element);
				} else {
					this.toStringObject(element, level + 1, builder);
				}
			}
		}
		if (isFirst) {
			builder.append(" no elements ]");
		} else {
			appendln(builder, iterator.isCompact());
			appendtab(builder, level - 1 + this.identTabs, iterator.isCompact()).append("] (").append(iterator.getComputedLen()).append(")");
		}
	}

	private void toStringMap(Map<?, ?> map, int level, TextBuilder builder) {
		builder.append(ObjectUtils.toHashInfo(map));
		if (level > this.level) {
			return;
		}
		builder.append("[");
		boolean isFirst = true;

		for (Object key : map.keySet()) {
			if (!isFirst) {
				builder.append(",");
			} else {
				isFirst = false;
			}
			appendln(builder, false);
			appendtab(builder, level + this.identTabs, false);

			builder.append(key).append(": ");
			Object entryVal = map.get(key);
			if (entryVal == null) {
				builder.append("null");
			} else {
				if (this.fieldDecision.toHashInfo(entryVal, null)) {
					builder.append(ObjectUtils.toHashInfo(entryVal));

				} else if (this.fieldDecision.invokeToString(entryVal, null)) {
					builder.append(entryVal);
				} else {
					this.toStringObject(entryVal, level + 1, builder);
				}
			}
		}

		if (isFirst) {
			builder.append(" no entries ]");
		} else {
			appendln(builder, false);
			appendtab(builder, level - 1 + this.identTabs, false).append("] (").append(map.size()).append(")");
		}
	}

	private void toStringAttributes(Object obj, Method getAttributeMethod, Set<String> attributes, int level, TextBuilder builder) {
		builder.append("[");
		boolean isFirst = true;

		for (String attribute : attributes) {
			if (!isFirst) {
				builder.append(",");
			} else {
				isFirst = false;
			}
			appendln(builder, false);
			appendtab(builder, level + this.identTabs, false);
			builder.append(attribute).append(": ");

			try {
				Object element = getAttributeMethod.invoke(obj, attribute);
				if (element == null) {
					builder.append("null");
				} else {
					if ("javax.servlet.error.exception".equals(attribute)) {
						builder.append(ObjectUtils.toHashInfo(element));

					} else if (this.fieldDecision.toHashInfo(element, null)) {
						builder.append(ObjectUtils.toHashInfo(element));

					} else if (this.fieldDecision.invokeToString(element, null)) {
						builder.append(element);
					} else {
						this.toStringObject(element, level + 1, builder);
					}
				}

			} catch (Exception ex) {
				builder.append("error=").append(ExceptionUtils.buildMessage(ex));
			}
		}

		if (isFirst) {
			builder.append(" no elements ]");
		} else {
			appendln(builder, false);
			appendtab(builder, level - 1 + this.identTabs, false).append("]");
		}
	}

	public String toStringAttributes(Object obj) {
		TextBuilder builder = new TextBuilder();
		appendtab(builder, this.identTabs, false);
		if (obj == null) {
			builder.append("null");
			return builder.toString();
		}
		try {
			Set<String> attributes = getAttributeNames(obj, "getAttributeNames");
			Method method = ReflectionUtils.findMethod(obj.getClass(), "getAttribute");
			AssertUtils.assertNotNull(method);

			builder.append(ObjectUtils.toHashInfo(obj)).append("-attributes");
			toStringAttributes(obj, method, attributes, 1, builder);
		} catch (Exception ex) {
			builder.append("error=").append(ExceptionUtils.buildMessage(ex));
		}
		return builder.toString();
	}

	public String toStringHeaders(Object obj) {
		TextBuilder builder = new TextBuilder();
		appendtab(builder, this.identTabs, false);
		if (obj == null) {
			builder.append("null");
			return builder.toString();
		}
		try {
			Set<String> attributes = getAttributeNames(obj, "getHeaderNames");
			Method method = ReflectionUtils.findMethod(obj.getClass(), "getHeaders");
			AssertUtils.assertNotNull(method);

			builder.append(ObjectUtils.toHashInfo(obj)).append("-headers");
			toStringAttributes(obj, method, attributes, 1, builder);
		} catch (Exception ex) {
			builder.append("error=").append(ExceptionUtils.buildMessage(ex));
		}
		return builder.toString();
	}

	private static Set<String> getAttributeNames(Object obj, String methodName) throws Exception {
		Method method = ReflectionUtils.findMethod(obj.getClass(), methodName);
		AssertUtils.assertNotNull(method);

		Object attrs = method.invoke(obj);
		Set<String> names = new TreeSet<>();
		if (attrs instanceof Enumeration) {

			Enumeration<String> enm = ObjectUtils.cast(attrs);
			while (enm.hasMoreElements()) {
				names.add(enm.nextElement());
			}
		} else {
			Collection<String> attrCol = ObjectUtils.cast(attrs);
			names.addAll(attrCol);
		}
		return names;
	}

	public ToStringBuilder setLevel(int level) {
		this.level = ValueUtils.valueOrMin(level, 1);
		return this;
	}

	public ToStringBuilder setIdentTabs(int identTabs) {
		this.identTabs = ValueUtils.valueOrMin(identTabs, 0);
		return this;
	}

	private TextBuilder appendln(TextBuilder builder, boolean isCompact) {
		if (!isCompact) {
			builder.appendln();
		}
		return builder;
	}

	private TextBuilder appendtab(TextBuilder builder, int n, boolean isCompact) {
		if (isCompact) {
			return builder.appendsp();
		}
		return builder.appendsp(2 * n);
	}

	protected boolean otherToString(Class<?> type) {
		if (BitSet.class.isAssignableFrom(type)) {
			return true;
		}
		if ((type == Path.class) || (type == URL.class) || (type == URI.class)) {
			return true;
		}
		if (AnnotatedElement.class.isAssignableFrom(type) || Type.class.isAssignableFrom(type) || ClassLoader.class.isAssignableFrom(type)) {
			return true;
		}
		if (Annotation.class.isAssignableFrom(type) || (type == ProtectionDomain.class)) {
			return true;
		}
		if ((type == File.class) || InputStream.class.isAssignableFrom(type) || OutputStream.class.isAssignableFrom(type)) {
			return true;
		}
		if (Reader.class.isAssignableFrom(type) || Writer.class.isAssignableFrom(type)) {
			return true;
		}
		if (ResourceBundle.class.isAssignableFrom(type) || Logger.class.isAssignableFrom(type)) {
			return true;
		}
		return false;
	}

	static boolean isBasicType(Class<?> type) {
		if (TypeUtils.isPrimitiveOrWrapper(type)) {
			return true;
		}
		if (CharSequence.class.isAssignableFrom(type)) {
			return true;
		}
		if (Enum.class.isAssignableFrom(type) || (type == BigDecimal.class)) {
			return true;
		}
		if (Date.class.isAssignableFrom(type) || Calendar.class.isAssignableFrom(type) || TimeZone.class.isAssignableFrom(type) || (type == Locale.class)
				|| Charset.class.isAssignableFrom(type)) {
			return true;
		}
		if (Temporal.class.isAssignableFrom(type)) {
			return true;
		}
		return false;
	}

	interface ElementIterator {

		boolean hasNext();

		Object next();

		int getIndex();

		int getComputedLen();

		boolean isCompact();
	}

	static final Object MORE_ELEMENTS = new Object() {
	};

	static class ArrayIterator implements ElementIterator {

		final Object obj;
		final int len;
		final Class<?> elementType;
		int index = 0;

		public ArrayIterator(Object obj) {
			this.obj = obj;
			this.len = Array.getLength(obj);
			this.elementType = obj.getClass().getComponentType();
		}

		@Override
		public boolean hasNext() {
			return this.index < this.len;
		}

		@Override
		public Object next() {
			// byte[]
			if (this.elementType == byte.class) {
				if (this.index > 128) {
					return MORE_ELEMENTS;
				}
			}
			return Array.get(this.obj, this.index++);
		}

		@Override
		public int getIndex() {
			return this.index;
		}

		@Override
		public int getComputedLen() {
			return this.len;
		}

		@Override
		public boolean isCompact() {
			return isBasicType(this.elementType);
		}
	}

	static class IteratorIterator implements ElementIterator {
		final Iterator<?> obj;
		int index = 0;

		public IteratorIterator(Iterator<?> obj) {
			this.obj = obj;
		}

		@Override
		public boolean hasNext() {
			return this.obj.hasNext();
		}

		@Override
		public Object next() {
			this.index++;
			return this.obj.next();
		}

		@Override
		public int getIndex() {
			return this.index;
		}

		@Override
		public int getComputedLen() {
			return this.index;
		}

		@Override
		public boolean isCompact() {
			return false;
		}
	}

	static class EnumerationIterator implements ElementIterator {
		final Enumeration<?> obj;
		int index = 0;

		public EnumerationIterator(Enumeration<?> obj) {
			this.obj = obj;
		}

		@Override
		public boolean hasNext() {
			return this.obj.hasMoreElements();
		}

		@Override
		public Object next() {
			this.index++;
			return this.obj.nextElement();
		}

		@Override
		public int getIndex() {
			return this.index;
		}

		@Override
		public int getComputedLen() {
			return this.index;
		}

		@Override
		public boolean isCompact() {
			return false;
		}
	}
}
