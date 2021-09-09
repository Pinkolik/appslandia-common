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

package com.appslandia.common.crypto;

import java.security.SecureRandom;
import java.util.Random;
import java.util.regex.Pattern;

import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.CharUtils;
import com.appslandia.common.utils.RandomUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class PasswordUtil {

	// must contain one digit from 0-9
	// must contain one lower case characters
	// must contain one upper case characters
	// must contain one special symbols in the list $@&#!?*%:+-
	// minimum length is 8 characters, maximum is 128
	private static final Pattern PASSWORD_PATTERN = Pattern.compile("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[$@&#!?*%:+-]).{8,128}");

	private static final char[] ALPHABET_SYMBOLS = "$@&#!?*%:+-".toCharArray();
	private static final char[] ALPHABET_UPPER = CharUtils.toCharRanges("A-Z");
	private static final char[] ALPHABET_LOWER = CharUtils.toCharRanges("a-z");
	private static final char[] ALPHABET_DIGITS = CharUtils.toCharRanges("0-9");

	private static final char[][] ALPHABET = new char[][] { ALPHABET_SYMBOLS, ALPHABET_UPPER, ALPHABET_LOWER, ALPHABET_DIGITS };

	final Random random = new SecureRandom();

	public char[] generatePassword(int minLength, int maxLength) {
		AssertUtils.assertTrue(minLength <= maxLength, "minLength <= maxLength");
		AssertUtils.assertTrue(minLength >= 8, "minLength >= 8");

		int length = RandomUtils.nextInt(minLength, maxLength, this.random);
		return CharUtils.randomChars(length, ALPHABET, this.random);
	}

	public static boolean isValid(String password) {
		AssertUtils.assertNotNull(password);
		return PASSWORD_PATTERN.matcher(password).matches();
	}

	public static char[] generatePassword(int length) {
		return new PasswordUtil().generatePassword(length, length);
	}
}
