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

package com.appslandia.common.caching;

import javax.cache.Cache;
import javax.cache.CacheManager;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JCacheManager implements AppCacheManager {

	final CacheManager cacheManager;

	public JCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	@Override
	public <K, V> AppCache<K, V> getCache(String cacheName) {
		Cache<K, V> cache = this.cacheManager.getCache(cacheName);
		return (cache != null) ? new JCache<>(cache) : null;
	}

	@Override
	public boolean clearCache(String cacheName) {
		Cache<?, ?> cache = this.cacheManager.getCache(cacheName);
		if (cache != null) {
			cache.clear();
			return true;
		}
		return false;
	}

	@Override
	public boolean destroyCache(String cacheName) {
		if (this.cacheManager.getCache(cacheName) != null) {
			this.cacheManager.destroyCache(cacheName);
			return true;
		}
		return false;
	}

	@Override
	public Iterable<String> getCacheNames() {
		return this.cacheManager.getCacheNames();
	}

	@Override
	public void close() {
		this.cacheManager.close();
	}
}
