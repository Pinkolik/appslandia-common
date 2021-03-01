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

package com.appslandia.common.jwt;

import java.util.Date;
import java.util.Map;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JwtHeader extends JwtClaims {
	private static final long serialVersionUID = 1L;

	public static final String TYP = "typ";
	public static final String ALG = "alg";
	public static final String KID = "kid";

	public JwtHeader() {
	}

	public JwtHeader(Map<String, Object> map) {
		super(map);
	}

	@Override
	public JwtHeader set(String key, Object value) {
		super.set(key, value);
		return this;
	}

	@Override
	public JwtHeader setArray(String key, Object... values) {
		super.setArray(key, values);
		return this;
	}

	@Override
	public JwtHeader setNumericDate(String key, Date value) {
		super.setNumericDate(key, value);
		return this;
	}

	@Override
	public JwtHeader setNumericDate(String key, long timeInMs) {
		super.setNumericDate(key, timeInMs);
		return this;
	}

	public String getType() {
		return (String) this.get(TYP);
	}

	public JwtHeader setType(String value) {
		this.put(TYP, value);
		return this;
	}

	public String getAlgorithm() {
		return (String) this.get(ALG);
	}

	public JwtHeader setAlgorithm(String value) {
		this.put(ALG, value);
		return this;
	}

	public String getKid() {
		return (String) this.get(KID);
	}

	public JwtHeader setKid(String value) {
		this.put(KID, value);
		return this;
	}
}
