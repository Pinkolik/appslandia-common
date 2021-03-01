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

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.appslandia.common.base.FormatProvider;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class Java8DateFormatter<T extends TemporalAccessor> implements Formatter {

	private static final ConcurrentMap<String, DateTimeFormatter> FORMATTERS = new ConcurrentHashMap<>();

	final boolean localized;

	public Java8DateFormatter(boolean localized) {
		this.localized = localized;
	}

	protected abstract String getLocalizedPattern(FormatProvider formatProvider);

	protected abstract String getIsoPattern();

	protected abstract T parse(String str, DateTimeFormatter formatter) throws DateTimeParseException;

	@Override
	public String format(Object obj, FormatProvider formatProvider) {
		if (obj == null) {
			return null;
		}
		if (this.localized) {
			return getFormatter(getLocalizedPattern(formatProvider)).format((TemporalAccessor) obj);
		}

		// ISO
		return getFormatter(getIsoPattern()).format((TemporalAccessor) obj);
	}

	@Override
	public T parse(String str, FormatProvider formatProvider) throws FormatterException {
		str = StringUtils.trimToNull(str);
		if (str == null) {
			return null;
		}

		try {
			if (this.localized) {
				return parse(str, getFormatter(getLocalizedPattern(formatProvider)));
			}
			// ISO
			return parse(str, getFormatter(getIsoPattern()));
		} catch (DateTimeParseException ex) {
		}

		throw new FormatterException("An error occurred while parsing temporal (str=" + str + ", type=" + getArgType() + ")", getErrorMsgKey());
	}

	protected static DateTimeFormatter getFormatter(String pattern) {
		return FORMATTERS.computeIfAbsent(pattern, p -> DateTimeFormatter.ofPattern(p));
	}
}
