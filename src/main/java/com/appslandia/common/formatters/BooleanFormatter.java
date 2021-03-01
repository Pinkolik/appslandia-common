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

package com.appslandia.common.formatters;

import java.util.Locale;

import com.appslandia.common.base.FormatProvider;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class BooleanFormatter implements Formatter {

	public static final String ERROR_MSG_KEY = BooleanFormatter.class.getName() + ".message";

	public static final String VALUE_TRUE = Boolean.TRUE.toString();
	public static final String VALUE_FALSE = Boolean.FALSE.toString();

	@Override
	public String getErrorMsgKey() {
		return ERROR_MSG_KEY;
	}

	@Override
	public Class<?> getArgType() {
		return Boolean.class;
	}

	@Override
	public String format(Object obj, FormatProvider formatProvider) {
		if (obj == null) {
			return null;
		}
		return Boolean.TRUE.equals(obj) ? VALUE_TRUE : VALUE_FALSE;
	}

	@Override
	public Boolean parse(String str, FormatProvider formatProvider) throws FormatterException {
		str = StringUtils.trimToNull(str);
		if (str == null) {
			return null;
		}
		String val = str.toLowerCase(Locale.ENGLISH);
		if (VALUE_TRUE.equals(val)) {
			return Boolean.TRUE;
		}
		if (VALUE_FALSE.equals(val)) {
			return Boolean.FALSE;
		}
		throw toParsingError(str);
	}
}
