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

import java.util.Map;

import com.appslandia.common.utils.ParseUtils;
import com.appslandia.common.utils.SYS;
import com.appslandia.common.utils.SplitUtils;
import com.appslandia.common.utils.StringFormat;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public interface Config {

	String getString(String key);

	default public String getString(String key, String defaultValue) {
		String value = getString(key);
		return (value != null) ? value : defaultValue;
	}

	default public String getRequiredString(String key) throws IllegalStateException {
		String value = getString(key);
		if (value == null) {
			throw toNoValueException(key);
		}
		return value;
	}

	default public String[] getStringArray(String key) {
		String value = getString(key);
		if (value == null) {
			return StringUtils.EMPTY_ARRAY;
		}
		return SplitUtils.split(value, ',');
	}

	default public String getFormatted(String key) throws IllegalStateException {
		String value = getString(key);
		if (value == null) {
			return null;
		}
		return StringFormat.format(value, (pname, expr) -> {
			// CONFIG
			String resolvedValue = getString(pname);

			// SYS
			if (resolvedValue == null) {
				resolvedValue = SYS.resolveExpr(expr, new Out<>());
			}
			if (resolvedValue == null) {
				throw SYS.toNoValueExprException(expr);
			}
			return resolvedValue;
		});
	}

	default public String getRequiredFormatted(String key) throws IllegalStateException {
		String value = getFormatted(key);
		if (value == null) {
			throw toNoValueException(key);
		}
		return value;
	}

	default public String getFormatted(String key, Map<String, Object> parameters) throws IllegalStateException {
		String value = getString(key);
		if (value == null) {
			return null;
		}
		return StringFormat.format(value, (pname, expr) -> {
			// Parameters
			Object resolvedValue = parameters.get(pname);

			// CONFIG
			if (resolvedValue == null) {
				resolvedValue = getString(pname);
			}

			// SYS
			if (resolvedValue == null) {
				resolvedValue = SYS.resolveExpr(expr, new Out<>());
			}
			if (resolvedValue == null) {
				throw SYS.toNoValueExprException(expr);
			}
			return resolvedValue;
		});
	}

	default public String getRequiredFormatted(String key, Map<String, Object> parameters) throws IllegalStateException {
		String value = getFormatted(key, parameters);
		if (value == null) {
			throw toNoValueException(key);
		}
		return value;
	}

	default public String getFormatted(String key, Object... parameters) throws IllegalStateException {
		String value = getString(key);
		if (value == null) {
			return null;
		}
		return StringFormat.format(value, (pname, expr) -> {

			Object resolvedValue = null;
			try {
				int index = Integer.parseInt(pname);

				// Parameters
				if ((0 <= index) && (index < parameters.length)) {
					resolvedValue = parameters[index];
				}

			} catch (NumberFormatException ex) {
				// SYS
				resolvedValue = SYS.resolveExpr(expr, new Out<>());
			}

			if (resolvedValue == null) {
				throw SYS.toNoValueExprException(expr);
			}
			return resolvedValue;
		});
	}

	default public String getRequiredFormatted(String key, Object... parameters) throws IllegalStateException {
		String value = getFormatted(key, parameters);
		if (value == null) {
			throw toNoValueException(key);
		}
		return value;
	}

	default public boolean getBool(String key, boolean defaultValue) {
		String value = getString(key);
		return ParseUtils.parseBool(value, defaultValue);
	}

	default public boolean getRequiredBool(String key) throws IllegalStateException, BoolFormatException {
		String value = getString(key);
		if (value == null) {
			throw toNoValueException(key);
		}
		if (ParseUtils.isTrueValue(value)) {
			return true;
		}
		if (ParseUtils.isFalseValue(value)) {
			return false;
		}
		throw new BoolFormatException(value);
	}

	default public int getInt(String key, int defaultValue) {
		String value = getString(key);
		if (value == null) {
			return defaultValue;
		}
		return ParseUtils.parseInt(value, defaultValue);
	}

	default public int getRequiredInt(String key) throws IllegalStateException, NumberFormatException {
		String value = getString(key);
		if (value == null) {
			throw toNoValueException(key);
		}
		return Integer.parseInt(value);
	}

	default public long getLong(String key, long defaultValue) {
		String value = getString(key);
		if (value == null) {
			return defaultValue;
		}
		return ParseUtils.parseLong(value, defaultValue);
	}

	default public long getRequiredLong(String key) throws IllegalStateException, NumberFormatException {
		String value = getString(key);
		if (value == null) {
			throw toNoValueException(key);
		}
		return Long.parseLong(value);
	}

	default public double getDouble(String key, double defaultValue) {
		String value = getString(key);
		if (value == null) {
			return defaultValue;
		}
		return ParseUtils.parseDouble(value, defaultValue);
	}

	default public double getRequiredDouble(String key) throws IllegalStateException, NumberFormatException {
		String value = getString(key);
		if (value == null) {
			throw toNoValueException(key);
		}
		double val = Double.parseDouble(value);
		if (Double.isNaN(val)) {
			throw toNoValueException(key);
		}
		return val;
	}

	static IllegalStateException toNoValueException(String key) {
		return new IllegalStateException("The key '" + key + "' doesn't have a value.");
	}
}
