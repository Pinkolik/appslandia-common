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

package com.appslandia.common.utils;

import java.util.Map;
import java.util.regex.Pattern;

import com.appslandia.common.base.Out;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class SYS {

	public static boolean getBoolProp(String key, boolean defaultValue) {
		String value = StringUtils.trimToNull(System.getProperty(key));
		return ParseUtils.parseBool(value, defaultValue);
	}

	public static int getIntProp(String key, int defaultValue) {
		String value = StringUtils.trimToNull(System.getProperty(key));
		return ParseUtils.parseInt(value, defaultValue);
	}

	public static long getLongProp(String key, long defaultValue) {
		String value = StringUtils.trimToNull(System.getProperty(key));
		return ParseUtils.parseLong(value, defaultValue);
	}

	public static double getDoubleProp(String key, double defaultValue) {
		String value = StringUtils.trimToNull(System.getProperty(key));
		return ParseUtils.parseDouble(value, defaultValue);
	}

	public static String getProp(String key, String defaultValue) {
		String value = StringUtils.trimToNull(System.getProperty(key));
		return (value != null) ? value : defaultValue;
	}

	public static String getRequiredProp(String key) {
		String value = getProp(key, null);
		if (value == null) {
			throw new IllegalStateException("The property '" + key + "' doesn't have a value.");
		}
		return value;
	}

	public static String getEnv(String key, String defaultValue) {
		String value = StringUtils.trimToNull(System.getenv(key));
		return (value != null) ? value : defaultValue;
	}

	public static String getRequiredEnv(String key) {
		String value = getEnv(key, null);
		if (value == null) {
			throw new IllegalStateException("The env." + key + " doesn't have a value.");
		}
		return value;
	}

	public static String resolveString(String str) throws IllegalStateException {
		if (str == null) {
			return null;
		}
		return StringFormat.format(str, (p, expr) -> {
			// SYS
			String resolvedValue = resolveExpr(expr, new Out<Boolean>());

			if (resolvedValue == null) {
				throw toNoValueExprException(expr);
			}
			return resolvedValue;
		});
	}

	public static String resolveString(String str, Map<String, Object> parameters) throws IllegalStateException {
		if (str == null) {
			return null;
		}
		return StringFormat.format(str, (pname, expr) -> {
			// Parameters
			Object resolvedValue = parameters.get(pname);

			// SYS
			if (resolvedValue == null) {
				resolvedValue = resolveExpr(expr, new Out<>());
			}
			if (resolvedValue == null) {
				throw toNoValueExprException(expr);
			}
			return resolvedValue;
		});
	}

	public static String resolveString(String str, Object... parameters) throws IllegalStateException {
		if (str == null) {
			return null;
		}
		return StringFormat.format(str, (pname, expr) -> {

			Object resolvedValue = null;
			try {
				int index = Integer.parseInt(pname);

				// Parameters
				if ((0 <= index) && (index < parameters.length)) {
					resolvedValue = parameters[index];
				}

			} catch (NumberFormatException ex) {
				// SYS
				resolvedValue = resolveExpr(expr, new Out<>());
			}

			if (resolvedValue == null) {
				throw toNoValueExprException(expr);
			}
			return resolvedValue;
		});
	}

	public static String resolveExpr(String valueOrExpr) throws IllegalStateException {
		AssertUtils.assertNotNull(valueOrExpr);

		String resolvedValue = resolveExpr(valueOrExpr, new Out<>());

		if (resolvedValue == null) {
			throw toNoValueExprException(valueOrExpr);
		}
		return resolvedValue;
	}

	// ${prop_name}
	// ${prop_name:defaultValue}
	// ${ENV.env_name}
	// ${ENV.env_name:defaultValue}
	// ${prop_name, ENV.env_name:defaultValue}

	private static final String ENV_VAL_EXPR_PATTERN = StringFormat.format("((${0})|((${0}\\s*,\\s*)?env.${0}))(\\:[^\\s]+.+)?", "[a-z\\d._]+");
	private static final Pattern ENV_VAL_HOLDER_PATTERN = Pattern.compile(StringFormat.format("\\$\\{\\s*${0}\\s*}", ENV_VAL_EXPR_PATTERN),
			Pattern.CASE_INSENSITIVE);

	public static String resolveExpr(String valueOrExpr, Out<Boolean> isExpr) {
		AssertUtils.assertNotNull(valueOrExpr);

		// Normal value?
		if (!ENV_VAL_HOLDER_PATTERN.matcher(valueOrExpr).matches()) {
			isExpr.value = Boolean.FALSE;
			return valueOrExpr;
		}

		// Expression
		isExpr.value = Boolean.TRUE;

		String expr = valueOrExpr.substring(2, valueOrExpr.length() - 1).trim();
		return resolve(expr);
	}

	private static String resolve(String expr) {
		int commaIdx = expr.indexOf(',');
		int colonIdx = expr.indexOf(':');

		if (commaIdx < 0) {
			if (colonIdx < 0) {
				// env.env_name
				if (StringUtils.startsWithIgnoreCase(expr, "env.")) {
					return getEnv(expr.substring(4), null);
				}
				// prop_name
				return getProp(expr, null);
			} else {
				String defaultValue = expr.substring(colonIdx + 1);
				String leftExpr = expr.substring(0, colonIdx);

				// env.env_name:defaultValue
				if (StringUtils.startsWithIgnoreCase(leftExpr, "env.")) {
					return getEnv(leftExpr.substring(4), defaultValue);
				}
				// prop_name:defaultValue
				return getProp(leftExpr, defaultValue);
			}
		} else {
			if (colonIdx < 0) {
				// prop_name, env.env_name
				String val = getProp(expr.substring(0, commaIdx).trim(), null);
				if (val == null) {
					val = getEnv(expr.substring(commaIdx + 1).trim().substring(4), null);
				}
				return val;
			}

			// prop_name:defaultValue_with_,
			// env.env_name:defaultValue_with_,
			// prop_name,env.env_name:defaultValue

			String defaultValue = expr.substring(colonIdx + 1);
			String leftExpr = expr.substring(0, colonIdx);
			int leftCommaIdx = leftExpr.indexOf(',');

			if (leftCommaIdx < 0) {
				// env.env_name:defaultValue_with_,
				if (StringUtils.startsWithIgnoreCase(leftExpr, "env.")) {
					return getEnv(leftExpr.substring(4), defaultValue);
				}

				// prop_name:defaultValue_with_,
				return getProp(leftExpr, defaultValue);
			} else {
				// prop_name,env.env_name:defaultValue
				String val = getProp(leftExpr.substring(0, leftCommaIdx).trim(), null);
				if (val == null) {
					val = getEnv(leftExpr.substring(leftCommaIdx + 1).trim().substring(4), defaultValue);
				}
				return val;
			}
		}
	}

	public static IllegalStateException toNoValueExprException(String expr) {
		return new IllegalStateException("Can't resolve value for expression '" + expr + "'");
	}
}
