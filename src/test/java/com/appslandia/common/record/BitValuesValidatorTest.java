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

package com.appslandia.common.record;

import org.junit.Assert;
import org.junit.Test;

import com.appslandia.common.record.FieldValidator.FieldError;
import com.appslandia.common.utils.BitBool;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class BitValuesValidatorTest {

	final BitValuesValidator validator = new BitValuesValidator();

	@Test
	public void test() {
		FieldError error = validator.validate(BitBool.TRUE, null);
		Assert.assertNull(error);

		error = validator.validate(BitBool.FALSE, null);
		Assert.assertNull(error);
	}

	@Test
	public void test_null() {
		FieldError error = validator.validate(null, null);
		Assert.assertNull(error);
	}

	@Test
	public void test_invalid() {
		FieldError error = validator.validate(2, null);
		Assert.assertNotNull(error);
	}
}
