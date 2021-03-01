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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class CollectionUtils {

	public static <K, V> Map<K, V> toMap(K k1, V v1) {
		return toMap(new HashMap<K, V>(), k1, v1);
	}

	public static <K, V> Map<K, V> toMap(K k1, V v1, K k2, V v2) {
		return toMap(new HashMap<K, V>(), k1, v1, k2, v2);
	}

	public static <K, V> Map<K, V> toMap(K k1, V v1, K k2, V v2, K k3, V v3) {
		return toMap(new HashMap<K, V>(), k1, v1, k2, v2, k3, v3);
	}

	public static <K, V> Map<K, V> toMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
		return toMap(new HashMap<K, V>(), k1, v1, k2, v2, k3, v3, k4, v4);
	}

	public static <K, V> Map<K, V> toMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
		return toMap(new HashMap<K, V>(), k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
	}

	public static <K, V> Map<K, V> toMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
		return toMap(new HashMap<K, V>(), k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6);
	}

	public static <K, V> Map<K, V> toMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
		return toMap(new HashMap<K, V>(), k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7);
	}

	public static <K, V> Map<K, V> toMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
		return toMap(new HashMap<K, V>(), k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8);
	}

	public static <K, V> Map<K, V> toMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
		return toMap(new HashMap<K, V>(), k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9);
	}

	public static <K, V> Map<K, V> unmodifiableMap(K k1, V v1) {
		return ObjectUtils.cast(Collections.unmodifiableMap(toMap(new HashMap<>(), k1, v1)));
	}

	public static <K, V> Map<K, V> unmodifiableMap(K k1, V v1, K k2, V v2) {
		return ObjectUtils.cast(Collections.unmodifiableMap(toMap(new HashMap<>(), k1, v1, k2, v2)));
	}

	public static <K, V> Map<K, V> unmodifiableMap(K k1, V v1, K k2, V v2, K k3, V v3) {
		return ObjectUtils.cast(Collections.unmodifiableMap(toMap(new HashMap<>(), k1, v1, k2, v2, k3, v3)));
	}

	public static <K, V> Map<K, V> unmodifiableMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
		return ObjectUtils.cast(Collections.unmodifiableMap(toMap(new HashMap<>(), k1, v1, k2, v2, k3, v3, k4, v4)));
	}

	public static <K, V> Map<K, V> unmodifiableMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
		return ObjectUtils.cast(Collections.unmodifiableMap(toMap(new HashMap<>(), k1, v1, k2, v2, k3, v3, k4, v4, k5, v5)));
	}

	public static <K, V> Map<K, V> unmodifiableMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
		return ObjectUtils.cast(Collections.unmodifiableMap(toMap(new HashMap<>(), k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6)));
	}

	public static <K, V> Map<K, V> unmodifiableMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
		return ObjectUtils.cast(Collections.unmodifiableMap(toMap(new HashMap<>(), k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7)));
	}

	public static <K, V> Map<K, V> unmodifiableMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
		return ObjectUtils.cast(Collections.unmodifiableMap(toMap(new HashMap<>(), k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8)));
	}

	public static <K, V> Map<K, V> unmodifiableMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
		return ObjectUtils.cast(Collections.unmodifiableMap(toMap(new HashMap<>(), k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9)));
	}

	public static <K, V> Map<K, V> unmodifiableMap(Object... entries) {
		return ObjectUtils.cast(Collections.unmodifiableMap(toMap(new HashMap<>(), entries)));
	}

	public static <K, V> Map<K, V> unmodifiableMap(Map<K, V> m, Object... entries) {
		return ObjectUtils.cast(Collections.unmodifiableMap(toMap(m, entries)));
	}

	public static <K, V> Map<K, V> toMap(Object... entries) {
		return toMap(new HashMap<K, V>(), entries);
	}

	public static <K, V> Map<K, V> toMap(Map<K, V> m, Object... keyValues) {
		AssertUtils.assertTrue(keyValues.length % 2 == 0, "keyValues is invalid.");

		for (int i = 0; i < keyValues.length; i += 2) {
			K k = ObjectUtils.cast(keyValues[i]);
			V v = ObjectUtils.cast(keyValues[i + 1]);

			m.put(k, v);
		}
		return m;
	}

	@SafeVarargs
	public static <V> Set<V> unmodifiableSet(V... elements) {
		return Collections.unmodifiableSet(toSet(new HashSet<V>(), elements));
	}

	@SafeVarargs
	public static <V> Set<V> unmodifiableSet(Set<V> s, V... elements) {
		return Collections.unmodifiableSet(toSet(s, elements));
	}

	@SafeVarargs
	public static <V> Set<V> toSet(V... elements) {
		return toSet(new HashSet<V>(), elements);
	}

	@SafeVarargs
	public static <V> Set<V> toSet(Set<V> s, V... elements) {
		Arrays.stream(elements).forEach(e -> s.add(e));
		return s;
	}

	@SafeVarargs
	public static <V> List<V> unmodifiableList(V... elements) {
		return Collections.unmodifiableList(toList(new ArrayList<V>(), elements));
	}

	@SafeVarargs
	public static <V> List<V> unmodifiableList(List<V> l, V... elements) {
		return Collections.unmodifiableList(toList(l, elements));
	}

	@SafeVarargs
	public static <V> List<V> toList(V... elements) {
		return toList(new ArrayList<V>(), elements);
	}

	@SafeVarargs
	public static <V> List<V> toList(List<V> l, V... elements) {
		Arrays.stream(elements).forEach(e -> l.add(e));
		return l;
	}

	public static <V> List<V> unmodifiable(List<V> list) {
		return ((list != null) && !list.isEmpty()) ? Collections.unmodifiableList(list) : Collections.<V>emptyList();
	}

	public static <K, V> Map<K, V> unmodifiable(Map<K, V> map) {
		return ((map != null) && !map.isEmpty()) ? Collections.unmodifiableMap(map) : Collections.<K, V>emptyMap();
	}

	public static <V> Set<V> unmodifiable(Set<V> set) {
		return ((set != null) && !set.isEmpty()) ? Collections.unmodifiableSet(set) : Collections.<V>emptySet();
	}

	public static <V> List<V> unmodifiableOrNull(List<V> list) {
		return ((list != null) && !list.isEmpty()) ? Collections.unmodifiableList(list) : null;
	}

	public static <K, V> Map<K, V> unmodifiableOrNull(Map<K, V> map) {
		return ((map != null) && !map.isEmpty()) ? Collections.unmodifiableMap(map) : null;
	}

	public static <V> Set<V> unmodifiableOrNull(Set<V> set) {
		return ((set != null) && !set.isEmpty()) ? Collections.unmodifiableSet(set) : null;
	}

	public static <K, V> Map<V, K> inverse(Map<K, V> m, Map<V, K> newMap) {
		for (Entry<K, V> entry : m.entrySet()) {
			newMap.put(entry.getValue(), entry.getKey());
		}
		return newMap;
	}

	public static <T> boolean hasElements(Collection<T> c) {
		return (c != null) && (c.size() > 0);
	}
}
