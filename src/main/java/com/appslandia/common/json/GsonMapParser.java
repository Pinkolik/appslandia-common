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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.appslandia.common.utils.AssertUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class GsonMapParser {

	public Map<String, Object> parseMap(JsonObject jsonObject, Supplier<Map<String, Object>> newMap, boolean makeReadonly) {
		AssertUtils.assertTrue(jsonObject.isJsonObject());

		Map<String, Object> map = newMap.get();
		for (String key : jsonObject.keySet()) {

			map.put(key, parseValue(jsonObject.get(key), makeReadonly));
		}
		return makeReadonly ? Collections.unmodifiableMap(map) : map;
	}

	public Object parseValue(JsonElement element, boolean makeReadonly) {
		// NULL
		if (element.isJsonNull()) {
			return null;
		}

		// Primitive
		if (element.isJsonPrimitive()) {
			JsonPrimitive jsonPrim = element.getAsJsonPrimitive();

			// String
			if (jsonPrim.isString()) {
				return element.getAsString();
			}

			// Boolean
			if (jsonPrim.isBoolean()) {
				return element.getAsBoolean();
			}

			// Double/Long
			if (jsonPrim.isNumber()) {
				String str = element.getAsString();

				if (str.contains(".")) {
					return element.getAsDouble();
				}
				return element.getAsLong();
			}
			throw new AssertionError();
		}

		// Array
		if (element.isJsonArray()) {
			JsonArray jsonArray = element.getAsJsonArray();
			List<Object> list = new ArrayList<>(jsonArray.size());

			for (JsonElement jsonElement : jsonArray) {
				list.add(parseValue(jsonElement, makeReadonly));
			}
			return makeReadonly ? Collections.unmodifiableList(list) : list;
		}

		// MAP
		if (element.isJsonObject()) {
			JsonObject jsonObject = element.getAsJsonObject();
			Map<String, Object> map = new LinkedHashMap<>(jsonObject.keySet().size());

			for (String key : jsonObject.keySet()) {
				map.put(key, parseValue(jsonObject.get(key), makeReadonly));
			}
			return makeReadonly ? Collections.unmodifiableMap(map) : map;
		}
		throw new IllegalStateException();
	}
}
