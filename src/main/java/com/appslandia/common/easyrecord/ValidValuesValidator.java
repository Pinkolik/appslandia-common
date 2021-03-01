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

package com.appslandia.common.easyrecord;

import java.lang.reflect.Array;

import com.appslandia.common.utils.AssertUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ValidValuesValidator implements FieldValidator {

	public static final String ERROR_MSG_KEY = ValidValuesValidator.class.getName() + ".message";

	@Override
	public FieldError validate(Object value, Object constraintArgs) {
		AssertUtils.assertNotNull(constraintArgs, "constraintArgs is required.");
		AssertUtils.assertTrue(constraintArgs.getClass().isArray(), "constraintArgs must be an array.");

		if (value == null) {
			return null;
		}
		String valueToCheck = value.toString();

		int len = Array.getLength(constraintArgs);
		for (int i = 0; i < len; i++) {
			String validValue = String.valueOf(Array.get(constraintArgs, i));

			if (validValue.equalsIgnoreCase(valueToCheck)) {
				return null;
			}
		}
		return new FieldError(ERROR_MSG_KEY, constraintArgs);
	}
}
