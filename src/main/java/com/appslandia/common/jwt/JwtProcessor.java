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

import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import com.appslandia.common.base.BaseEncoder;
import com.appslandia.common.base.DestroyException;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.crypto.CryptoException;
import com.appslandia.common.json.JsonException;
import com.appslandia.common.json.JsonProcessor;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.ValueUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JwtProcessor extends InitializeObject {

	protected JsonProcessor jsonProcessor;
	protected JwtSigner jwtSigner;
	protected String issuer;

	@Override
	protected void init() throws Exception {
		AssertUtils.assertNotNull(this.jsonProcessor, "jsonProcessor is required.");

		this.jwtSigner = ValueUtils.valueOrAlt(this.jwtSigner, JwtSigner.NONE);
	}

	@Override
	public void destroy() throws DestroyException {
		if (this.jsonProcessor != null) {
			this.jsonProcessor.destroy();
		}
		if (this.jwtSigner != null) {
			this.jwtSigner.destroy();
		}
	}

	public JwtHeader newHeader() {
		this.initialize();
		return new JwtHeader().setType("JWT").setAlgorithm(this.jwtSigner.getAlg());
	}

	public JwtPayload newPayload() {
		this.initialize();
		return new JwtPayload().setIssuer(this.issuer);
	}

	public String toJwt(JwtToken jwt) throws CryptoException, JsonException, JwtException {
		this.initialize();
		AssertUtils.assertNotNull(jwt);
		AssertUtils.assertNotNull(jwt.getHeader());
		AssertUtils.assertNotNull(jwt.getPayload());

		// Header
		String base64Header = BaseEncoder.BASE64_URL.encode(this.jsonProcessor.toByteArray(jwt.getHeader()));

		// PAYLOAD
		String base64Payload = BaseEncoder.BASE64_URL.encode(this.jsonProcessor.toByteArray(jwt.getPayload()));

		// No ALG
		if (this.jwtSigner == JwtSigner.NONE) {
			return newBuilder(base64Header, base64Payload, 1).append(".").toString();
		}

		// Signature
		String dataToSign = newBuilder(base64Header, base64Payload, 0).toString();
		String base64Sig = BaseEncoder.BASE64_URL.encode(this.jwtSigner.sign(dataToSign.getBytes(StandardCharsets.UTF_8)));

		// JWT
		return newBuilder(base64Header, base64Payload, 1 + base64Sig.length()).append(".").append(base64Sig).toString();
	}

	public JwtToken parseJwt(String jwt) throws IllegalArgumentException, CryptoException, JsonException, JwtException {
		this.initialize();
		AssertUtils.assertNotNull(jwt);

		String[] parts = JwtUtils.parseParts(jwt);
		if (parts == null) {
			throw new JwtException("JWT token is invalid format.");
		}

		// No ALG
		if (parts[2] == null) {
			if (this.jwtSigner != JwtSigner.NONE) {
				throw new JwtException("JWT signature is required.");
			}
		} else {
			if (this.jwtSigner == JwtSigner.NONE) {
				throw new JwtException("jwtSigner is required.");
			}

			// ALG
			String dataToSign = newBuilder(parts[0], parts[1], 0).toString();

			if (!this.jwtSigner.verify(dataToSign.getBytes(StandardCharsets.UTF_8), BaseEncoder.BASE64_URL.decode(parts[2]))) {
				throw new JwtException("JWT signature is invalid.");
			}
		}

		// Header
		String headerJson = new String(BaseEncoder.BASE64_URL.decode(parts[0]), StandardCharsets.UTF_8);
		JwtHeader header = this.jsonProcessor.read(new StringReader(headerJson), JwtHeader.class);

		// PAYLOAD
		String payloadJson = new String(BaseEncoder.BASE64_URL.decode(parts[1]), StandardCharsets.UTF_8);
		JwtPayload payload = this.jsonProcessor.read(new StringReader(payloadJson), JwtPayload.class);

		return new JwtToken(header, payload);
	}

	public JsonProcessor getJsonProcessor() {
		this.initialize();
		return this.jsonProcessor;
	}

	public JwtProcessor setJsonProcessor(JsonProcessor jsonProcessor) {
		assertNotInitialized();
		this.jsonProcessor = jsonProcessor;
		return this;
	}

	public JwtSigner getJwtSigner() {
		this.initialize();
		return this.jwtSigner;
	}

	public JwtProcessor setJwtSigner(JwtSigner jwtSigner) {
		assertNotInitialized();
		this.jwtSigner = jwtSigner;
		return this;
	}

	public JwtProcessor setIssuer(String issuer) {
		assertNotInitialized();
		this.issuer = issuer;
		return this;
	}

	static StringBuilder newBuilder(String base64Header, String base64Payload, int signtureLen) {
		return new StringBuilder(base64Header.length() + 1 + base64Payload.length() + signtureLen).append(base64Header).append(".").append(base64Payload);
	}
}
