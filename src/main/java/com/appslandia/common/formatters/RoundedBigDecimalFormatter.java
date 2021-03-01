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

import java.math.BigDecimal;
import java.text.NumberFormat;

import com.appslandia.common.base.FormatProvider;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class RoundedBigDecimalFormatter extends NumberFormatter {

	public static final String ERROR_MSG_KEY = RoundedBigDecimalFormatter.class.getName() + ".message";

	final int scale;
	final int roundingMode;

	public RoundedBigDecimalFormatter(int scale, int roundingMode) {
		super(false);

		this.scale = scale;
		this.roundingMode = roundingMode;
	}

	public RoundedBigDecimalFormatter(int scale, int roundingMode, boolean localized) {
		super(localized);

		this.scale = scale;
		this.roundingMode = roundingMode;
	}

	@Override
	public String getErrorMsgKey() {
		return ERROR_MSG_KEY;
	}

	@Override
	public Class<?> getArgType() {
		return BigDecimal.class;
	}

	@Override
	protected NumberFormat getLocalizedFormat(FormatProvider formatProvider) {
		return formatProvider.getDecimalFormat();
	}

	@Override
	public BigDecimal parse(String str, FormatProvider formatProvider) throws FormatterException {
		str = StringUtils.trimToNull(str);
		if (str == null) {
			return null;
		}

		if (this.localized) {
			Number number = this.parseNumber(str, formatProvider);

			if (number instanceof BigDecimal) {
				return ((BigDecimal) number).setScale(this.scale, this.roundingMode);
			}
			return new BigDecimal(number.toString()).setScale(this.scale, this.roundingMode);
		}

		// Java Format
		try {
			return new BigDecimal(str).setScale(this.scale, this.roundingMode);

		} catch (NumberFormatException ex) {
			throw toParsingError(str);
		}
	}
}
