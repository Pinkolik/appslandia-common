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

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import com.appslandia.common.crypto.MacDigester;
import com.appslandia.common.json.GsonMapDeserializer;
import com.appslandia.common.json.GsonProcessor;
import com.google.gson.GsonBuilder;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JwtProcessorTest {

	@Test
	public void test() {
		GsonBuilder gsonBuilder = GsonProcessor.newBuilder().registerTypeAdapter(JwtHeader.class, new GsonMapDeserializer<>((m) -> new JwtHeader(m)))
				.registerTypeAdapter(JwtPayload.class, new GsonMapDeserializer<>((m) -> new JwtPayload(m)));
		GsonProcessor gsonProcessor = new GsonProcessor().setBuilder(gsonBuilder);

		JwtProcessor jwtProcessor = new JwtProcessor().setJsonProcessor(gsonProcessor);
		jwtProcessor.setJwtSigner(new JwtSigner().setAlg("HS256").setSigner(new MacDigester().setAlgorithm("HmacSHA256").setSecret("secret".getBytes())));
		jwtProcessor.setIssuer("Issuer1");

		JwtHeader header = jwtProcessor.newHeader();
		JwtPayload payload = jwtProcessor.newPayload().setExpiresIn(1, TimeUnit.DAYS);

		try {
			String jwt = jwtProcessor.toJwt(new JwtToken(header, payload));
			Assert.assertNotNull(jwt);

			JwtToken token = jwtProcessor.parseJwt(jwt);

			Assert.assertNotNull(token);
			Assert.assertNotNull(token.getHeader());
			Assert.assertNotNull(token.getPayload());

			Assert.assertEquals(token.getHeader().getType(), "JWT");
			Assert.assertEquals(token.getHeader().getAlgorithm(), "HS256");
			Assert.assertEquals(token.getPayload().get("iss"), "Issuer1");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_none() {
		GsonBuilder gsonBuilder = GsonProcessor.newBuilder().registerTypeAdapter(JwtHeader.class, new GsonMapDeserializer<>((m) -> new JwtHeader(m)))
				.registerTypeAdapter(JwtPayload.class, new GsonMapDeserializer<>((m) -> new JwtPayload(m)));
		GsonProcessor gsonProcessor = new GsonProcessor().setBuilder(gsonBuilder);

		JwtProcessor jwtProcessor = new JwtProcessor().setJsonProcessor(gsonProcessor);
		jwtProcessor.setIssuer("Issuer1");

		JwtHeader header = jwtProcessor.newHeader();
		JwtPayload payload = jwtProcessor.newPayload().setExpiresIn(1, TimeUnit.DAYS);

		try {
			String jwt = jwtProcessor.toJwt(new JwtToken(header, payload));
			Assert.assertNotNull(jwt);

			JwtToken token = jwtProcessor.parseJwt(jwt);

			Assert.assertNotNull(token);
			Assert.assertNotNull(token.getHeader());
			Assert.assertNotNull(token.getPayload());

			Assert.assertEquals(token.getHeader().getType(), "JWT");
			Assert.assertEquals(token.getPayload().get("iss"), "Issuer1");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}
}
