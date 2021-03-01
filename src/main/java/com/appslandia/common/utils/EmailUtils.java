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

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class EmailUtils {

	private static final Pattern EMAIL_PATTERN = Pattern.compile("(?=.{6,255}$)[_a-z\\d-\\+]+(\\.[_a-z\\d-]+)*@[a-z\\d-]+(\\.[a-z\\d-]+)*(\\.[a-z]{2,})",
			Pattern.CASE_INSENSITIVE);

	public static boolean isValid(CharSequence email) {
		if (email == null) {
			return true;
		}
		return EMAIL_PATTERN.matcher(email).matches();
	}

	public static InternetAddress parseAddress(String email, String person) throws AddressException {
		AssertUtils.assertNotNull(email);

		InternetAddress address = new InternetAddress(email);
		if (person != null) {
			try {
				address.setPersonal(person, StandardCharsets.UTF_8.name());
			} catch (UnsupportedEncodingException ex) {
				throw new Error(ex);
			}
		}
		return address;
	}

	public static String toPersonEmail(String email, String person) {
		AssertUtils.assertNotNull(email);
		if (person == null)
			return email;

		try {
			return String.format("%s <%s>", MimeUtility.encodeWord(person, StandardCharsets.UTF_8.name(), null), email);
		} catch (UnsupportedEncodingException ex) {
			throw new Error(ex);
		}
	}
}
