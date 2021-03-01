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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.appslandia.common.utils.ExceptionUtils;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class PropertyConfig extends ConfigMap implements Serializable {
	private static final long serialVersionUID = 1L;

	public PropertyConfig() {
		super(new LinkedHashMap<String, String>());
	}

	public PropertyConfig(Map<String, String> newMap) {
		super(newMap);
	}

	public PropertyConfig load(InputStream is) throws IOException {
		Properties props = new LinkedProperties();
		props.load(is);
		fromProperties(props);
		return this;
	}

	public PropertyConfig load(Reader r) throws IOException {
		Properties props = new LinkedProperties();
		props.load(r);
		fromProperties(props);
		return this;
	}

	public PropertyConfig load(File file) throws IOException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
			return load(br);
		} finally {
			if (br != null) {
				br.close();
			}
		}
	}

	public PropertyConfig load(String file) throws IOException {
		return load(new File(file));
	}

	protected void fromProperties(Properties props) {
		for (Object prop : props.keySet()) {
			String key = (String) prop;
			String value = (String) props.get(key);

			if (!key.isEmpty()) {
				this.map.put(key, !value.isEmpty() ? value : null);
			}
		}
	}

	public void store(File file, String comments) throws IOException {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
			store(bw, comments);
		} finally {
			if (bw != null) {
				bw.close();
			}
		}
	}

	public void store(String file, String comments) throws IOException {
		store(new File(file), comments);
	}

	public void store(OutputStream os, String comments) throws IOException {
		toProperties().store(os, comments);
	}

	public void store(Writer w, String comments) throws IOException {
		toProperties().store(w, comments);
	}

	protected Properties toProperties() {
		Properties props = new LinkedProperties();
		for (Map.Entry<String, String> prop : this.map.entrySet()) {
			props.put(prop.getKey(), prop.getValue() != null ? prop.getValue() : StringUtils.EMPTY_STRING);
		}
		return props;
	}

	public PropertyConfig set(String key, String value) {
		super.set(key, value);
		return this;
	}

	public PropertyConfig set(String key, boolean value) {
		super.set(key, value);
		return this;
	}

	public PropertyConfig set(String key, int value) {
		super.set(key, value);
		return this;
	}

	public PropertyConfig set(String key, long value) {
		super.set(key, value);
		return this;
	}

	public PropertyConfig set(String key, double value) {
		super.set(key, value);
		return this;
	}

	@Override
	public String toString() {
		try {
			StringWriter out = new StringWriter();
			store(out, getClass().getName());
			return out.toString();
		} catch (IOException ex) {
			return ExceptionUtils.toStackTrace(ex);
		}
	}

	private static class LinkedProperties extends Properties {
		private static final long serialVersionUID = 1;

		final Set<Object> keys = new LinkedHashSet<>();

		@Override
		public Set<Object> keySet() {
			return this.keys;
		}

		@Override
		public Enumeration<Object> keys() {
			return Collections.enumeration(this.keys);
		}

		@Override
		public Object put(Object key, Object value) {
			this.keys.add(key);
			return super.put(key, value);
		}
	}
}
