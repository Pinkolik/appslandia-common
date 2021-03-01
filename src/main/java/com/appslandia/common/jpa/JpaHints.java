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

package com.appslandia.common.jpa;

import java.util.Collections;
import java.util.Map;

import javax.persistence.CacheRetrieveMode;
import javax.persistence.CacheStoreMode;
import javax.persistence.PessimisticLockScope;

import com.appslandia.common.base.Params;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JpaHints {

	public static final String HINT_CACHE_STORE_MODE = "javax.persistence.cache.storeMode";
	public static final String HINT_CACHE_RETRIEVE_MODE = "javax.persistence.cache.retrieveMode";

	public static final String HINT_JPA_FETCH_GRAPH = "javax.persistence.fetchgraph";
	public static final String HINT_JPA_LOAD_GRAPH = "javax.persistence.loadgraph";

	public static final String HINT_PESSIMISTIC_LOCK_SCOPE = "javax.persistence.lock.scope";
	public static final String HINT_PESSIMISTIC_LOCK_TIMEOUT = "javax.persistence.lock.timeout";

	public static final Map<String, Object> HINT_PROP_CACHE_STORE_USE = Collections.unmodifiableMap(Params.of(HINT_CACHE_STORE_MODE, CacheStoreMode.USE));
	public static final Map<String, Object> HINT_PROP_CACHE_STORE_REFRESH = Collections
			.unmodifiableMap(Params.of(HINT_CACHE_STORE_MODE, CacheStoreMode.REFRESH));
	public static final Map<String, Object> HINT_PROP_CACHE_STORE_BYPASS = Collections.unmodifiableMap(Params.of(HINT_CACHE_STORE_MODE, CacheStoreMode.BYPASS));

	public static final Map<String, Object> HINT_PROP_CACHE_RETRIEVE_USE = Collections
			.unmodifiableMap(Params.of(HINT_CACHE_RETRIEVE_MODE, CacheRetrieveMode.USE));
	public static final Map<String, Object> HINT_PROP_CACHE_RETRIEVE_BYPASS = Collections
			.unmodifiableMap(Params.of(HINT_CACHE_RETRIEVE_MODE, CacheRetrieveMode.BYPASS));

	public static final Map<String, Object> HINT_PROP_PESSIMISTIC_LOCK_EXTENDED = Collections
			.unmodifiableMap(Params.of(HINT_PESSIMISTIC_LOCK_SCOPE, PessimisticLockScope.EXTENDED));

	public static final String HINT_QUERY_READONLY = "hint.query_readonly";

	private static volatile HintMapper __hintMapper;
	private static final Object MUTEX = new Object();

	public static HintMapper getHintMapper() {
		HintMapper obj = __hintMapper;
		if (obj == null) {
			synchronized (MUTEX) {
				if ((obj = __hintMapper) == null) {
					__hintMapper = obj = initHintMapper();
				}
			}
		}
		return obj;
	}

	public static void setHintMapper(HintMapper impl) {
		if (__hintMapper == null) {
			synchronized (MUTEX) {
				if (__hintMapper == null) {
					__hintMapper = impl;
					return;
				}
			}
		}
		throw new IllegalStateException("JpaHints.__hintMapper must be null.");
	}

	private static HintMapper initHintMapper() {
		HintMapper impl = new HintMapper();
		return impl;
	}
}
