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

package com.appslandia.common.validators;

import java.util.HashMap;
import java.util.Map;

import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.ObjectUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class ModelValidator<T> {

	public abstract boolean validate(T model);

	private static final Map<String, ModelValidator<?>> VALIDATORS = new HashMap<String, ModelValidator<?>>();

	public static void addValidator(String key, ModelValidator<?> modelValidator) {
		AssertUtils.assertTrue(!VALIDATORS.containsKey(key), "validator already exists (key=" + key + ")");
		VALIDATORS.put(key, modelValidator);
	}

	public static <T> ModelValidator<T> getValidator(String key) throws IllegalArgumentException, ClassCastException {
		ModelValidator<?> validator = VALIDATORS.get(key);
		if (validator == null) {
			throw new IllegalArgumentException("validator is not found (key=" + key + ")");
		}
		return ObjectUtils.cast(validator);
	}
}
