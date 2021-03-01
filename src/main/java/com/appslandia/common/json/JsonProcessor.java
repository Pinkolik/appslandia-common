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

package com.appslandia.common.json;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.appslandia.common.base.InitializeException;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.base.MemoryStream;
import com.appslandia.common.base.StringWriter;
import com.appslandia.common.utils.ObjectUtils;
import com.appslandia.common.utils.ReflectionUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class JsonProcessor extends InitializeObject {

	public abstract void write(Writer out, Object obj) throws JsonException;

	public abstract <T> T read(Reader reader, Class<T> resultClass) throws JsonException;

	public abstract <T> T read(Reader reader, Type type) throws JsonException;

	public <V> Map<String, V> readAsMap(Reader reader) throws JsonException {
		return ObjectUtils.cast(read(reader, HashMap.class));
	}

	public <V> Map<String, V> readAsLinkedMap(Reader reader) throws JsonException {
		return ObjectUtils.cast(read(reader, LinkedHashMap.class));
	}

	public String toString(Object obj) throws JsonException {
		StringWriter out = new StringWriter(128);
		write(out, obj);
		return out.toString();
	}

	public byte[] toByteArray(Object obj) throws JsonException {
		MemoryStream content = new MemoryStream(512);
		try (OutputStreamWriter out = new OutputStreamWriter(content, StandardCharsets.UTF_8)) {

			write(out, obj);
		} catch (IOException ex) {
			throw new JsonException(ex);
		}
		return content.toByteArray();
	}

	private static JsonProcessor __default;
	private static final Object MUTEX = new Object();

	public static JsonProcessor getDefault() {
		JsonProcessor obj = __default;
		if (obj == null) {
			synchronized (MUTEX) {
				if ((obj = __default) == null) {
					__default = obj = initJsonProcessor();
				}
			}
		}
		return obj;
	}

	public static void setDefault(JsonProcessor impl) {
		if (__default == null) {
			synchronized (MUTEX) {
				if (__default == null) {
					__default = impl;
					return;
				}
			}
		}
		throw new IllegalStateException("JsonProcessor.__default must be null.");
	}

	private static Supplier<JsonProcessor> __provider;

	public static void setProvider(Supplier<JsonProcessor> impl) {
		if (__default == null) {
			synchronized (MUTEX) {
				if (__default == null) {
					__provider = impl;
					return;
				}
			}
		}
		throw new IllegalStateException("JsonProcessor.__default must be null.");
	}

	private static JsonProcessor initJsonProcessor() {
		if (__provider != null) {
			return __provider.get();
		}
		try {
			Class<? extends JsonProcessor> implClass = ReflectionUtils.loadClass("com.appslandia.common.json.GsonProcessor", null);
			return ReflectionUtils.newInstance(implClass);
		} catch (Exception ex) {
			throw new InitializeException(ex);
		}
	}
}
