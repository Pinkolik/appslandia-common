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

import java.text.NumberFormat;

import com.appslandia.common.base.FormatProvider;
import com.appslandia.common.utils.NumberUtils;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class IntegerFormatter extends NumberFormatter {

	public static final String ERROR_MSG_KEY = IntegerFormatter.class.getName() + ".message";

	public IntegerFormatter() {
		super(false);
	}

	public IntegerFormatter(boolean localized) {
		super(localized);
	}

	@Override
	public String getErrorMsgKey() {
		return ERROR_MSG_KEY;
	}

	@Override
	public Class<?> getArgType() {
		return Integer.class;
	}

	@Override
	protected NumberFormat getLocalizedFormat(FormatProvider formatProvider) {
		return formatProvider.getIntegerFormat();
	}

	@Override
	public Integer parse(String str, FormatProvider formatProvider) throws FormatterException {
		str = StringUtils.trimToNull(str);
		if (str == null) {
			return null;
		}

		if (this.localized) {
			Number number = this.parseNumber(str, formatProvider);
			long value = number.longValue();

			if (!NumberUtils.isIntRange(value)) {
				throw toNumberOverflowError(str);
			}
			return (int) value;
		}

		// Java Format
		try {
			long value = Long.parseLong(str);

			if (!NumberUtils.isIntRange(value)) {
				throw toNumberOverflowError(str);
			}
			return (int) value;

		} catch (NumberFormatException ex) {
			throw toParsingError(str);
		}
	}
}
