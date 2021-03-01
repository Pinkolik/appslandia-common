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

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class LruCacheTest {

	@Test
	public void test() {
		LruCache<String, Integer> cache = new LruCache<>(5);

		cache.put("k1", 1);
		cache.put("k2", 2);
		cache.put("k3", 3);
		cache.put("k4", 4);
		cache.put("k5", 5);

		Assert.assertEquals(cache.get("k1"), Integer.valueOf(1));
		Assert.assertEquals(cache.get("k2"), Integer.valueOf(2));
		Assert.assertEquals(cache.get("k3"), Integer.valueOf(3));
		Assert.assertEquals(cache.get("k4"), Integer.valueOf(4));
		Assert.assertEquals(cache.get("k5"), Integer.valueOf(5));

		cache.put("k6", 6);
		Assert.assertEquals(cache.get("k6"), Integer.valueOf(6));
		Assert.assertNull(cache.get("k1"));
		Assert.assertNotNull(cache.get("k2"));
	}
}
