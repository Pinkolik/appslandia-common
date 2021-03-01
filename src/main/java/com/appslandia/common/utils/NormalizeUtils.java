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

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class NormalizeUtils {

	public interface DecomposedCharacterConverter {
		char convert(char decomposedCharacter);
	}

	private static DecomposedCharacterConverter decomposedCharacterConverter;

	public static void setDecomposedCharacterConverter(DecomposedCharacterConverter converter) {
		AssertUtils.assertNull(decomposedCharacterConverter);
		decomposedCharacterConverter = converter;
	}

	private static final Pattern STRIP_ACCENTS_PATTERN = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

	public static String unaccent(String text) {
		if (text == null) {
			return null;
		}
		StringBuilder decomposed = new StringBuilder(Normalizer.normalize(text, Normalizer.Form.NFD));
		if (decomposedCharacterConverter != null) {
			for (int i = 0; i < decomposed.length(); i++) {
				char converted = decomposedCharacterConverter.convert(decomposed.charAt(i));

				if (converted != decomposed.charAt(i)) {
					decomposed.deleteCharAt(i);
					decomposed.insert(i, converted);
				}
			}
		}
		return STRIP_ACCENTS_PATTERN.matcher(decomposed).replaceAll(StringUtils.EMPTY_STRING);
	}

	private static final Pattern[] WTSP_PUNCT_HYPHEN_PATTERNS = PatternUtils.compile("\\s+|\\p{Punct}+", "-{2,}");

	public static String normalizeLabel(String label) {
		label = normalize(label, WTSP_PUNCT_HYPHEN_PATTERNS, '-');
		return StringUtils.toLowerCase(label, Locale.ROOT);
	}

	private static final Pattern[] WTSPACE_PATTERNS = PatternUtils.compile("\\s+");

	public static String normalizeText(String str) {
		return normalize(str, WTSPACE_PATTERNS, (char) 32);
	}

	public static String normalize(String str, Pattern[] matchers, char charReplacement) {
		if (str == null) {
			return null;
		}
		String replacement = new String(new char[] { charReplacement });
		for (Pattern p : matchers) {
			str = p.matcher(str).replaceAll(Matcher.quoteReplacement(replacement));
		}
		return StringUtils.trimToNull(str, charReplacement);
	}

	public static String normalize(String str, Pattern[] matchers, String replacement) {
		if (str == null) {
			return null;
		}
		for (Pattern p : matchers) {
			str = p.matcher(str).replaceAll(Matcher.quoteReplacement(replacement));
		}
		return StringUtils.trimToNull(str);
	}

	static final Pattern[] CRLF3_PATTERNS = PatternUtils.compile("(\r?\n){3,}");

	public static String normalizeTextML(String text) {
		if (text == null)
			return null;

		return normalize(text, CRLF3_PATTERNS, StringUtils.DOUBLE_LINE_SEP);
	}
}
