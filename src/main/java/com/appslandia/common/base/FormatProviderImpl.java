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

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class FormatProviderImpl implements FormatProvider {

	protected final Language language;

	protected NumberFormat integerFormat;
	protected NumberFormat decimalFormat;

	protected ProviderMap<String, SimpleDateFormat> dateFormats;
	protected ProviderMap<Locale, NumberFormat> currencyFormats;

	public FormatProviderImpl() {
		this(Language.getDefault());
	}

	public FormatProviderImpl(Language language) {
		this.language = language;
	}

	@Override
	public Language getLanguage() {
		return this.language;
	}

	@Override
	public NumberFormat getIntegerFormat() {
		if (this.integerFormat == null) {
			this.integerFormat = NumberFormat.getIntegerInstance(this.language.getLocale());
		}
		return this.integerFormat;
	}

	@Override
	public NumberFormat getDecimalFormat() {
		if (this.decimalFormat == null) {
			NumberFormat impl = NumberFormat.getNumberInstance(this.language.getLocale());

			if (impl instanceof DecimalFormat) {
				((DecimalFormat) impl).setParseBigDecimal(true);
			}
			this.decimalFormat = impl;
		}
		return this.decimalFormat;
	}

	@Override
	public DateFormat getDateFormat(String pattern) {
		return this.getDateFormats().get(pattern);
	}

	protected ProviderMap<String, SimpleDateFormat> getDateFormats() {
		if (this.dateFormats != null) {
			return this.dateFormats;
		}
		return this.dateFormats = new ProviderMap<String, SimpleDateFormat>((pattern) -> {
			SimpleDateFormat value = new SimpleDateFormat(pattern);

			value.setLenient(false);
			return value;
		});
	}

	@Override
	public NumberFormat getCurrencyFormat(Locale locale) {
		return this.getCurrencyFormats().get(locale);
	}

	protected ProviderMap<Locale, NumberFormat> getCurrencyFormats() {
		if (this.currencyFormats != null) {
			return this.currencyFormats;
		}
		return this.currencyFormats = new ProviderMap<Locale, NumberFormat>((locale) -> {
			NumberFormat value = NumberFormat.getCurrencyInstance(locale);

			if (value instanceof DecimalFormat) {
				((DecimalFormat) value).setParseBigDecimal(true);
			}
			return value;
		});
	}
}
